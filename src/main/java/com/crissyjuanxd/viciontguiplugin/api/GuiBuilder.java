package com.crissyjuanxd.viciontguiplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

// Construye una GUI completa: gui_id + target opcional (hud/inventory) + background opcional + elementos
public final class GuiBuilder {
    private final String guiId;
    private String target; // null = pantalla normal (screen)
    private String backgroundTexture;
    private int bgWidth, bgHeight, bgTexWidth, bgTexHeight;
    private final List<JsonObject> elements = new ArrayList<>();

    private GuiBuilder(String guiId) { this.guiId = guiId; }

    public static GuiBuilder create(String guiId) { return new GuiBuilder(guiId); }

    public GuiBuilder target(GuiTarget target) {
        this.target = (target == null || target == GuiTarget.SCREEN) ? null : target.name().toLowerCase();
        return this;
    }

    public GuiBuilder background(String texture, int width, int height) {
        return background(texture, width, height, width, height);
    }

    public GuiBuilder background(String texture, int width, int height, int texWidth, int texHeight) {
        this.backgroundTexture = texture;
        this.bgWidth = width; this.bgHeight = height;
        this.bgTexWidth = texWidth; this.bgTexHeight = texHeight;
        return this;
    }

    public GuiBuilder element(GuiElementBuilder element) { elements.add(element.build()); return this; }

    public String guiId() { return guiId; }

    public String buildJson() {
        JsonObject root = new JsonObject();
        root.addProperty("gui_id", guiId);
        if (target != null) root.addProperty("target", target);
        if (backgroundTexture != null) {
            JsonObject bg = new JsonObject();
            bg.addProperty("texture", backgroundTexture);
            bg.addProperty("width", bgWidth);
            bg.addProperty("height", bgHeight);
            bg.addProperty("texture_width", bgTexWidth);
            bg.addProperty("texture_height", bgTexHeight);
            root.add("background", bg);
        }
        JsonArray arr = new JsonArray();
        elements.forEach(arr::add);
        root.add("elements", arr);
        return root.toString();
    }
}