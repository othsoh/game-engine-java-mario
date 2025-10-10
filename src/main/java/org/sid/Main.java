package org.sid;

import org.sid.jade.Window;
import org.sid.jade.ImguiLayer;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();          // singleton
        window.setImGuiLayer(new ImguiLayer()); // give it the UI
        window.run();
    }
}