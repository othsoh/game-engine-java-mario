package org.sid;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.CallbackI;
import org.sid.components.FontRenderer;
import org.sid.components.SpriteRenderer;
import org.sid.renderer.Shader;
import org.sid.renderer.Texture;
import org.sid.utils.Time;
import org.w3c.dom.Text;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {
    private float[] vertexArray = {

            //Position                   //Color                      //UV coords       //Text Description
            100.5f, 0.5f, 0.0f,          1.0f, 0.0f, 0.0f, 1.0f,      1, 0,             //bottom right      red
            0.5f, 100.5f, 0.0f,          0.0f, 1.0f, 0.0f, 1.0f,      0, 1,              //top left          green
            100.5f, 100.5f, 0.0f,        0.0f, 0.0f, 1.0f, 1.0f,      1, 1,              //top right         blue
            0.5f, 0.5f, 0.0f,            1.0f, 1.0f, 0.0f, 1.0f,      0, 0,              //bottom left       yellow
    };

    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    private Texture testTexture;

    private GameObject testObj;

    private boolean isFirstTime = true;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {

        System.out.println("Creating test object");
        this.testObj= new GameObject("test object");

        System.out.println("Adding Components to tst object");
        testObj.addComponent(new SpriteRenderer());
        testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);






        this.camera = new Camera(new Vector2f());


        defaultShader = new Shader("assets/shaders/default.glsl");

        this.testTexture = new Texture("assets/images/testImage.jpg");

        defaultShader.compileAndLink();

        //Generate VBO, VAO and EBO + send it to GPU

        vaoID = GL30.glGenVertexArrays();
        GL43.glBindVertexArray(vaoID);

        //Create float buffer
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create vbo and upload the vertex buffer
        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL15.GL_STATIC_DRAW);


        //ADD the vertex attributes

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeByte = (positionSize + colorSize+ uvSize) * Float.BYTES;

        GL20.glVertexAttribPointer(0, positionSize, GL11.GL_FLOAT, false, vertexSizeByte, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, colorSize, GL11.GL_FLOAT, false, vertexSizeByte, positionSize * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL20.glVertexAttribPointer(2,uvSize,GL11.GL_FLOAT,false,vertexSizeByte,(positionSize+colorSize) * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {


        defaultShader.use();

        //upload texture to shader
        defaultShader.uploadTexture("textureSampler",0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        //Bind VAO
        GL30.glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2); // Enable texture coordinates attribute

        GL11C.glDrawElements(GL11C.GL_TRIANGLES, elementArray.length, GL11C.GL_UNSIGNED_INT, 0);


        //disable and unbind
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2); // Disable texture coordinates attribute
        GL30.glBindVertexArray(0);

        testTexture.unbind();
        defaultShader.detach();

        if (isFirstTime) {
            System.out.println("Creating test object 2");
            GameObject testObj2 = new GameObject("test object 2");
            testObj2.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(testObj2);
            isFirstTime = false ;
        }

        for (GameObject gameObject : gameObjects){
            gameObject.update(dt);
        }

    }

}
