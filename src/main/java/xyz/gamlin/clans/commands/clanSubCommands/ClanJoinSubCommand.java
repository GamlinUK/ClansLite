package xyz.gamlin.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.models.ClanInvite;
import xyz.gamlin.clans.utils.ClanInviteUtil;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class ClanJoinSubCommand {

    Logger logger = Clans.getPlugin().getLogger();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    private static final String PLAYER_PLACEHOLDER = "%PLAYER%";
    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    public boolean clanJoinSubCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            AtomicReference<String> inviterUUIDString = new AtomicReference<>("");
            Set<Map.Entry<UUID, ClanInvite>> clanInvitesList = ClanInviteUtil.getInvites();
            if (ClanInviteUtil.searchInvitee(player.getUniqueId().toString())) {
                clanInvitesList.forEach((invites) ->
                        inviterUUIDString.set(invites.getValue().getInviter()));
                logger.info(String.valueOf(inviterUUIDString.get()));
                Player inviterPlayer = Bukkit.getPlayer(UUID.fromString(inviterUUIDString.get()));
                Clan clan = ClansStorageUtil.findClanByOwner(inviterPlayer);
                if (clan != null) {
                    if (ClansStorageUtil.addClanMember(clan, player)) {
                        ClanInviteUtil.removeInvite(inviterUUIDString.get());
                        String joinMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-successful")).replace(CLAN_PLACEHOLDER, clan.getClanFinalName());
                        player.sendMessage(joinMessage);
                        if (clansConfig.getBoolean("clan-join.announce-to-all")){
                            if (clansConfig.getBoolean("clan-join.send-as-title")){
                                for (Player onlinePlayers : Clans.connectedPlayers.keySet()){
                                    onlinePlayers.sendTitle(ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-broadcast-title-1")
                                                    .replace(PLAYER_PLACEHOLDER, player.getName())
                                                    .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clan.getClanFinalName()))),
                                            ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-broadcast-title-2")
                                                    .replace(PLAYER_PLACEHOLDER, player.getName())
                                                    .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clan.getClanFinalName()))),
                                            30, 30, 30);
                                }
                            }else {
                                Bukkit.broadcastMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-broadcast-chat")
                                        .replace(PLAYER_PLACEHOLDER, player.getName())
                                        .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clan.getClanFinalName()))));
                            }
                        }
                    }else {
                        String failureMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-failed")).replace(CLAN_PLACEHOLDER, clan.getClanFinalName());
                        player.sendMessage(failureMessage);
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-failed-no-valid-clan")));
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-failed-no-invite")));
            }
            return true;

        }
        return false;
    }
}
