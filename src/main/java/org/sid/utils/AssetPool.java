package org.sid.utils;

import org.sid.GameObject;
import org.sid.components.Sprite;
import org.sid.components.SpriteSheet;
import org.sid.renderer.Shader;
import org.sid.renderer.Texture;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public  class AssetPool {
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();


    public static Shader getShader( String resourceName) {
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


    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }
        else{
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);

        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);

        assert AssetPool.spriteSheets.containsKey(file.getAbsolutePath()) : "Error: Tried to retrieve a non existing sprite: "+ resourceName;

        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null);

    }

}
