package xyz.gamlin.clans;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.gamlin.clans.commands.ClanAdmin;
import xyz.gamlin.clans.commands.ClanCommand;
import xyz.gamlin.clans.expansions.PlayerClanExpansion;
import xyz.gamlin.clans.files.ClansFileManager;
import xyz.gamlin.clans.files.MessagesFileManager;
import xyz.gamlin.clans.listeners.ClanChat;
import xyz.gamlin.clans.listeners.PlayerDamage;
import xyz.gamlin.clans.updateSystem.JoinEvent;
import xyz.gamlin.clans.updateSystem.UpdateChecker;
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
    public MessagesFileManager messagesFileManager;
    public ClansFileManager clansFileManager;

    @Override
    public void onEnable() {
        //Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")||
                Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")||
                Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18")||
                Bukkit.getServer().getVersion().contains("1.19"))){
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin is only supported on the Minecraft versions listed below:"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.13.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.14.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.15.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.16.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.17.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.18.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &41.19.x"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4Is now disabling!"));
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
        }else {
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &aA supported Minecraft version has been detected"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        //Load the plugin configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Load messages.yml
        this.messagesFileManager = new MessagesFileManager();
        messagesFileManager.MessagesFileManager(this);

        //Load clans.yml
        this.clansFileManager = new ClansFileManager();
        clansFileManager.ClansFileManager(this);
        if (clansFileManager.getClansConfig().contains("clans.data")){
            try {
                ClansStorageUtil.restoreClans();
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from clans.yml!"));
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4See below for errors!"));
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        //Register the plugin commands
        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());

        //Register the plugin events
        this.getServer().getPluginManager().registerEvents(new ClanChat(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        //Register PlaceHolderAPI hooks
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlayerClanExpansion().register();
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3PlaceholderAPI found!"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3External placeholders enabled!"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }else {
            logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &cPlaceholderAPI not found!"));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &cExternal placeholders disabled!"));
            logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Plugin startup message
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin created by: &b&lGamlin"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin maintained by: &b&lLoving11ish"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3has been loaded successfully"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));

        //Check for available updates
        new UpdateChecker(this, 97163).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.1")));
                logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.2")));
                logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.3")));
            }else {
                logger.warning(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.1")));
                logger.warning(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.2")));
                logger.warning(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.3")));
            }
        });

        //Start auto save task
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TaskTimerUtils.runClansAutoSaveOne();
                logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-save-started")));
            }
        },100L);

        //Start auto invite clear task
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TaskTimerUtils.runClanInviteClearOne();
                logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-started")));
            }
        },100L);
    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic

        //Safely stop the background tasks if running
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin created by: &b&lGamlin"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin maintained by: &b&lLoving11ish"));
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID1)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID1)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID2)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID2)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID2);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID3)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID3)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID3);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID4)||Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID4)){
                Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID4);
            }
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Background tasks have disabled successfully!"));
        }

        //Save clansList HashMap to clans.yml
        if (!ClansStorageUtil.getRawClansList().isEmpty()){
            try {
                ClansStorageUtil.saveClans();
                logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3All clans saved to clans.yml successfully!"));
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save clans to clans.yml!"));
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4See below error for reason!"));
                e.printStackTrace();
            }
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
