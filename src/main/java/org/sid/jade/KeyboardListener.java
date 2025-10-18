package org.sid.jade;

import org.lwjgl.glfw.GLFW;

public class KeyboardListener {
    private static KeyboardListener keyboardListener;
    private boolean keyPressed[] = new boolean[GLFW.GLFW_KEY_LAST + 2];

    private KeyboardListener(){
    }

    private static KeyboardListener get() {
        if (keyboardListener == null) {
            keyboardListener = new KeyboardListener();
        }
        return keyboardListener;
    }
    
    public static void keyCallBack(long window, int key, int scancode, int action, int mods){
        if(action== GLFW.GLFW_PRESS){
            get().keyPressed[key]= true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key]= false;
        }
    }

    public static boolean isKeyPresed(int keycode){
        return get().keyPressed[keycode];
    }
}
