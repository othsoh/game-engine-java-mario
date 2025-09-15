package org.sid.renderer;

import org.joml.Vector4f;
import org.lwjgl.opengl.*;
import org.sid.Window;
import org.sid.components.SpriteRenderer;
import org.sid.utils.AssetPool;

import java.awt.*;

public class RenderBatch {

    //vertex format:
    //===================
    //POS               COLOR
    //float float       float float float float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET=0;
    private final int COLOR_OFFSET=(POS_OFFSET +POS_SIZE) * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;


    private int vaoID, vboID;

    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        shader = AssetPool.getShaders("assets/shaders/default.glsl");

        this.sprites = new  SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 vertices per quad
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom=true;
    }


    public void start(){
        //Generate and bind a vertex array Object
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        //the dynamic draw attribute to tell gpu that these vertices are gonna change more often, so put it in a place (in gpu) easy to reach/change
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,vertices.length * Float.BYTES, GL15.GL_DYNAMIC_DRAW);

        //create and upload indices buffer
        int eboID = GL15.glGenBuffers();
        int[] indices = generateIndices();
        GL15.glBindBuffer(GL15C.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        //Enable Buffer attribute pointers
        GL20C.glVertexAttribPointer(0,POS_SIZE, GL11.GL_FLOAT,false,VERTEX_SIZE_BYTES,POS_OFFSET);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, COLOR_SIZE,GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        GL20.glEnableVertexAttribArray(1);

    }
    public void addSprite(SpriteRenderer spriteRenderer){
        // Get index of last sprite
        int index = this.numSprites;
        sprites[index] = spriteRenderer;
        numSprites++;

        // Add properties to local vertices Array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize ){
            this.hasRoom = false;
        }
    }



    public void render(){
        // for now,  reuffer all data every frame
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);

        // use shader

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());


        GL30.glBindVertexArray(vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11C.glDrawElements(GL11C.GL_TRIANGLES, numSprites * 6,GL11C.GL_UNSIGNED_INT, 0);

        //disable the vertex array and unbind
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30C.glBindVertexArray(0);

        shader.detach();
    }

    private int[] generateIndices() {

        // 6 indices per quad (3 per triangle)
        int[] elements  = new int[6 * maxBatchSize];
        for (int  i = 0; i < maxBatchSize ; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        //# #
        //# #
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i=0; i<4; i++){
            if (i==1){
                yAdd = 0.0f;
            } else if (i==2) {
                xAdd = 0.0f;
            } else if (i==3) {
                yAdd = 1.0f;
            }

            //load vertices
            vertices[offset] = sprite.gameObject.transform.getPosition().x +(xAdd * sprite.gameObject.transform.getScale().x);
            vertices[offset+1] = sprite.gameObject.transform.getPosition().y +(yAdd * sprite.gameObject.transform.getScale().y);

            //load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            offset += VERTEX_SIZE;
        }

    }

    private void loadElementIndices(int[] elements, int index) {

        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        //Quad 1 (2 triangles)      Quad 2 (2 triangles)
        // 3, 2, 0, 0, 2, 1         7, 6, 4, 4, 6, 5

        //Triangle 1:
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        //Triangle 2:
        elements[offsetArrayIndex+ 3] = offset ;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset+ 1;


    }

    public boolean isHasRoom() {
        return hasRoom;
    }

    public void setHasRoom(boolean hasRoom) {
        this.hasRoom = hasRoom;
    }
}
