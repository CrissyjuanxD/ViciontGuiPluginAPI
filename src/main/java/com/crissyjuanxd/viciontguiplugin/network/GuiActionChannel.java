package com.crissyjuanxd.viciontguiplugin.network;

import com.crissyjuanxd.viciontguiplugin.api.ViciontGuiAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public final class GuiActionChannel implements PluginMessageListener {

    public static final String CHANNEL = "viciontguis:action";

    public GuiActionChannel(Plugin plugin) {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL, this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!CHANNEL.equals(channel)) return;
        int[] cursor = { 0 };
        String guiId = ByteBufUtil.readString(message, cursor);
        String action = ByteBufUtil.readString(message, cursor);
        ViciontGuiAPI.dispatch(player, guiId, action);
    }
}