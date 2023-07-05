package xyz.gamlin.clans.commands.clanSubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.*;

public class ClanPrefixSubCommand {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    int MIN_CHAR_LIMIT = clansConfig.getInt("clan-tags.min-character-limit");
    int MAX_CHAR_LIMIT = clansConfig.getInt("clan-tags.max-character-limit");

    Set<Map.Entry<UUID, Clan>> clans = ClansStorageUtil.getClans();
    ArrayList<String> clansPrefixList = new ArrayList<>();

    public boolean clanPrefixSubCommand(CommandSender sender, String[] args, List<String> bannedTags) {
        if (sender instanceof Player player) {
            clans.forEach((clans) ->
                    clansPrefixList.add(clans.getValue().getClanPrefix()));
            if (args.length == 2) {
                if (bannedTags.contains(args[1])){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-is-banned").replace("%CLANPREFIX%", args[1])));
                    return true;
                }
                if (clansPrefixList.contains(args[1])){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-already-taken").replace("%CLANPREFIX%", args[1])));
                    return true;
                }
                if (ClansStorageUtil.isClanOwner(player)){
                    if (args[1].length() >= MIN_CHAR_LIMIT && args[1].length() <= MAX_CHAR_LIMIT) {
                        Clan playerClan = ClansStorageUtil.findClanByOwner(player);
                        ClansStorageUtil.updatePrefix(player, args[1]);
                        String prefixConfirmation = ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-change-successful")).replace("%CLANPREFIX%", playerClan.getClanPrefix());
                        sender.sendMessage(prefixConfirmation);
                        clansPrefixList.clear();
                        return true;
                    }else if (args[1].length() > MAX_CHAR_LIMIT) {
                        int maxCharLimit = clansConfig.getInt("clan-tags.max-character-limit");
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-too-long").replace("%CHARMAX%", String.valueOf(maxCharLimit))));
                        clansPrefixList.clear();
                        return true;
                    }else {
                        int minCharLimit = clansConfig.getInt("clan-tags.min-character-limit");
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-too-short").replace("%CHARMIN%", String.valueOf(minCharLimit))));
                        clansPrefixList.clear();
                        return true;
                    }
                }else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("must-be-owner-to-change-prefix")));
                    clansPrefixList.clear();
                    return true;
                }
            }else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invalid-prefix")));
                clansPrefixList.clear();
            }
            return true;

        }
        return false;
    }
}
