package org.sid.jade;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import org.sid.editor.GameViewWindow;

import static org.lwjgl.glfw.GLFW.*;

public class ImguiLayer {

    private long glfwWindow;
    private boolean showText = false;
    private ImGuiIO io;

    // Remove the initializer here to avoid the assertion error.
    // The IO instance will be passed to the setup method.
    public ImguiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }
    public ImguiLayer(){
        this.glfwWindow = 0;
    }

    public void setup(ImGuiIO io) {
        this.io = io; // Assign the IO instance from the Window class
        io.setIniFilename("imgui.ini");
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);

        // Important: Use getFonts() on the provided io instance
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();

//        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
//            if (action == GLFW_PRESS) {
//                io.setKeysDown(key, true);
//            } else if (action == GLFW_RELEASE) {
//                io.setKeysDown(key, false);
//            }
//
//            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
//            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
//            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
//            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
//
//            if (!io.getWantCaptureKeyboard()) {
//                KeyboardListener.keyCallBack(w, key, scancode, action, mods);
//            }
//        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse() || GameViewWindow.getWantCaptureMouse()) {
                MouseListener.MouseButtonCallback(w, button, action, mods);
            }
        });

        // Set FreeType renderer before adding fonts
//        fontAtlas.setFreeTypeRenderer(true);

        // Clear existing fonts if necessary. This will remove the default font.
        fontAtlas.clear();

        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 16.5f, fontConfig);
        fontAtlas.build();
        fontConfig.destroy();
    }

    public void imgui() {
        // Do NOT call setup() here. This method should only draw the UI.
        ImGui.begin("Cool Window");

        if (ImGui.button("I am a button")) {
            showText = true;
        }

        if (showText) {
            ImGui.text("You clicked a button");
            ImGui.sameLine();
            if (ImGui.button("Stop showing text")) {
                showText = false;
            }
        }
        ImGui.end();
    }
}