package org.sid;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {

    private int width, height;
    private String title;

    private static Window window = null;
    private long glfwWindow;


    public float r, g, b, a;

    private static Scene currentScene = null ;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Unknown scene"+ newScene;
                break;
        }
    }


    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }
    public static Scene getScene(){
        return get().currentScene;
    }

    public void run() {

        System.out.println("Window created with title: " + this.title + Version.getVersion());

        init();
        loop();

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void init() {

        // Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        // Create the window
        glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (glfwWindow == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //Call Mouse and Keyboard Callbacks
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePositionCallBack);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrolCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);

        GLFW.glfwSetKeyCallback(glfwWindow, keyboardListener::keyCallBack);

        // Create the OpenGl Context
        GLFW.glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Show the window
        GLFW.glfwShowWindow(glfwWindow);

        // Create the Capabilities
        // This is important for OpenGL to work
        // It must be called after the window is created and made current
        // and before any OpenGL calls are made
        GL.createCapabilities();

        Window.changeScene(0);
    }

    private void loop() {

        float beginTime = (float) GLFW.glfwGetTime();
        float endTime = 0.0f;
        float dt = -1.0f;

        // Run the rendering loop until the user closes the window
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            // Poll for window events
            GLFW.glfwPollEvents();

            // Set the clear color to red
            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if (dt>= 0){
                currentScene.update(dt);
            }

            // Swap the buffers
            GLFW.glfwSwapBuffers(glfwWindow);

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

    }


}
