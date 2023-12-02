package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.io.IOException;

public class ClanDisbandSubCommand {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean disbandClanSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
