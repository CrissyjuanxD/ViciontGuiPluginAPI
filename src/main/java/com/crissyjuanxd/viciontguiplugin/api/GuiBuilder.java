package com.crissyjuanxd.viciontguiplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GuiBuilder {
    private final String guiId;
    private String target;
    private String backgroundTexture;
    private int bgWidth;
    private int bgHeight;
    private int bgTexWidth;
    private int bgTexHeight;
    private boolean fixedScale = false;
    private final List<JsonObject> elements = new ArrayList<>();

    private String openSound;
    private float openPitch = 1.0f;
    private float openVolume = 1.0f;
    private String closeSound;
    private float closePitch = 1.0f;
    private float closeVolume = 1.0f;

    private GuiBuilder(String guiId) {
        this.guiId = guiId;
    }

    public static GuiBuilder create(String guiId) {
        return new GuiBuilder(guiId);
    }

    public GuiBuilder target(GuiTarget target) {
        this.target = target != null && target != GuiTarget.SCREEN ? target.name().toLowerCase() : null;
        return this;
    }

    public GuiBuilder fixedScale(boolean fixedScale) {
        this.fixedScale = fixedScale;
        return this;
    }

    public GuiBuilder background(String texture, int width, int height) {
        return this.background(texture, width, height, width, height);
    }

    public GuiBuilder background(String texture, int width, int height, int texWidth, int texHeight) {
        this.backgroundTexture = texture;
        this.bgWidth = width;
        this.bgHeight = height;
        this.bgTexWidth = texWidth;
        this.bgTexHeight = texHeight;
        return this;
    }

    public GuiBuilder openSound(String sound, float pitch, float volume) {
        this.openSound = sound;
        this.openPitch = pitch;
        this.openVolume = volume;
        return this;
    }

    public GuiBuilder closeSound(String sound, float pitch, float volume) {
        this.closeSound = sound;
        this.closePitch = pitch;
        this.closeVolume = volume;
        return this;
    }

    public GuiBuilder element(GuiElementBuilder element) {
        this.elements.add(element.build());
        return this;
    }

    public String guiId() {
        return this.guiId;
    }

    public String buildJson() {
        JsonObject root = new JsonObject();
        root.addProperty("gui_id", this.guiId);
        if (this.target != null) {
            root.addProperty("target", this.target);
        }

        if (this.fixedScale) {
            root.addProperty("fixed_scale", true);
        }

        if (this.openSound != null) {
            root.addProperty("open_sound", this.openSound);
            root.addProperty("open_pitch", this.openPitch);
            root.addProperty("open_volume", this.openVolume);
        }
        if (this.closeSound != null) {
            root.addProperty("close_sound", this.closeSound);
            root.addProperty("close_pitch", this.closePitch);
            root.addProperty("close_volume", this.closeVolume);
        }

        if (this.backgroundTexture != null) {
            JsonObject bg = new JsonObject();
            bg.addProperty("texture", this.backgroundTexture);
            bg.addProperty("width", this.bgWidth);
            bg.addProperty("height", this.bgHeight);
            bg.addProperty("texture_width", this.bgTexWidth);
            bg.addProperty("texture_height", this.bgTexHeight);
            root.add("background", bg);
        }

        JsonArray arr = new JsonArray();
        Objects.requireNonNull(arr);
        this.elements.forEach(arr::add);
        root.add("elements", arr);
        return root.toString();
    }
}