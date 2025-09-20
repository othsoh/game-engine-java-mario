package org.sid.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL45;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    private String filepath;
    private int textureID;
    private int width, height ;


    public Texture(String filepath) {
        this.filepath = filepath;

        //Generate texture on GPU
        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        //set texture params

        //repeat image
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        //when stretch image pixelate
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); // nearest pixel turning to the next pixel (looking like one big pixel)
        //when shrink image pixelate
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);
            int format;
            if (channels.get(0) == 4) {
                format = GL11.GL_RGBA;
            } else if (channels.get(0) == 3) {
                format = GL11.GL_RGB;
            } else {
                format = GL11.GL_RED;
            }
            
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width.get(0), height.get(0),
                    0, format, GL11.GL_UNSIGNED_BYTE, image);
            
            System.out.println("Loaded texture: " + filepath + 
                               " (" + width.get(0) + "x" + height.get(0) + 
                               ", " + channels.get(0) + " channels)");
        } else {
            assert false: "Error: Couldn't load Image " + filepath + " ~~~~ lvl: Texture";
        }

        STBImage.stbi_image_free(image);
    }
    
    public void bind(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }
    
    public void unbind(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
