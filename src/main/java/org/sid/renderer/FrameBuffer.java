package org.sid.renderer;

import org.lwjgl.opengl.GL30;
import org.w3c.dom.Text;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

    private Texture texture;
    private int fboID;

    public FrameBuffer(int width, int height){

        fboID = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        //Create Texture and Attach it to the frameBuffer

        texture = new Texture(width, height);
        GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);

        /*
        Textures are great when you need to read the rendered result later, for example in a shader.

        But sometimes, you only need to store data temporarily (like depth or stencil info) that you won’t sample later.

        That’s what renderbuffers are for — they are optimized storage for data that you don’t need to access directly
         in your shader.
        */
        int rboID = glGenRenderbuffers();

        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        //check if the framebuffer is ready to use

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE :
                "ERROR::FRAMEBUFFER:: Framebuffer is not complete!";

        //unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


    }

    public int getTextureId() {
        return texture.getId();
    }


    public int getFboID() {
        return fboID;
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

    }
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }
}
