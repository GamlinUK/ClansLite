package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClanListSubCommand {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean clanListSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Set<Map.Entry<UUID, Clan>> clans = ClansStorageUtil.getClans();
            StringBuilder clansString = new StringBuilder();
            if (clans.isEmpty()) {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("no-clans-to-list")));
            } else {
                clansString.append(ColorUtils.translateColorCodes(messagesConfig.getString("clans-list-header") + "\n"));
                clans.forEach((clan) ->
                        clansString.append(ColorUtils.translateColorCodes(clan.getValue().getClanFinalName() + "\n")));
                clansString.append(" ");
                clansString.append(ColorUtils.translateColorCodes(messagesConfig.getString("clans-list-footer")));
                player.sendMessage(clansString.toString());
            }
            return true;

        }
        return false;
    }
}
