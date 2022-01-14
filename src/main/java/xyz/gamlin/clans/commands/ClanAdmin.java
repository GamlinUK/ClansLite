package xyz.gamlin.clans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class ClanAdmin implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                    }
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Clans.getPlugin().reloadConfig();
                    Clans.getPlugin().clansFileManager.reloadClansConfig();
                    Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                }
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("save")) {
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("saving-clans-start")));
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-1")));
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("clans-save-error-2")));
                        e.printStackTrace();
                    }
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Clans.getPlugin().reloadConfig();
                    Clans.getPlugin().clansFileManager.reloadClansConfig();
                    Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")));
                }
            }
        }
        return true;
    }
}
