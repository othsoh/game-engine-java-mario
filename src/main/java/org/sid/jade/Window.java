package org.sid.jade;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryUtil;
import org.sid.editor.GameViewWindow;
import org.sid.renderer.DebugDraw;
import org.sid.renderer.FrameBuffer;
import org.sid.scenes.LevelEditorScene;
import org.sid.scenes.LevelScene;
import org.sid.scenes.Scene;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;


public class Window {

    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;
    private FrameBuffer frameBuffer;

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
//        this.imguiLayer = imguiLayer;

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
        currentScene.load();
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
    public void initWindow(){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindow == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Input callbacks
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePositionCallBack);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrolCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyboardListener::keyCallBack);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        GLFW.glfwMakeContextCurrent(glfwWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        GL11C.glEnable(GL11C.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);


    }
    public void initImgui(){

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        this.imguiLayer = new ImguiLayer(glfwWindow);
        this.imguiLayer.setup(io);

        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);    // builds atlas with your fonts
        //2560:1440
        this.frameBuffer = new FrameBuffer(2560, 1440);
        GL11.glViewport(0,0 ,2560, 1440 );
    }

    public void init() {
        initWindow();
        initImgui();

        Window.changeScene(0);
    }

    private void loop() {
        float beginTime = (float) GLFW.glfwGetTime();
        float dt = -1.0f;
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GLFW.glfwPollEvents();

            DebugDraw.beginFrame();
            this.frameBuffer.bind();

            // Update & clear
            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);


            if (dt >= 0 && currentScene != null) {
                DebugDraw.draw();

                currentScene.update(dt);
            }

            this.frameBuffer.unbind();

            // --- ImGui frame start ---
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            //setup dock space
            setupDockSpace();


            // Draw your ImGui UI here or via an injected layer
            if (imguiLayer != null) {
                currentScene.sceneImgui();
                imguiLayer.imgui();
            }
            GameViewWindow.imgui();
           ImGui.end();
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
        currentScene.saveAndExit();
    }

    private void setupDockSpace() {

        int windowFlags = ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.MenuBar;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove;
        windowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockSpace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        //setup DockSpace

        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static FrameBuffer getFrameBuffer(){
        return get().frameBuffer;
    }
    public static float getViewPortAspectRatio(){
        return 16.0f/9.0f;
    }
}
