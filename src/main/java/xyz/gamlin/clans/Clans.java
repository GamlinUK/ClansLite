package xyz.gamlin.clans;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;
import xyz.gamlin.clans.commands.ClanAdmin;
import xyz.gamlin.clans.commands.ClanCommand;
import xyz.gamlin.clans.listeners.ClanChat;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.TaskTimerUtils;

import java.io.IOException;
import java.util.logging.Logger;

public final class Clans extends JavaPlugin {

    Logger logger = this.getLogger();

    private static Clans plugin;

    @Override
    public void onEnable() {

        int pluginId = 13076;
        Metrics metrics = new Metrics(this, pluginId);

        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();


        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());

        try {
            ClansStorageUtil.loadClans();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(new ClanChat(), this);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TaskTimerUtils.runClansAutoSaveOne();
                logger.info(ColorUtils.translateColorCodes("&6ClansLite: &aAuto save task has started."));
            }
        },100L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Clans getPlugin() {
        return plugin;
    }
}
