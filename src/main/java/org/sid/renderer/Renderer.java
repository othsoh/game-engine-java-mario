package org.sid.renderer;

import org.sid.GameObject;
import org.sid.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject){
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);

        if (sprite != null){
            add(sprite);
        }
    }

    private void add(SpriteRenderer spriteRenderer){
        boolean added = false;
        for (RenderBatch batch : batches){
            if(batch.isHasRoom()){
                batch.addSprite(spriteRenderer);
                added = true;
                break;
            }

        }
        if (!added){
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batches.add(batch);
            batch.addSprite(spriteRenderer);
        }
    }
    public void render(){
        for( RenderBatch batch: batches){
            batch.render();
        }
    }
}
