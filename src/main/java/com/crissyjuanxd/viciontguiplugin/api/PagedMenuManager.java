package com.crissyjuanxd.viciontguiplugin.api;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Resuelve prev_page/next_page para cualquier PagedContentProvider — regístralo una vez por proveedor en tu plugin. */
public final class PagedMenuManager {

    private record Session(String guiId, PagedContentProvider provider, int page) {}

    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    public PagedMenuManager() {
        ViciontGuiAPI.onAction("prev_page", (p, guiId, action) -> changePage(p, -1));
        ViciontGuiAPI.onAction("next_page", (p, guiId, action) -> changePage(p, 1));
    }

    public void open(Player player, String guiId, PagedContentProvider provider) {
        sessions.put(player.getUniqueId(), new Session(guiId, provider, 0));
        send(player, true);
    }

    /** Re-renderiza la página actual solo si ESE jugador tiene esta gui abierta ahora mismo (para updates en vivo). */
    public void rerenderIfOpen(Player player, String guiId) {
        Session s = sessions.get(player.getUniqueId());
        if (s != null && s.guiId().equals(guiId)) send(player, false);
    }

    private void changePage(Player player, int delta) {
        Session s = sessions.get(player.getUniqueId());
        if (s == null) return;
        int total = s.provider().getPageCount(player);
        int next = Math.max(0, Math.min(total - 1, s.page() + delta));
        sessions.put(player.getUniqueId(), new Session(s.guiId(), s.provider(), next));
        send(player, false);
    }

    private void send(Player player, boolean initialOpen) {
        Session s = sessions.get(player.getUniqueId());
        if (s == null) return;

        GuiBuilder builder = GuiBuilder.create(s.guiId());
        s.provider().applyBackground(builder);

        int total = s.provider().getPageCount(player);
        if (total > 1) {
            if (s.page() > 0) {
                builder.element(GuiElementBuilder.button("btn_prev", "viciontguis:textures/gui/flecha_izq.png", "prev_page")
                        .position(-49, 122).size(35, 36));
            }
            if (s.page() < total - 1) {
                builder.element(GuiElementBuilder.button("btn_next", "viciontguis:textures/gui/flecha_der.png", "next_page")
                        .position(49, 122).size(35, 36));
            }
        }
        for (GuiElementBuilder el : s.provider().buildPageElements(player, s.page())) {
            builder.element(el);
        }

        if (initialOpen) ViciontGuiAPI.openScreen(player, builder);
        else ViciontGuiAPI.updateScreen(player, builder);
    }

    public void clear(Player player) { sessions.remove(player.getUniqueId()); }
}