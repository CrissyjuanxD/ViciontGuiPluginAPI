package com.crissyjuanxd.viciontguiplugin.api;

import java.util.List;
import org.bukkit.entity.Player;

public interface PagedContentProvider {
    int getPageCount(Player player);

    List<GuiElementBuilder> buildPageElements(Player player, int page);

    // NUEVO: Ahora recibe Player y la página
    void applyBackground(GuiBuilder builder, Player player, int page);
}