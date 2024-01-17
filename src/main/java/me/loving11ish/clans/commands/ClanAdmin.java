package me.loving11ish.clans.commands;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClanAdmin implements CommandExecutor {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
    private static final String PLAYER_TO_KICK = "%KICKEDPLAYER%";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        if (!ClansStorageUtil.getRawClansList().isEmpty()){
                            ClansStorageUtil.saveClans();
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-clans")));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                    }
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")));
                    for (Player p : onlinePlayers){
                        if (p.getName().equalsIgnoreCase(player.getName())){
                            continue;
                        }
                        if (!onlinePlayers.isEmpty()){
                            p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-broadcast-start")));
                        }
                    }
                    FoliaLib foliaLib = Clans.getFoliaLib();
                    Clans.getPlugin().onDisable();
                    foliaLib.getImpl().runLater(() ->
                            Bukkit.getPluginManager().getPlugin("ClansLite").onEnable(), 5L, TimeUnit.SECONDS);
                    foliaLib.getImpl().runLater(() -> {
                        Clans.getPlugin().reloadConfig();
                        ClanCommand.updateBannedTagsList();
                        Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                        Clans.getPlugin().clanGUIFileManager.reloadClanGUIConfig();
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                        for (Player p : onlinePlayers){
                            if (p.getName().equalsIgnoreCase(player.getName())){
                                continue;
                            }
                            if (!onlinePlayers.isEmpty()){
                                p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                            }
                        }
                    }, 5L, TimeUnit.SECONDS);
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("disband")) {
                    if (args.length == 2){
                        if (args[1].length() > 1){
                            Player onlinePlayerOwner = Bukkit.getPlayer(args[1]);
                            OfflinePlayer offlinePlayerOwner = UsermapStorageUtil.getBukkitOfflinePlayerByName(args[1]);
                            if (onlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteClan(onlinePlayerOwner)){
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else if (offlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteOfflineClan(offlinePlayerOwner)){
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player").replace(PLAYER_TO_KICK, args[1])));
                            }
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")));
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    player.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&nClansLite&r &3~~~~~~~~~~"));
                    player.sendMessage(ColorUtils.translateColorCodes("&3Version: &6" + Clans.getPlugin().getDescription().getVersion()));
                    player.sendMessage(ColorUtils.translateColorCodes("&3Authors: &6" + Clans.getPlugin().getDescription().getAuthors()));
                    player.sendMessage(ColorUtils.translateColorCodes("&3Description: &6" + Clans.getPlugin().getDescription().getDescription()));
                    player.sendMessage(ColorUtils.translateColorCodes("&3Website: "));
                    player.sendMessage(ColorUtils.translateColorCodes("&6" + Clans.getPlugin().getDescription().getWebsite()));
                    player.sendMessage(ColorUtils.translateColorCodes("&3Discord:"));
                    player.sendMessage(ColorUtils.translateColorCodes("&6https://discord.gg/crapticraft"));
                    player.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&nClansLite&r &3~~~~~~~~~~"));
                }

//----------------------------------------------------------------------------------------------------------------------
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(sendUsageMessage()));
            }
        }

//----------------------------------------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("save")) {
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        if (!ClansStorageUtil.getRawClansList().isEmpty()){
                            ClansStorageUtil.saveClans();
                        }else {
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-clans")));
                        }
                    } catch (IOException e) {
                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                        e.printStackTrace();
                    }
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")));
                    for (Player p : onlinePlayers){
                        if (!onlinePlayers.isEmpty()){
                            p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-broadcast-start")));
                        }
                    }
                    FoliaLib foliaLib = Clans.getFoliaLib();
                    Clans.getPlugin().onDisable();
                    foliaLib.getImpl().runLater(() ->
                            Bukkit.getPluginManager().getPlugin("ClansLite").onEnable(), 5L, TimeUnit.SECONDS);
                    foliaLib.getImpl().runLater(() -> {
                        Clans.getPlugin().reloadConfig();
                        ClanCommand.updateBannedTagsList();
                        Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                        Clans.getPlugin().clanGUIFileManager.reloadClanGUIConfig();
                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                        for (Player p : onlinePlayers){
                            if (!onlinePlayers.isEmpty()){
                                p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                            }
                        }
                    }, 5L, TimeUnit.SECONDS);
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("disband")) {
                    if (args.length == 2){
                        if (args[1].length() > 1){
                            Player onlinePlayerOwner = Bukkit.getPlayer(args[1]);
                            OfflinePlayer offlinePlayerOwner = UsermapStorageUtil.getBukkitOfflinePlayerByName(args[1]);
                            if (onlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteClan(onlinePlayerOwner)){
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else if (offlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteOfflineClan(offlinePlayerOwner)){
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else {
                                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player").replace(PLAYER_TO_KICK, args[1])));
                            }
                        }else {
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")));
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6ClansLite &3~~~~~~~~~~"));
                    console.sendMessage(ColorUtils.translateColorCodes("&3Version: &6" + Clans.getPlugin().getDescription().getVersion()));
                    console.sendMessage(ColorUtils.translateColorCodes("&3Authors: &6" + Clans.getPlugin().getDescription().getAuthors()));
                    console.sendMessage(ColorUtils.translateColorCodes("&3Description: &6" + Clans.getPlugin().getDescription().getDescription()));
                    console.sendMessage(ColorUtils.translateColorCodes("&3Website: "));
                    console.sendMessage(ColorUtils.translateColorCodes("&6" + Clans.getPlugin().getDescription().getWebsite()));
                    console.sendMessage(ColorUtils.translateColorCodes("&3Discord:"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6https://discord.gg/crapticraft"));
                    console.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6ClansLite &3~~~~~~~~~~"));
                }

//----------------------------------------------------------------------------------------------------------------------
            }else {
                console.sendMessage(ColorUtils.translateColorCodes(sendUsageMessage()));
            }
        }
        return true;
    }

    private String sendUsageMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> configStringList = messagesConfig.getStringList("clanadmin-command-incorrect-usage");
        for (String string : configStringList){
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
