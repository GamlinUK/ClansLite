package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ClanPvpSubCommand {

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean clanPvpSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
