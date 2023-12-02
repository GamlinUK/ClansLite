package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ClanLeaveSubCommand {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    public boolean clanLeaveSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ClansStorageUtil.findClanByOwner(player) != null) {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-clan-owner")));
                return true;
            }
            Clan targetClan = ClansStorageUtil.findClanByPlayer(player);
            if (targetClan != null) {
                if (ClansStorageUtil.removeClanMember(targetClan, player)){
                    String leaveMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-leave-successful")).replace(CLAN_PLACEHOLDER, targetClan.getClanFinalName());
                    player.sendMessage(leaveMessage);
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-leave-failed")));
                }
            }
            return true;
        }
        return true;
    }
}
