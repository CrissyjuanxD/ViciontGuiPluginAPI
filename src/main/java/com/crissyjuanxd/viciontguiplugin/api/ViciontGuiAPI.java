package com.crissyjuanxd.viciontguiplugin.api;

import com.crissyjuanxd.viciontguiplugin.network.GuiChannel;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ViciontGuiAPI {

    private static GuiChannel channel;

    private static final Map<String, GuiActionListener> globalListeners = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<String, GuiActionListener>> sessionListeners = new ConcurrentHashMap<>();
    // Para acciones con id dinámico, ej: "view_item_<id>", "toggle_<id>"
    private static final List<Map.Entry<String, GuiActionListener>> prefixListeners = new CopyOnWriteArrayList<>();

    private ViciontGuiAPI() {}

    public static void init(GuiChannel ch) { channel = ch; }

    // ---------- Enviar GUIs ----------

    /** Abre una pantalla normal (equivalente a target=screen en el JSON). */
    public static void openScreen(Player player, GuiBuilder gui) {
        channel.open(player, gui.guiId(), gui.buildJson());
    }

    /** Actualiza una pantalla ya abierta con datos nuevos (sin cerrar/reabrir del lado del jugador). */
    public static void updateScreen(Player player, GuiBuilder gui) {
        channel.update(player, gui.guiId(), gui.buildJson());
    }

    public static void closeScreen(Player player, String guiId) {
        channel.close(player, guiId);
    }

    /** Agrega o actualiza un overlay flotante (target=hud o target=inventory). */
    public static void setOverlay(Player player, GuiBuilder gui) {
        channel.open(player, gui.guiId(), gui.buildJson());
    }

    public static void removeOverlay(Player player, String guiId) {
        channel.delete(player, guiId);
    }

    // ---------- Escuchar clicks ----------

    /** Se ejecuta para cualquier jugador que dispare esta acción exacta, en cualquier gui. */
    public static void onAction(String action, GuiActionListener listener) {
        globalListeners.put(action, listener);
    }

    /** Igual que onAction pero por prefijo, para acciones con id dinámico ("view_algo_123"). */
    public static void onActionPrefix(String prefix, GuiActionListener listener) {
        prefixListeners.add(Map.entry(prefix, listener));
    }

    /** Se ejecuta UNA sola vez, solo para el próximo click de ESE jugador con esa acción exacta. */
    public static void onNextAction(Player player, String action, GuiActionListener listener) {
        sessionListeners.computeIfAbsent(player.getUniqueId(), id -> new ConcurrentHashMap<>()).put(action, listener);
    }

    // Llamado internamente por GuiActionChannel cuando llega un click real del cliente
    public static void dispatch(Player player, String guiId, String action) {
        Map<String, GuiActionListener> perPlayer = sessionListeners.get(player.getUniqueId());
        if (perPlayer != null) {
            GuiActionListener oneShot = perPlayer.remove(action);
            if (oneShot != null) { oneShot.onAction(player, guiId, action); return; }
        }

        GuiActionListener global = globalListeners.get(action);
        if (global != null) { global.onAction(player, guiId, action); return; }

        for (Map.Entry<String, GuiActionListener> entry : prefixListeners) {
            if (action.startsWith(entry.getKey())) {
                entry.getValue().onAction(player, guiId, action);
                return;
            }
        }
    }

    public static void clearPlayer(Player player) {
        sessionListeners.remove(player.getUniqueId());
    }
}