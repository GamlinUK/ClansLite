package xyz.gamlin.clans.commands;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.UsermapStorageUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClanAdmin implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String PLAYER_TO_KICK = "%KICKEDPLAYER%";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        if (!ClansStorageUtil.getRawClansList().isEmpty()){
                            ClansStorageUtil.saveClans();
                        }else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-clans")));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                    }
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")));
                    FoliaLib foliaLib = new FoliaLib(Clans.getPlugin());
                    Clans plugin = Clans.getPlugin();
                    plugin.onDisable();
                    foliaLib.getImpl().runLater(new Runnable() {
                        @Override
                        public void run() {
                            plugin.onEnable();
                        }
                    }, 5L, TimeUnit.SECONDS);
                    foliaLib.getImpl().runLater(new Runnable() {
                        @Override
                        public void run() {
                            Clans.getPlugin().reloadConfig();
                            ClanCommand.updateBannedTagsList();
                            Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                            Clans.getPlugin().clanGUIFileManager.reloadClanGUIConfig();
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
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
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else if (offlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteOfflineClan(offlinePlayerOwner)){
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player").replace(PLAYER_TO_KICK, args[1])));
                            }
                        }else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")));
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&nClansLite&r &3~~~~~~~~~~"));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Version: &6" + Clans.getPlugin().getDescription().getVersion()));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Authors: &6" + Clans.getPlugin().getDescription().getAuthors()));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Description: &6" + Clans.getPlugin().getDescription().getDescription()));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Website: "));
                    sender.sendMessage(ColorUtils.translateColorCodes("&6" + Clans.getPlugin().getDescription().getWebsite()));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Discord:"));
                    sender.sendMessage(ColorUtils.translateColorCodes("&6https://discord.gg/crapticraft"));
                    sender.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&nClansLite&r &3~~~~~~~~~~"));
                }

//----------------------------------------------------------------------------------------------------------------------
            }else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-1")));
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-2")));
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-3")));
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-4")));
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-5")));
            }
        }

//----------------------------------------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("save")) {
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        if (!ClansStorageUtil.getRawClansList().isEmpty()){
                            ClansStorageUtil.saveClans();
                        }else {
                            logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-clans")));
                        }
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                        e.printStackTrace();
                    }
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")));
                    FoliaLib foliaLib = new FoliaLib(Clans.getPlugin());
                    Clans plugin = Clans.getPlugin();
                    plugin.onDisable();
                    foliaLib.getImpl().runLater(new Runnable() {
                        @Override
                        public void run() {
                            plugin.onEnable();
                        }
                    }, 5L, TimeUnit.SECONDS);
                    foliaLib.getImpl().runLater(new Runnable() {
                        @Override
                        public void run() {
                            Clans.getPlugin().reloadConfig();
                            ClanCommand.updateBannedTagsList();
                            Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                            Clans.getPlugin().clanGUIFileManager.reloadClanGUIConfig();
                            logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
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
                                        logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else if (offlinePlayerOwner != null){
                                try {
                                    if (ClansStorageUtil.deleteOfflineClan(offlinePlayerOwner)){
                                        logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                                    }else {
                                        logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clan-admin-disband-failure")));
                                    }
                                } catch (IOException e) {
                                    logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                            }else {
                                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player").replace(PLAYER_TO_KICK, args[1])));
                            }
                        }else {
                            logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")));
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    logger.info(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6ClansLite &3~~~~~~~~~~"));
                    logger.info(ColorUtils.translateColorCodes("&3Version: &6" + Clans.getPlugin().getDescription().getVersion()));
                    logger.info(ColorUtils.translateColorCodes("&3Authors: &6" + Clans.getPlugin().getDescription().getAuthors()));
                    logger.info(ColorUtils.translateColorCodes("&3Description: &6" + Clans.getPlugin().getDescription().getDescription()));
                    logger.info(ColorUtils.translateColorCodes("&3Website: "));
                    logger.info(ColorUtils.translateColorCodes("&6" + Clans.getPlugin().getDescription().getWebsite()));
                    logger.info(ColorUtils.translateColorCodes("&3Discord:"));
                    logger.info(ColorUtils.translateColorCodes("&6https://discord.gg/crapticraft"));
                    logger.info(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6ClansLite &3~~~~~~~~~~"));
                }

//----------------------------------------------------------------------------------------------------------------------
            }else {
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-1")));
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-2")));
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-3")));
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-4")));
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("clanadmin-command-incorrect-usage.line-5")));
            }
        }
        return true;
    }
}
