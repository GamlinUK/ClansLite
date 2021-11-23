package xyz.gamlin.clans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class ClanAdmin implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: Saving clans data..."));
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Failed to save clans.json to file!"));
                        sender.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the console for errors!"));
                    }
                    sender.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aClans saved!"));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Clans.getPlugin().reloadConfig();
                    sender.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &aThe config has been reloaded!"));
                }
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            logger.warning(ColorUtils.translateColorCodes("&4Sorry, that command can only be run by a player!"));
        }

        return true;
    }
}
