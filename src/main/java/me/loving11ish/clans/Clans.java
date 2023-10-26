package me.loving11ish.clans;

import com.rylinaux.plugman.api.PlugManAPI;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.lib.PaperLib;
import me.loving11ish.clans.externalhooks.FloodgateAPI;
import me.loving11ish.clans.externalhooks.PlaceholderAPI;
import me.loving11ish.clans.externalhooks.PlugManXAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import me.loving11ish.clans.commands.*;
import me.loving11ish.clans.commands.commandTabCompleters.ChestCommandTabCompleter;
import me.loving11ish.clans.commands.commandTabCompleters.ClanAdminTabCompleter;
import me.loving11ish.clans.commands.commandTabCompleters.ClanCommandTabCompleter;
import me.loving11ish.clans.expansions.PlaceholderAPIClanExpansion;
import me.loving11ish.clans.files.ClanGUIFileManager;
import me.loving11ish.clans.files.ClansFileManager;
import me.loving11ish.clans.files.MessagesFileManager;
import me.loving11ish.clans.files.UsermapFileManager;
import me.loving11ish.clans.listeners.*;
import me.loving11ish.clans.menusystem.PlayerMenuUtility;
import me.loving11ish.clans.menusystem.paginatedMenu.ClanListGUI;
import me.loving11ish.clans.updatesystem.JoinEvent;
import me.loving11ish.clans.updatesystem.UpdateChecker;
import me.loving11ish.clans.utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Clans extends JavaPlugin {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    private FoliaLib foliaLib = new FoliaLib(this);

    private static Clans plugin;
    private static FloodgateApi floodgateApi;
    private static VersionCheckerUtils versionCheckerUtils;
    private static boolean chestsEnabled = false;
    private static boolean GUIEnabled = false;
    private static boolean onlineMode = false;

    public MessagesFileManager messagesFileManager;
    public ClansFileManager clansFileManager;
    public ClanGUIFileManager clanGUIFileManager;
    public UsermapFileManager usermapFileManager;

    public HashMap<UUID, WrappedTask> teleportQueue = new HashMap<>();
    public static HashMap<Player, String> connectedPlayers = new HashMap<>();
    public static HashMap<Player, String> bedrockPlayers = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        //Plugin startup logic
        plugin = this;
        versionCheckerUtils = new VersionCheckerUtils();
        versionCheckerUtils.setVersion();

        //Server version compatibility check
        if (versionCheckerUtils.getVersion() < 13||versionCheckerUtils.getVersion() > 20){
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Your server version is: &d" + Bukkit.getServer().getVersion()));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin is only supported on the Minecraft versions listed below:"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.13.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.14.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.15.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.16.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.17.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.18.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.19.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &41.20.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Is now disabling!"));
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aA supported Minecraft version has been detected"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aYour server version is: &d" + Bukkit.getServer().getVersion()));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
            console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        if (foliaLib.isUnsupported()){
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Your server appears to running a version other than Spigot based!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin uses features that your server most likely doesn't have!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Is now disabling!"));
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //Suggest PaperMC if not using
        if (foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlugManX")||PlugManXAPI.isPlugManXEnabled()){
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("ClansLite")){
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4You appear to be using an unsupported version of &d&lPlugManX"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Please &4&lFULLY RESTART YOUR SERVER!"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4&lNo official support will be given to you if you use this!"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4&lUnless Loving11ish has explicitly agreed to help!"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Please add ClansLite to the ignored-plugins list in PlugManX's config.yml"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aSuccessfully hooked into PlugManX"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aSuccessfully added ClansLite to ignoredPlugins list."));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cPlugManX not found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cDisabling PlugManX hook loader"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &6Continuing plugin startup"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Load the plugin configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        GUIEnabled = getConfig().getBoolean("use-global-GUI-system");
        chestsEnabled = getConfig().getBoolean("protections.chests.enabled");

        //Load messages.yml
        this.messagesFileManager = new MessagesFileManager();
        messagesFileManager.MessagesFileManager(this);

        //Load clangui.yml
        this.clanGUIFileManager = new ClanGUIFileManager();
        clanGUIFileManager.ClanGUIFileManager(this);

        //Load clans.yml
        this.clansFileManager = new ClansFileManager();
        clansFileManager.ClansFileManager(this);
        if (clansFileManager != null){
            if (clansFileManager.getClansConfig().contains("clans.data")){
                try {
                    ClansStorageUtil.restoreClans();
                } catch (IOException e) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from clans.yml!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4See below for errors!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from clans.yml!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //Load usermap.yml
        this.usermapFileManager = new UsermapFileManager();
        usermapFileManager.UsermapFileManager(this);
        if (usermapFileManager != null){
            if (usermapFileManager.getUsermapConfig().contains("users.data")){
                try {
                    UsermapStorageUtil.restoreUsermap();
                } catch (IOException e) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from usermap.yml!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4See below for errors!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to load data from usermap.yml!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Disabling Plugin!"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //Register the plugin commands
        this.getCommand("clan").setExecutor(new ClanCommand());
        this.getCommand("clanadmin").setExecutor(new ClanAdmin());
        this.getCommand("clanchat").setExecutor(new ClanChatCommand());
        this.getCommand("clanchatspy").setExecutor(new ClanChatSpyCommand());
        this.getCommand("clanchest").setExecutor(new ClanChestCommand());

        //Register the command tab completers
        this.getCommand("clan").setTabCompleter(new ClanCommandTabCompleter());
        this.getCommand("clanchest").setTabCompleter(new ChestCommandTabCompleter());
        this.getCommand("clanadmin").setTabCompleter(new ClanAdminTabCompleter());

        //Register the plugin events
        this.getServer().getPluginManager().registerEvents(new PlayerPreConnectionEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDisconnectEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMovementEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMessageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        if (versionCheckerUtils.getVersion() >= 14) {
            this.getServer().getPluginManager().registerEvents(new ChestBreakEvent(), this);
            this.getServer().getPluginManager().registerEvents(new ChestOpenEvent(), this);
            this.getServer().getPluginManager().registerEvents(new MenuEvent(), this);
            if (GUIEnabled){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Global GUI system enabled!"));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
            }
            if (chestsEnabled){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Chest protection system enabled!"));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cYour current server version does not support PersistentDataContainers!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
        }

        //Update banned tags list
        ClanCommand.updateBannedTagsList();

        //Register PlaceHolderAPI hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")||PlaceholderAPI.isPlaceholderAPIEnabled()){
            new PlaceholderAPIClanExpansion().register();
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3PlaceholderAPI found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3External placeholders enabled!"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cPlaceholderAPI not found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cExternal placeholders disabled!"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Register FloodgateApi hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate")||FloodgateAPI.isFloodgateEnabled()){
            floodgateApi = FloodgateApi.getInstance();
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3FloodgateApi found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Full Bedrock support enabled!"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3FloodgateApi not found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Bedrock support may not function!"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Plugin startup message
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin by: &b&lLoving11ish"));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3has been loaded successfully"));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aDeveloper debug mode enabled!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aThis WILL fill the console"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &awith additional ClansLite information!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aThis setting is not intended for "));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &acontinous use!"));
        }
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));

        //Check for available updates
        new UpdateChecker(97163).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.1")));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.2")));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("no-update-available.3")));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.1")));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.2")));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("update-available.3")));
            }
        });

        //Start auto save task
        foliaLib.getImpl().runLaterAsync(new Runnable() {
            @Override
            public void run() {
                TaskTimerUtils.runClansAutoSave();
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-save-started")));
            }
        }, 5L, TimeUnit.SECONDS);

        //Start auto invite clear task
        foliaLib.getImpl().runLaterAsync(new Runnable() {
            @Override
            public void run() {
                TaskTimerUtils.runClanInviteClear();
                console.sendMessage(ColorUtils.translateColorCodes(messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-started")));
            }
        }, 5L, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic

        //Unregister plugin listeners
        HandlerList.unregisterAll(this);

        //Safely stop the background tasks if running
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin by: &b&lLoving11ish"));
        try {
            if (!teleportQueue.isEmpty()){
                for (Map.Entry<UUID, WrappedTask> wrappedTaskEntry: teleportQueue.entrySet()){
                    WrappedTask wrappedTask = wrappedTaskEntry.getValue();
                    wrappedTask.cancel();
                    if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + wrappedTask.toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed teleport task canceled successfully"));
                    }
                    teleportQueue.remove(wrappedTaskEntry.getKey());
                }
            }
            if (!TaskTimerUtils.autoSaveTask.isCancelled()){
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + TaskTimerUtils.autoSaveTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task 1 canceled successfully"));
                }
                TaskTimerUtils.autoSaveTask.cancel();
            }
            if (!TaskTimerUtils.inviteClearTask.isCancelled()){
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + TaskTimerUtils.inviteClearTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task 2 canceled successfully"));
                }
                TaskTimerUtils.inviteClearTask.cancel();
            }
            if (!ClanListGUI.autoGUIRefreshTask.isCancelled()){
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + ClanListGUI.autoGUIRefreshTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task 5 canceled successfully"));
                }
                ClanListGUI.autoGUIRefreshTask.cancel();
            }
            if (foliaLib.isUnsupported()){
                Bukkit.getScheduler().cancelTasks(this);
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aBukkit scheduler tasks canceled successfully"));
                }
            }
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Background tasks have disabled successfully!"));
        }catch (Exception e){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Background tasks have disabled successfully!"));
        }

        //Save clansList HashMap to clans.yml
        if (clansFileManager != null){
            if (!ClansStorageUtil.getRawClansList().isEmpty()){
                try {
                    ClansStorageUtil.saveClans();
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3All clans saved to clans.yml successfully!"));
                } catch (IOException e) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save clans to clans.yml!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4See below error for reason!"));
                    e.printStackTrace();
                }
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save clans to clans.yml!"));
        }

        //Saver usermap to usermap.yml
        if (usermapFileManager != null){
            if (!UsermapStorageUtil.getRawUsermapList().isEmpty()){
                try {
                    UsermapStorageUtil.saveUsermap();
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3All users saved to usermap.yml successfully!"));
                } catch (IOException e) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save usermap to usermap.yml!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4See below error for reason!"));
                    e.printStackTrace();
                }
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save usermap to usermap.yml!"));
        }

        //Final plugin shutdown message
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Has been shutdown successfully"));
        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Goodbye!"));
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));

        plugin = null;
        floodgateApi = null;
        versionCheckerUtils = null;
        messagesFileManager = null;
        clansFileManager = null;
        clanGUIFileManager = null;
        usermapFileManager = null;
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

    public static VersionCheckerUtils getVersionCheckerUtils() {
        return versionCheckerUtils;
    }

    public static boolean isChestsEnabled() {
        return chestsEnabled;
    }

    public static void setChestsEnabled(boolean chestsEnabled) {
        Clans.chestsEnabled = chestsEnabled;
    }

    public static boolean isGUIEnabled() {
        return GUIEnabled;
    }

    public static void setGUIEnabled(boolean GUIEnabled) {
        Clans.GUIEnabled = GUIEnabled;
    }

    public static boolean isOnlineMode() {
        return onlineMode;
    }

    public static void setOnlineMode(boolean onlineMode) {
        Clans.onlineMode = onlineMode;
    }
}
