package xyz.gamlin.clans.commands.SubCommands.Clan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.apiEvents.ClanCreateEvent;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.*;

public class ClanCreateSubCommand {
    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    int MIN_CHAR_LIMIT = clansConfig.getInt("clan-tags.min-character-limit");
    int MAX_CHAR_LIMIT = clansConfig.getInt("clan-tags.max-character-limit");
    private static final String CLAN_PLACEHOLDER = "%CLAN%";
    private static final String CLAN_OWNER = "%CLANOWNER%";
    Set<Map.Entry<UUID, Clan>> clans = ClansStorageUtil.getClans();
    ArrayList<String> clanNamesList = new ArrayList<>();
    public boolean createClanSubCommand(CommandSender sender, String[] args, List<String> bannedTags) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();


            clans.forEach((clans) ->
                    clanNamesList.add(clans.getValue().getClanFinalName()));
            if (args.length >= 2) {
                if (bannedTags.contains(args[1])) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-is-banned").replace(CLAN_PLACEHOLDER, args[1])));
                    return true;
                }
                if (clanNamesList.contains(args[1])) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-already-taken").replace(CLAN_PLACEHOLDER, args[1])));
                    return true;
                }
                if (args[1].length() < MIN_CHAR_LIMIT) {
                    int minCharLimit = clansConfig.getInt("clan-tags.min-character-limit");
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-too-short").replace("%CHARMIN%", Integer.toString(minCharLimit))));
                    return true;
                } else if (args[1].length() > MAX_CHAR_LIMIT) {
                    int maxCharLimit = clansConfig.getInt("clan-tags.max-character-limit");
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-too-long").replace("%CHARMAX%", Integer.toString(maxCharLimit))));
                    return true;
                } else {
                    if (!ClansStorageUtil.isClanExisting(player)) {
                        Clan clan = ClansStorageUtil.createClan(player, args[1]);
                        String clanCreated = ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-successfully")).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(args[1]));
                        player.sendMessage(clanCreated);
                        fireClanCreateEvent(player, clan);
                        if (clansConfig.getBoolean("clan-creation.announce-to-all")) {
                            if (clansConfig.getBoolean("clan-creation.send-as-title")) {
                                for (Player onlinePlayers : Clans.connectedPlayers.keySet()) {
                                    onlinePlayers.sendTitle(ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-broadcast-title-1")
                                                    .replace(CLAN_OWNER, player.getName())
                                                    .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(args[1]))),
                                            ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-broadcast-title-2")
                                                    .replace(CLAN_OWNER, player.getName())
                                                    .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(args[1]))),
                                            30, 30, 30);
                                }
                            } else {
                                Bukkit.broadcastMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-broadcast-chat")
                                        .replace(CLAN_OWNER, player.getName())
                                        .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(args[1]))));
                            }
                        }
                    } else {
                        String clanNotCreated = ColorUtils.translateColorCodes(messagesConfig.getString("clan-creation-failed")).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(args[1]));
                        player.sendMessage(clanNotCreated);
                    }
                    clanNamesList.clear();
                    return true;
                }
            }
        }
        return false;
    }

    private static void fireClanCreateEvent(Player player, Clan clan) {
        ClanCreateEvent clanCreateEvent = new ClanCreateEvent(player, clan);
        Bukkit.getPluginManager().callEvent(clanCreateEvent);
    }
}
