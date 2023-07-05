package xyz.gamlin.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

public class ClanPvpSubCommand {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean clanPvpSubCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                if (ClansStorageUtil.isClanOwner(player)){
                    if (ClansStorageUtil.findClanByOwner(player) != null){
                        Clan clan = ClansStorageUtil.findClanByOwner(player);
                        if (clan.isFriendlyFireAllowed()){
                            clan.setFriendlyFireAllowed(false);
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("disabled-friendly-fire")));
                        }else {
                            clan.setFriendlyFireAllowed(true);
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("enabled-friendly-fire")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-clan")));
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
            }
            return true;

        }
        return false;
    }
}
