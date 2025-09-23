package org.sid.renderer;

import imgui.*;
import imgui.flag.ImFontAtlasFlags;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import org.sid.Scene;

public class ImguiLayer {

    private boolean showText = false;
    private ImGuiIO io;

    // Remove the initializer here to avoid the assertion error.
    // The IO instance will be passed to the setup method.

    public void setup(ImGuiIO io) {
        this.io = io; // Assign the IO instance from the Window class
        io.setIniFilename("imgui.ini");

        // Important: Use getFonts() on the provided io instance
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();

//        // Set FreeType renderer before adding fonts
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