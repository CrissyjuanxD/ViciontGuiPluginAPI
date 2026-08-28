package com.crissyjuanxd.viciontguiplugin.api;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public final class PagedMenuManager {
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    public PagedMenuManager() {
        ViciontGuiAPI.onAction("prev_page", (p, guiId, action) -> this.changePage(p, -1));
        ViciontGuiAPI.onAction("next_page", (p, guiId, action) -> this.changePage(p, 1));
    }

    // Abre el menú siempre desde la página 0 (cuando abres desde el menú principal)
    public void open(Player player, String guiId, PagedContentProvider provider) {
        this.sessions.put(player.getUniqueId(), new Session(guiId, provider, 0));
        this.send(player, true);
    }

    // Retoma el menú en la página guardada (cuando regresas de leer un cambio o ver una receta)
    public void resume(Player player, String guiId, PagedContentProvider provider) {
        Session existing = this.sessions.get(player.getUniqueId());
        int startingPage = 0;

        if (existing != null && existing.guiId().equals(guiId)) {
            startingPage = existing.page();
        }

        this.sessions.put(player.getUniqueId(), new Session(guiId, provider, startingPage));
        this.send(player, true);
    }

    public void rerenderIfOpen(Player player, String guiId) {
        Session s = this.sessions.get(player.getUniqueId());
        if (s != null && s.guiId().equals(guiId)) {
            this.send(player, false);
        }
    }

    private void changePage(Player player, int delta) {
        Session s = this.sessions.get(player.getUniqueId());
        if (s != null) {
            int total = s.provider().getPageCount(player);
            int next = Math.max(0, Math.min(total - 1, s.page() + delta));
            this.sessions.put(player.getUniqueId(), new Session(s.guiId(), s.provider(), next));
            this.send(player, false);
        }
    }

    private void send(Player player, boolean initialOpen) {
        Session s = this.sessions.get(player.getUniqueId());
        if (s != null) {
            GuiBuilder builder = GuiBuilder.create(s.guiId());

            s.provider().applyBackground(builder, player, s.page());

            for(GuiElementBuilder el : s.provider().buildPageElements(player, s.page())) {
                builder.element(el);
            }

            if (initialOpen) {
                ViciontGuiAPI.openScreen(player, builder);
            } else {
                ViciontGuiAPI.updateScreen(player, builder);
            }
        }
    }

    public void clear(Player player) {
        this.sessions.remove(player.getUniqueId());
    }

    private record Session(String guiId, PagedContentProvider provider, int page) {}
}