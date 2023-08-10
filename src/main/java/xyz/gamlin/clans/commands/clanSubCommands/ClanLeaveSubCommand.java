package xyz.gamlin.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

public class ClanLeaveSubCommand {

    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    public boolean clanLeaveSubCommand(CommandSender sender) {
        if (sender instanceof Player player) {
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
