package com.crissyjuanxd.viciontguiplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GuiElementBuilder {
    private final JsonObject json = new JsonObject();
    private final List<JsonObject> tooltip = new ArrayList<>();
    private JsonArray richMessage;
    private String textAlign = "center";

    private GuiElementBuilder() {}

    public static GuiElementBuilder create(String id, String type) {
        GuiElementBuilder b = new GuiElementBuilder();
        b.json.addProperty("id", id);
        b.json.addProperty("type", type);
        return b;
    }

    public static GuiElementBuilder button(String id, String texture, String action) {
        GuiElementBuilder b = create(id, "custom_button").texture(texture);
        if (action != null) b.action(action);
        return b;
    }

    public static GuiElementBuilder itemSlot(String id, String frameTexture, String itemId) {
        return create(id, "item_slot").texture(frameTexture).itemId(itemId);
    }

    public static GuiElementBuilder image(String id, String texture) {
        return create(id, "image").texture(texture);
    }

    public static GuiElementBuilder invisibleButton(String id, String action) {
        return create(id, "invisible_button").action(action);
    }

    public static GuiElementBuilder text(String id, String text, String color, float scale, boolean bold) {
        return create(id, "text").textContent(text, color, scale, bold);
    }

    public static GuiElementBuilder richText(String id, JsonArray message, int maxWidth, Integer maxHeight, String color) {
        return create(id, "rich_text").richMessage(message, maxWidth, maxHeight, color);
    }

    public static GuiElementBuilder entity(String id, String entityId, String entityName, int entityScale) {
        return create(id, "entity").entityData(entityId, entityName, entityScale);
    }

    public GuiElementBuilder hoverSound(String sound, float pitch, float volume) {
        this.json.addProperty("hover_sound", sound);
        this.json.addProperty("hover_pitch", pitch);
        this.json.addProperty("hover_volume", volume);
        return this;
    }

    public GuiElementBuilder clickSound(String sound, float pitch, float volume) {
        this.json.addProperty("click_sound", sound);
        this.json.addProperty("click_pitch", pitch);
        this.json.addProperty("click_volume", volume);
        return this;
    }

    public GuiElementBuilder anchor(String anchor) {
        this.json.addProperty("anchor", anchor);
        return this;
    }

    public GuiElementBuilder position(int x, int y) {
        this.json.addProperty("x", x);
        this.json.addProperty("y", y);
        return this;
    }

    public GuiElementBuilder animSpeed(float speed) {
        this.json.addProperty("anim_speed", speed);
        return this;
    }

    public GuiElementBuilder size(int width, int height) {
        this.json.addProperty("width", width);
        this.json.addProperty("height", height);
        return this;
    }

    public GuiElementBuilder textureSize(int texWidth, int texHeight) {
        this.json.addProperty("texture_width", texWidth);
        this.json.addProperty("texture_height", texHeight);
        return this;
    }

    public GuiElementBuilder texture(String texture) {
        this.json.addProperty("texture", texture);
        return this;
    }

    public GuiElementBuilder action(String action) {
        this.json.addProperty("action", action);
        return this;
    }

    public GuiElementBuilder itemId(String itemId) {
        this.json.addProperty("item_id", itemId);
        return this;
    }

    public GuiElementBuilder customModelData(int cmd) {
        this.json.addProperty("custom_model_data", cmd);
        return this;
    }

    public GuiElementBuilder scale(float scale) {
        this.json.addProperty("scale", scale);
        return this;
    }

    public GuiElementBuilder outline(boolean outline) {
        this.json.addProperty("outline", outline);
        return this;
    }

    public GuiElementBuilder entityData(String entityId, String entityName, int scale) {
        this.json.addProperty("entity_id", entityId);
        if (entityName != null) this.json.addProperty("entity_name", entityName);
        this.json.addProperty("entity_scale", scale);
        return this;
    }

    public GuiElementBuilder textContent(String text, String color, float scale, boolean bold) {
        this.json.addProperty("text", text);
        this.json.addProperty("color", color);
        this.json.addProperty("scale", scale);
        this.json.addProperty("bold", bold);
        return this;
    }

    public GuiElementBuilder textAlign(String align) {
        this.textAlign = align;
        return this;
    }

    public GuiElementBuilder richMessage(JsonArray message, int maxWidth, Integer maxHeight, String color) {
        this.richMessage = message;
        this.json.addProperty("max_width", maxWidth);
        if (maxHeight != null) this.json.addProperty("max_height", maxHeight);
        if (color != null) this.json.addProperty("color", color);
        return this;
    }

    public GuiElementBuilder tooltipLine(String text, String color, boolean bold) {
        JsonObject line = new JsonObject();
        line.addProperty("text", text);
        line.addProperty("color", color);
        line.addProperty("bold", bold);
        this.tooltip.add(line);
        return this;
    }

    public JsonObject build() {
        if (!this.tooltip.isEmpty()) {
            JsonArray arr = new JsonArray();
            this.tooltip.forEach(arr::add);
            this.json.add("tooltip", arr);
        }

        if (this.richMessage != null) {
            this.json.add("message", this.richMessage);
        }

        if (this.json.has("type") && this.json.get("type").getAsString().equals("text")) {
            this.json.addProperty("text_align", this.textAlign);
        }

        return this.json;
    }
}