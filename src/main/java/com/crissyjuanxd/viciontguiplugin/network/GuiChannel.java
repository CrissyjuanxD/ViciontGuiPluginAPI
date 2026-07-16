package com.crissyjuanxd.viciontguiplugin.network;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;

public final class GuiChannel {

    public static final String CHANNEL = "viciontguis:gui";

    // Debe coincidir en orden con GuiPayload.Action del mod (OPEN, UPDATE, CLOSE, DELETE)
    public enum Action { OPEN, UPDATE, CLOSE, DELETE }

    private final Plugin plugin;

    public GuiChannel(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
    }

    public void send(Player player, Action action, String guiId, String json) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(action.ordinal());
        ByteBufUtil.writeString(out, guiId);
        ByteBufUtil.writeString(out, json == null ? "" : json);
        player.sendPluginMessage(plugin, CHANNEL, out.toByteArray());
    }

    public void open(Player player, String guiId, String json)   { send(player, Action.OPEN, guiId, json); }
    public void update(Player player, String guiId, String json) { send(player, Action.UPDATE, guiId, json); }
    public void close(Player player, String guiId)                { send(player, Action.CLOSE, guiId, ""); }
    public void delete(Player player, String guiId)                { send(player, Action.DELETE, guiId, ""); }
}