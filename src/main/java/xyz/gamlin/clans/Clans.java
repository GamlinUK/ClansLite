package xyz.gamlin.clans;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
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

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    Logger logger = this.getLogger();

    private static Clans plugin;

    @Override
    public void onEnable() {
        //Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")||
                Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")||
                Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18"))){
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin is only supported on the Minecraft versions listed below:"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.13.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.14.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.15.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.16.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.17.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.18.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4Is now disabling!"));
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(plugin);
        }else {
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &aA supported Minecraft version has been detected"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        //Load plugin metrics
        int pluginId = 13076;
        Metrics metrics = new Metrics(this, pluginId);

        //Load the plugin configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            ClansStorageUtil.loadClans();
        } catch (IOException e) {
            logger.severe("&6ClansLite: &4Failed to load clans.json!");
            logger.severe("&6ClansLite: &4Disabling plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        //Register the plugin commands
        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());

        //Register the plugin events
        this.getServer().getPluginManager().registerEvents(new ClanChat(), this);

        //Plugin startup message
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin created by: &b&lGamlin"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin contributors: &b&lLoving11ish"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3has been loaded successfully"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));

        //Start auto save task
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
        //Plugin shutdown logic

        //Safely stop the auto save tasks if running
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID1)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID1)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID2)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID2)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID2);
            }
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin created by: &b&lGamlin"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin contributors: &b&lLoving11ish"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Background tasks have disabled successfully!"));
        }
        try {
            ClansStorageUtil.saveClans();
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3clans.json has been successfully saved!"));
        }catch (IOException e){
            logger.severe("&6ClansLite: &4Failed to save clans.json!");
            logger.severe("&6ClansLite: &4See below error for details!");
            e.printStackTrace();
        }
        //Final plugin shutdown message
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Has been shutdown successfully"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Goodbye!"));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    public static Clans getPlugin() {
        return plugin;
    }
}
