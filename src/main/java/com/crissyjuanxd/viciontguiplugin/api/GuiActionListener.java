package com.crissyjuanxd.viciontguiplugin.api;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface GuiActionListener {
    void onAction(Player player, String guiId, String action);
}