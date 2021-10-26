package xyz.gamlin.clans;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;
import xyz.gamlin.clans.commands.ClanAdmin;
import xyz.gamlin.clans.commands.ClanCommand;
import xyz.gamlin.clans.listeners.ClanChat;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;

import java.io.IOException;

public final class Clans extends JavaPlugin {

    private static Clans plugin;

    public static Clans getPlugin() {
        return plugin;
    }

    FileConfiguration config = getConfig();

    public FileConfiguration getClansConfig() { return config; }

    @Override
    public void onEnable() {

        int pluginId = 13076;
        Metrics metrics = new Metrics(this, pluginId);

        plugin = this;

        config.options().copyDefaults();
        saveConfig();


        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());

        try {
            ClansStorageUtil.loadClans();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(new ClanChat(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
