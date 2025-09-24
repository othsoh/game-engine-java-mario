package org.sid.components;

import org.joml.Vector2f;
import org.sid.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    List<Sprite> sprites = new ArrayList<>();

    private Texture texture;

    public SpriteSheet(){

    }

    public void init(Texture texture,int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.sprites = new ArrayList<>();
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for (int i =0; i < numSprites; i++){
            float rightX = (currentX + spriteWidth) /  (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();
            float leftX = currentX / (float) texture.getWidth();
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();

            Vector2f[] textCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(texture);
            sprite.setTexCoords(textCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()){
                currentX=0;
                currentY -= spriteHeight+ spacing;
            }
        }
    }
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


//    public SpriteSheet(Texture texture,int spriteWidth, int spriteHeight, int numSprites, int spacing){
//        this.sprites = new ArrayList<>();
//
//        this.texture = texture;
//        int currentX = 0;
//        int currentY = texture.getHeight() - spriteHeight;
//
//        for (int i =0; i < numSprites; i++){
//            float rightX = (currentX + spriteWidth) /  (float) texture.getWidth();
//            float bottomY = currentY / (float) texture.getHeight();
//            float leftX = currentX / (float) texture.getWidth();
//            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
//
//            Vector2f[] textCoords = new Vector2f[]{
//                    new Vector2f(rightX, topY),
//                    new Vector2f(rightX, bottomY),
//                    new Vector2f(leftX, bottomY),
//                    new Vector2f(leftX, topY)
//            };
//
//            Sprite sprite = new Sprite(this.texture, textCoords);
//            this.sprites.add(sprite);
//
//            currentX += spriteWidth + spacing;
//            if(currentX >= texture.getWidth()){
//                currentX=0;
//                currentY -= spriteHeight+ spacing;
//            }
//        }
//    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }

}
