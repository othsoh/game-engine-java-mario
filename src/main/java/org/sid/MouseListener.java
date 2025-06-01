package org.sid;

import org.lwjgl.glfw.GLFW;

public class MouseListener {

    private static MouseListener mouseListener;
    private double xPos, yPos, scrollX, scrollY, lastXPos, lastYPos;
    private boolean mouseButtonPressed[] = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

    private boolean isDragging;

    private MouseListener() {
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastXPos = 0.0;
        this.lastYPos = 0.0;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
    }

    private static MouseListener get() {
        if (MouseListener.mouseListener == null) {
            MouseListener.mouseListener = new MouseListener();
        }
        return MouseListener.mouseListener;
    }

    public static void MousePositionCallBack(long window, double xpos, double ypos) {
        get().lastYPos = get().yPos;
        get().lastXPos = get().xPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] ||get().mouseButtonPressed[2] ;

    }

    public static void MouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void MouseScrolCallback(long window, double xoffset, double yoffset) {
        get().scrollX = xoffset;
        get().scrollY = yoffset;
    }

    public static void endFrame() {
        get().scrollY = 0.0;
        get().scrollX = 0.0;
        get().lastXPos = get().xPos;
        get().lastYPos = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().xPos - get().lastXPos);
    }

    public static float getDy() {
        return (float) (get().yPos - get().lastYPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        return get().mouseButtonPressed[button];
    }

}
