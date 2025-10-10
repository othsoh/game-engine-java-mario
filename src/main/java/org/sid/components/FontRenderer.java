package org.sid.components;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Sprite Renderer Component Found inside Font Renderer");

        }

    }


    @Override
    public void update(float dt) {

    }
}
