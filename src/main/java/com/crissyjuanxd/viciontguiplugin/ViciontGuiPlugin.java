package com.crissyjuanxd.viciontguiplugin;

import com.crissyjuanxd.viciontguiplugin.api.PagedMenuManager;
import com.crissyjuanxd.viciontguiplugin.api.ViciontGuiAPI;
import com.crissyjuanxd.viciontguiplugin.network.GuiActionChannel;
import com.crissyjuanxd.viciontguiplugin.network.GuiChannel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ViciontGuiPlugin extends JavaPlugin implements Listener {
    public String Prefix = "§5§lViciont§2§lGUI &7➤ &f";
    public String Version;
    public static boolean shuttingDown = false;
    private static ViciontGuiPlugin instance;
    private PagedMenuManager pagedMenuManager;


    @Override
    public void onEnable() {
        instance = this;
        this.Version = getDescription().getVersion();

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        Prefix + "&aha sido habilitado!, &eVersion: " + Version));


        GuiChannel guiChannel = new GuiChannel(this);
        new GuiActionChannel(this);
        ViciontGuiAPI.init(guiChannel);

        this.pagedMenuManager = new PagedMenuManager();

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ViciontGuiPlugin ha sido activado correctamente.");
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
        HandlerList.unregisterAll((Plugin) this);
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        Prefix + "&aha sido deshabilitado!, &eVersion: " + Version));
        getLogger().info("ViciontGuiPlugin desactivado.");
        shuttingDown = true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ViciontGuiAPI.clearPlayer(event.getPlayer());
        pagedMenuManager.clear(event.getPlayer());
    }

    /** Otros plugins pueden usar esto para reusar el mismo PagedMenuManager en vez de instanciar el suyo. */
    public PagedMenuManager pagedMenus() { return pagedMenuManager; }
}