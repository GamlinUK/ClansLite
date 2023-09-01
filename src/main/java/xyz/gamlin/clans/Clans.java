package xyz.gamlin.clans;

import com.rylinaux.plugman.api.PlugManAPI;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.gamlin.clans.commands.*;
import xyz.gamlin.clans.commands.commandTabCompleters.ChestCommandTabCompleter;
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
import xyz.gamlin.clans.utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Clans extends JavaPlugin {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final PluginDescriptionFile pluginsendMessage = getDescription();
    private final String pluginVersion = pluginsendMessage.getVersion();
    private FoliaLib foliaLib = new FoliaLib(this);

    private static Clans plugin;
    private static FloodgateApi floodgateApi;
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

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")||
                Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")||
                Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18")||
                Bukkit.getServer().getVersion().contains("1.19")||Bukkit.getServer().getVersion().contains("1.20"))){
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

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported()||foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlugManX")||isPlugManXEnabled()){
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
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDisconnectEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMovementEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMessageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChestBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChestOpenEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new MenuEvent(), this);

        //Update banned tags list
        ClanCommand.updateBannedTagsList();

        //Register PlaceHolderAPI hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")||isPlaceholderAPIEnabled()){
            new PlayerClanExpansion().register();
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
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate")||isFloodgateEnabled()){
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
            if (!ClanListGUI.task5.isCancelled()){
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + ClanListGUI.task5.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task 5 canceled successfully"));
                }
                ClanListGUI.task5.cancel();
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

    public boolean isFloodgateEnabled() {
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound FloodgateApi class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"));
            }
            return true;
        } catch (ClassNotFoundException e) {
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find FloodgateApi class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"));
            }
            return false;
        }
    }

    public boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound PlugManX main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dcom.rylinaux.plugman.PlugMan"));
            }
            return true;
        }catch (ClassNotFoundException e){
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find PlugManX main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dcom.rylinaux.plugman.PlugMan"));
            }
            return false;
        }
    }

    public boolean isPlaceholderAPIEnabled() {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin");
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound PlaceholderAPI main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"));
            }
            return true;
        }catch (ClassNotFoundException e){
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find PlaceholderAPI main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"));
            }
            return false;
        }
    }

    public static Clans getPlugin() {
        return plugin;
    }

    public static FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }
}
