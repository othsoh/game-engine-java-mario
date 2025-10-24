package org.sid.editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.sid.jade.MouseListener;
import org.sid.jade.Window;

public class GameViewWindow {

    private static float leftX, rightX, topY, bottomY;

    public static void imgui(){
        ImGui.begin("Game ViewPort", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewPort();
        ImVec2 windowPos = getViewPortPosition(windowSize);




        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureID = Window.getFrameBuffer().getTextureId();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0,1,1,0);

        MouseListener.setViewPortPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setViewPortSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private static ImVec2 getLargestSizeForViewPort() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getViewPortAspectRatio();

        if (aspectHeight > windowSize.y){
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getViewPortAspectRatio();
        }
        return new ImVec2(aspectWidth , aspectHeight );

    }
    private static ImVec2 getViewPortPosition(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewPortX = (windowSize.x /2.0f) - (aspectSize.x/2.0f);
        float viewPortY = (windowSize.y /2.0f) - (aspectSize.y/2.0f);

        return new ImVec2(viewPortX + ImGui.getCursorPosX(), viewPortY + ImGui.getCursorPosY());
    }

    public static boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }
}
