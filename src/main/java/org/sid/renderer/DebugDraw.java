package org.sid.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.sid.jade.Window;
import org.sid.utils.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        lines.add(new Line2D(from, to, color, lifetime));
    }

    public static void addLine2D(Vector2f from, Vector2f to) {
        lines.add(new Line2D(from, to, new Vector3f(0.0f, 0.0f , 0.0f),1));
    }

    public static void addLine2D(Vector2f from, Vector2f to ,Vector3f color) {
        lines.add(new Line2D(from, to, color,1));
    }

    public static int getLinesCount(){
        return lines.size();
    }

}
