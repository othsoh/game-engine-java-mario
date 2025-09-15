package org.sid.utils;

import org.sid.GameObject;
import org.sid.renderer.Shader;
import org.sid.renderer.Texture;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public  class AssetPool {
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Shader> shaders = new HashMap<>();


    public static Shader getShaders( String resourceName) {
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compileAndLink();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }


    public static Texture getTextures(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }
        else{
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

}
