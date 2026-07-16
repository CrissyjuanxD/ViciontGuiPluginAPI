package com.crissyjuanxd.viciontguiplugin.api;

import org.bukkit.entity.Player;

import java.util.List;

/** Implementa esto en TU plugin (misiones, recetas, lo que sea) para reusar la paginación genérica. */
public interface PagedContentProvider {
    int getPageCount(Player player);
    List<GuiElementBuilder> buildPageElements(Player player, int page);
    void applyBackground(GuiBuilder builder);
}