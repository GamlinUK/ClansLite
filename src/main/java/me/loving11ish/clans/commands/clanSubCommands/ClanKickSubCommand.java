package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

public class ClanKickSubCommand {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";
    private static final String PLAYER_TO_KICK = "%KICKEDPLAYER%";

    public boolean clanKickSubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                if (args[1].length() > 1) {
                    Clan targetClan = ClansStorageUtil.findClanByOwner(player);
                    if (ClansStorageUtil.findClanByOwner(player) != null) {
                        Player playerToKick = Bukkit.getPlayer(args[1]);
                        OfflinePlayer offlinePlayerToKick = UsermapStorageUtil.getBukkitOfflinePlayerByName(args[1]);
                        if (playerToKick != null) {
                            if (!player.getName().equalsIgnoreCase(args[1])){
                                Clan playerClan = ClansStorageUtil.findClanByPlayer(playerToKick);
                                if (targetClan.equals(playerClan)) {
                                    if (ClansStorageUtil.removeClanMember(targetClan, playerToKick)){
                                        String playerKickedMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-member-kick-successful")).replace(PLAYER_TO_KICK, args[1]);
                                        player.sendMessage(playerKickedMessage);
                                        if (playerToKick.isOnline()) {
                                            String kickMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-kicked-player-message")).replace(CLAN_PLACEHOLDER, targetClan.getClanFinalName());
                                            playerToKick.sendMessage(kickMessage);
                                            return true;
                                        }
                                    }else {
                                        String differentClanMessage = ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-clan")).replace(PLAYER_TO_KICK, args[1]);
                                        player.sendMessage(differentClanMessage);
                                    }
                                }else {
                                    String differentClanMessage = ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-clan")).replace(PLAYER_TO_KICK, args[1]);
                                    player.sendMessage(differentClanMessage);
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-kick-yourself")));
                            }
                        }else if (offlinePlayerToKick != null){
                            if (!player.getName().equalsIgnoreCase(args[1])){
                                Clan offlinePlayerClan = ClansStorageUtil.findClanByOfflinePlayer(offlinePlayerToKick);
                                if (targetClan.equals(offlinePlayerClan)){
                                    if (ClansStorageUtil.removeOfflineClanMember(targetClan, offlinePlayerToKick)){
                                        String playerKickedMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-member-kick-successful")).replace(PLAYER_TO_KICK, args[1]);
                                        player.sendMessage(playerKickedMessage);
                                    }else {
                                        String differentClanMessage = ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-clan")).replace(PLAYER_TO_KICK, args[1]);
                                        player.sendMessage(differentClanMessage);
                                    }
                                    return true;
                                }else {
                                    String differentClanMessage = ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-clan")).replace(PLAYER_TO_KICK, args[1]);
                                    player.sendMessage(differentClanMessage);
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-kick-yourself")));
                            }
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player").replace(PLAYER_TO_KICK, args[1])));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("must-be-owner-to-kick")));
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-kick-command-usage")));
                }
            }
            return true;

        }
        return false;
    }
}
