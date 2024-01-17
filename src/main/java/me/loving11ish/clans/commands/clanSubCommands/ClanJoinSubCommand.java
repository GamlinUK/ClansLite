package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.models.ClanInvite;
import me.loving11ish.clans.utils.ClanInviteUtil;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ClanJoinSubCommand {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    private static final String PLAYER_PLACEHOLDER = "%PLAYER%";
    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    public boolean clanJoinSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AtomicReference<String> inviterUUIDString = new AtomicReference<>("");
            Set<Map.Entry<UUID, ClanInvite>> clanInvitesList = ClanInviteUtil.getInvites();
            if (ClanInviteUtil.searchInvitee(player.getUniqueId().toString())) {
                clanInvitesList.forEach((invites) ->
                        inviterUUIDString.set(invites.getValue().getInviter()));
                console.sendMessage(String.valueOf(inviterUUIDString.get()));
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
