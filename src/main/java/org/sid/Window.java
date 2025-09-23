package org.sid;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryUtil;
import org.sid.renderer.ImguiLayer;

public class Window {

    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;

    public float r = 1, g = 1, b = 1, a = 1;
    private static Scene currentScene = null;

    // --- ImGui integration ---
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private String glslVersion = "#version 330 core";
    private ImguiLayer imguiLayer; // optional: pass your own UI builder

    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }
    public Window(ImguiLayer imguiLayer){
        this();
        this.imguiLayer = imguiLayer;

    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                throw new IllegalArgumentException("Unknown scene " + newScene);
        }
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (window == null) window = new Window();
        return window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void setImGuiLayer(ImguiLayer layer) {
        this.imguiLayer = layer;
    }

    public void run() {
        System.out.println("Window created with title: " + this.title + " " + Version.getVersion());
        init();
        loop();
        destroy();
    }

    public void init() {
        // GLFW error callback
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindow == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Input callbacks
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePositionCallBack);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrolCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, keyboardListener::keyCallBack);

        GLFW.glfwMakeContextCurrent(glfwWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        GL11C.glEnable(GL11C.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // --- ImGui setup: CONTEXT CREATION ---
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        imguiLayer.setup(io);          // add fonts here
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);    // builds atlas with your fonts

        Window.changeScene(0);
    }

    private void loop() {
        float beginTime = (float) GLFW.glfwGetTime();
        float dt = -1.0f;

        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GLFW.glfwPollEvents();

            // Update & clear
            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if (dt >= 0 && currentScene != null) {
                currentScene.update(dt);
            }

            // --- ImGui frame start ---
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            // Draw your ImGui UI here or via an injected layer
            if (imguiLayer != null) {
                imguiLayer.imgui();
            }

            // Render ImGui
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            // Multi-viewport
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backup = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backup);
            }

            GLFW.glfwSwapBuffers(glfwWindow);

            float endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
