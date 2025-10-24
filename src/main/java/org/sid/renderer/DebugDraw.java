package org.sid.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.sid.jade.Window;
import org.sid.utils.AssetPool;
import org.sid.utils.JMath;
import org.sid.utils.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class DebugDraw {

    public static final int MAX_LINES = 500;

    private static final List<Line2D> lines = new ArrayList<>();

    // a line is 2 vertices and each vertex has 6 floats (x,y,z,r,g,b)
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];

    private static final Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean isStarted = false;

    public static void start() {
        //generate VAO
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        //Create VBO
        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL15.GL_DYNAMIC_DRAW);

        //Enable the vertex array attributes
        //POSITION
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        //COLOR
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

    }

    public static void beginFrame() {
        if (!isStarted) {
            start();
            isStarted = true;
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw() {

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = 0.0f;

                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6;

            }
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        //use Shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        //Bind the VAO
        GL30.glBindVertexArray(vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_LINES, 0, lines.size() * 6 * 2);

        //Disable + Detach
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shader.detach();

    }




    public static void addLine2D(Vector2f from, Vector2f to) {
        lines.add(new Line2D(from, to, new Vector3f(0.0f, 0.0f , 0.0f),1));
    }


    public static void addLine2D(Vector2f from, Vector2f to ,Vector3f color) {
        lines.add(new Line2D(from, to, color,1));
    }
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        lines.add(new Line2D(from, to, color, lifetime));
    }

    public static void addBox2D(Vector2f center,Vector2f dimensions , float rotation, Vector3f color, int lifetime) {

        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).div(2.0f));

        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).div(2.0f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(max.x, min.y),
                new Vector2f(max.x, max.y), new Vector2f(min.x, max.y)
        };


        if (rotation != 0f){
            for (Vector2f vertex: vertices){
                JMath.rotate(vertex,rotation,center);
            }
        }
        addLine2D(vertices[0], vertices[1],color, lifetime);
        addLine2D(vertices[1], vertices[2],color, lifetime);
        addLine2D(vertices[2], vertices[3],color, lifetime);
        addLine2D(vertices[3], vertices[0],color, lifetime);

    }

    public static void addBox2D(Vector2f center,Vector2f dimensions) {

        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).div(2.0f));

        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).div(2.0f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(max.x, min.y),
                new Vector2f(max.x, max.y), new Vector2f(min.x, max.y)
        };

        Vector3f color = new Vector3f(1,1,1);
        int lifetime = 1;

        addLine2D(vertices[0], vertices[1],color, lifetime);
        addLine2D(vertices[1], vertices[2],color, lifetime);
        addLine2D(vertices[2], vertices[3],color, lifetime);
        addLine2D(vertices[3], vertices[0],color, lifetime);

    }


    public static void addCircle2D(Vector2f center,float radius, Vector3f color, int lifetime) {


        Vector2f firstVertex = new Vector2f(center.x, center.y + radius);

        int numOfLines = (int) (360/ Settings.CIRCLE_SLICES);
        Vector2f[] circleVertices = new Vector2f[numOfLines];
        circleVertices[0] = firstVertex;
        for (int i = 1; i<numOfLines; i++){
            Vector2f nextVertex = new Vector2f(circleVertices[i-1]);
            JMath.rotate(nextVertex, Settings.CIRCLE_SLICES,center);
            circleVertices[i]=nextVertex;

            addLine2D(circleVertices[i-1], circleVertices[i],color, lifetime);

        }
        addLine2D(circleVertices[circleVertices.length-1], circleVertices[0], color, lifetime);
    }
}
