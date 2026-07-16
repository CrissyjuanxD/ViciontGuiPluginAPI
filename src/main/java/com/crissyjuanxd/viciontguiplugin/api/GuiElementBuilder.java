package com.crissyjuanxd.viciontguiplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

// Construye un elemento individual: custom_button, item_slot, text, rich_text, entity, image, invisible_button
public final class GuiElementBuilder {
    private final JsonObject json = new JsonObject();
    private final List<JsonObject> tooltip = new ArrayList<>();
    private JsonArray richMessage;

    private GuiElementBuilder() {}

    public static GuiElementBuilder create(String id, String type) {
        GuiElementBuilder b = new GuiElementBuilder();
        b.json.addProperty("id", id);
        b.json.addProperty("type", type);
        return b;
    }

    public static GuiElementBuilder button(String id, String texture, String action) {
        GuiElementBuilder b = create(id, "custom_button").texture(texture);
        if (action != null) b.action(action); // sin esto, "action": null rompía el parseo en el cliente
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

    public GuiElementBuilder anchor(String anchor) { json.addProperty("anchor", anchor); return this; }
    public GuiElementBuilder position(int x, int y) { json.addProperty("x", x); json.addProperty("y", y); return this; }
    public GuiElementBuilder size(int width, int height) { json.addProperty("width", width); json.addProperty("height", height); return this; }
    public GuiElementBuilder textureSize(int texWidth, int texHeight) { json.addProperty("texture_width", texWidth); json.addProperty("texture_height", texHeight); return this; }
    public GuiElementBuilder texture(String texture) { json.addProperty("texture", texture); return this; }
    public GuiElementBuilder action(String action) { json.addProperty("action", action); return this; }
    public GuiElementBuilder itemId(String itemId) { json.addProperty("item_id", itemId); return this; }
    public GuiElementBuilder customModelData(int cmd) { json.addProperty("custom_model_data", cmd); return this; }

    public GuiElementBuilder scale(float scale) {
        json.addProperty("scale", scale);
        return this;
    }

    public GuiElementBuilder outline(boolean outline) {
        json.addProperty("outline", outline);
        return this;
    }

    public GuiElementBuilder entityData(String entityId, String entityName, int scale) {
        json.addProperty("entity_id", entityId);
        if (entityName != null) json.addProperty("entity_name", entityName);
        json.addProperty("entity_scale", scale);
        return this;
    }

    public GuiElementBuilder textContent(String text, String color, float scale, boolean bold) {
        json.addProperty("text", text);
        json.addProperty("color", color);
        json.addProperty("scale", scale);
        json.addProperty("bold", bold);
        return this;
    }

    /** message: JsonArray con formato de JSON text component de vanilla (igual que un tellraw). */
    public GuiElementBuilder richMessage(JsonArray message, int maxWidth, Integer maxHeight, String color) {
        this.richMessage = message;
        json.addProperty("max_width", maxWidth);
        if (maxHeight != null) json.addProperty("max_height", maxHeight);
        if (color != null) json.addProperty("color", color);
        return this;
    }

    public GuiElementBuilder tooltipLine(String text, String color, boolean bold) {
        JsonObject line = new JsonObject();
        line.addProperty("text", text);
        line.addProperty("color", color);
        line.addProperty("bold", bold);
        tooltip.add(line);
        return this;
    }

    public JsonObject build() {
        if (!tooltip.isEmpty()) {
            JsonArray arr = new JsonArray();
            tooltip.forEach(arr::add);
            json.add("tooltip", arr);
        }
        if (richMessage != null) {
            json.add("message", richMessage);
        }
        return json;
    }
}