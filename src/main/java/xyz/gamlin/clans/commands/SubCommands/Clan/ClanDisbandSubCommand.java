package xyz.gamlin.clans.commands.SubCommands.Clan;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.IOException;

public class ClanDisbandSubCommand {
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    public boolean disbandClanSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
                try {
                    if (ClansStorageUtil.deleteClan(player)) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-disband-failure")));
                    }
                } catch (IOException e) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                    e.printStackTrace();
                }
                return true;
        }
        return false;
    }
}
