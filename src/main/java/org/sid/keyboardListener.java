package org.sid;

import org.lwjgl.glfw.GLFW;

public class keyboardListener {
    private static keyboardListener keyboardListener;
    private boolean keyPressed[] = new boolean[GLFW.GLFW_KEY_LAST + 2];

    private keyboardListener(){
    }

    private static keyboardListener get() {
        if (keyboardListener.keyboardListener == null) {
            keyboardListener.keyboardListener = new keyboardListener();
        }
        return keyboardListener.keyboardListener;
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
