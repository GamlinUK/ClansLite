package xyz.gamlin.clans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.gamlin.clans.commands.ClanAdmin;
import xyz.gamlin.clans.commands.ClanChatCommand;
import xyz.gamlin.clans.commands.ClanCommand;
import xyz.gamlin.clans.commands.commandTabCompleters.ClanAdminTabCompleter;
import xyz.gamlin.clans.commands.commandTabCompleters.ClanCommandTabCompleter;
import xyz.gamlin.clans.expansions.PlayerClanExpansion;
import xyz.gamlin.clans.files.ClanGUIFileManager;
import xyz.gamlin.clans.files.ClansFileManager;
import xyz.gamlin.clans.files.MessagesFileManager;
import xyz.gamlin.clans.files.UsermapFileManager;
import xyz.gamlin.clans.listeners.*;
import xyz.gamlin.clans.menuSystem.PlayerMenuUtility;
import xyz.gamlin.clans.menuSystem.paginatedMenu.ClanListGUI;
import xyz.gamlin.clans.updateSystem.JoinEvent;
import xyz.gamlin.clans.updateSystem.UpdateChecker;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.TaskTimerUtils;
import xyz.gamlin.clans.utils.UsermapStorageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public final class Clans extends JavaPlugin {

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    Logger logger = this.getLogger();

    private static Clans plugin;
    private static FloodgateApi floodgateApi;
    public MessagesFileManager messagesFileManager;
    public ClansFileManager clansFileManager;
    public ClanGUIFileManager clanGUIFileManager;
    public UsermapFileManager usermapFileManager;

    public static HashMap<Player, String> connectedPlayers = new HashMap<>();
    public static HashMap<Player, String> bedrockPlayers = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

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
            return;
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

        //Load clangui.yml
        this.clanGUIFileManager = new ClanGUIFileManager();
        clanGUIFileManager.ClanGUIFileManager(this);

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
                return;
            }
        }

        //Load usermap.yml
        this.usermapFileManager = new UsermapFileManager();
        usermapFileManager.UsermapFileManager(this);
        if (usermapFileManager.getUsermapConfig().contains("users.data")){
            try {
                UsermapStorageUtil.restoreUsermap();
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from usermap.yml!"));
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4See below for errors!"));
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        //Register the plugin commands
        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());
        this.getCommand("cc").setExecutor(new ClanChatCommand());

        //Register the command tab completers
        this.getCommand("clan").setTabCompleter(new ClanCommandTabCompleter());
        this.getCommand("clanadmin").setTabCompleter(new ClanAdminTabCompleter());

        //Register the plugin events
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDisconnectEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMessageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new MenuEvent(), this);

        //Update banned tags list
        ClanCommand.updateBannedTagsList();

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

        //Register FloodgateApi hooks
        if (Bukkit.getPluginManager().getPlugin("floodgate") != null){
            floodgateApi = FloodgateApi.getInstance();
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3FloodgateApi found!"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Full Bedrock support enabled!"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3FloodgateApi not found!"));
            logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Bedrock support may not function!"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Plugin startup message
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin by: &b&lLoving11ish"));
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
        if (getConfig().getBoolean("general.run-auto-save-task.enabled")){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    TaskTimerUtils.runClansAutoSaveOne();
                    logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-save-started")));
                }
            },100L);
        }

        //Start auto invite clear task
        if (getConfig().getBoolean("general.run-auto-invite-wipe-task.enabled")){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    TaskTimerUtils.runClanInviteClearOne();
                    logger.info(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-started")));
                }
            },100L);
        }
    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic

        //Safely stop the background tasks if running
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin by: &b&lLoving11ish"));
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
            if (Bukkit.getScheduler().isCurrentlyRunning(ClanListGUI.taskID5)||Bukkit.getScheduler().isQueued(ClanListGUI.taskID5)){
                Bukkit.getScheduler().cancelTask(ClanListGUI.taskID5);
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

        //Saver usermap to usermap.yml
        if (!UsermapStorageUtil.getRawUsermapList().isEmpty()){
            try {
                UsermapStorageUtil.saveUsermap();
                logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3All users saved to usermap.yml successfully!"));
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save usermap to usermap.yml!"));
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

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(player))) {
            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);
            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(player);
        }
    }

    public static Clans getPlugin() {
        return plugin;
    }

    public static FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }
}
