package org.sid;

import org.sid.renderer.ImguiLayer;

public class BetterMain {
    public static void main(String[] args) {
        Window window = new Window(new ImguiLayer());
        window.init();
        window.run();
        window.destroy();
    }
}
