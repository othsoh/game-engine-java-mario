package org.sid.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL20C;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Shader {

    private int shaderProgramID;
    private String filepath;
    private String vertexSource;
    private String fragmentSource;

    private boolean isBeingUsed= false;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {

            String source = new String(Files.readAllBytes(Path.of(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            //index of the word next to #type: (vertex/fragment)
            int index = source.indexOf("#type") + 6;
            //index of the end of the word #type: (vertex/fragment)
            int endOfWord = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, endOfWord).trim();

            index = source.indexOf("#type", endOfWord) + 6;
            endOfWord = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, endOfWord).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else throw new IOException("Error splitting the first pattern :" + firstPattern);

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else throw new IOException("Error splitting the first pattern :" + secondPattern);


        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error Opening the Shader file: " + filepath;
        }

    }

    public void compileAndLink() {
        int vertexID, fragmentID;

        //load and compile vertex shader
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        // Pass the shader source to GPU
        GL20.glShaderSource(vertexID,vertexSource);
        GL20C.glCompileShader(vertexID);

        //check for errors
        int success = GL20.glGetShaderi(vertexID,GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE){
            int len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed");
            System.out.println(GL20.glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //load and compile fragment shader
        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        // PAss the shader source to GPU
        GL20.glShaderSource(fragmentID,fragmentSource);
        GL20C.glCompileShader(fragmentID);

        //check for errors
        success = GL20.glGetShaderi(fragmentID,GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE){
            int len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed");
            System.out.println(GL20.glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //LINK shaders
        shaderProgramID = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgramID, vertexID);
        GL20.glAttachShader(shaderProgramID, fragmentID);

        GL20.glLinkProgram(shaderProgramID);

        //Check for linking Errors

        success = GL20.glGetProgrami(shaderProgramID, GL20.GL_LINK_STATUS);
        if (success == GL11.GL_FALSE){
            int len = GL20.glGetProgrami(shaderProgramID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking shaders failed");
            System.out.println(GL20.glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use() {
        if (!isBeingUsed) {
            //bind Shader Program
            GL20.glUseProgram(shaderProgramID);
            isBeingUsed= true;
        }
    }

    public void detach() {
        //unbind Shader Program
        GL20.glUseProgram(0);
        isBeingUsed= false;
    }

    public void uploadMat4f(String varName, Matrix4f matrix4f){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID,varName);
        use();
        FloatBuffer MatBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(MatBuffer);
        GL20.glUniformMatrix4fv(varLocation, false,MatBuffer);
    }


    public void uploadMat3f(String varName, Matrix3f matrix3f){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID,varName);
        use();
        FloatBuffer MatBuffer = BufferUtils.createFloatBuffer(9);
        matrix3f.get(MatBuffer);
        GL20.glUniformMatrix3fv(varLocation, false,MatBuffer);
    }


    public void uploadVec3f(String varName, Vector3f vector3f){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform3f(varLocation, vector3f.x, vector3f.y ,vector3f.z);
    }


    public void uploadVec4f(String varName, Vector4f vector4f){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform4f(varLocation, vector4f.x, vector4f.y ,vector4f.z, vector4f.w);
    }

    public void uploadFloat(String varName, float var){
        int varLocation = GL20C.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform1f(varLocation, var);
    }

    public void uploadInt(String varName, int var){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform1i(varLocation, var);

    }

    public void uploadTexture(String varName, int slot){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform1i(varLocation, slot);

    }
    public void uploadIntArray(String varName, int[] slots){
        int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
        use();
        GL20.glUniform1iv(varLocation, slots);

    }
}
