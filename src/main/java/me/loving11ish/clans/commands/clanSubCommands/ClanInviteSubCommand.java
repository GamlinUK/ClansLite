package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClanInviteUtil;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ClanInviteSubCommand {

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String INVITED_PLAYER = "%INVITED%";

    public boolean clanInviteSubCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                if (args[1].length() < 1) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-no-valid-player")));
                    return true;
                }
                if (ClansStorageUtil.findClanByOwner(player) == null) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-not-clan-owner")));
                    return true;
                } else {
                    String invitedPlayerStr = args[1];
                    if (invitedPlayerStr.equalsIgnoreCase(player.getName())) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-self-error")));
                    } else {
                        Player invitedPlayer = Bukkit.getPlayer(invitedPlayerStr);
                        if (invitedPlayer == null) {
                            String playerNotFound = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invitee-not-found")).replace(INVITED_PLAYER, invitedPlayerStr);
                            sender.sendMessage(playerNotFound);
                        } else if (ClansStorageUtil.findClanByPlayer(invitedPlayer) != null) {
                            String playerAlreadyInClan = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-invited-already-in-clan")).replace(INVITED_PLAYER, invitedPlayerStr);
                            sender.sendMessage(playerAlreadyInClan);
                        } else {
                            Clan clan = ClansStorageUtil.findClanByOwner(player);
                            if (!(player.hasPermission("clanslite.maxclansize.*")||player.hasPermission("clanslite.*")||player.isOp())){
                                if (!clansConfig.getBoolean("clan-size.tiered-clan-system.enabled")){
                                    if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.default-max-clan-size")) {
                                        int maxSize = clansConfig.getInt("clan-size.default-max-clan-size");
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(maxSize)));
                                        return true;
                                    }
                                }else {
                                    if (player.hasPermission("clanslite.maxclansize.group6")){
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-6")){
                                            int g6MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-6");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g6MaxSize)));
                                            return true;
                                        }
                                    }else if (player.hasPermission("clanslite.maxclansize.group5")){
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2")){
                                            int g5MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-5");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g5MaxSize)));
                                            return true;
                                        }
                                    }else if (player.hasPermission("clanslite.maxclansize.group4")) {
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4")) {
                                            int g4MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g4MaxSize)));
                                            return true;
                                        }
                                    }else if (player.hasPermission("clanslite.maxclansize.group3")) {
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4")) {
                                            int g3MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-3");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g3MaxSize)));
                                            return true;
                                        }
                                    }else if (player.hasPermission("clanslite.maxclansize.group2")) {
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2")) {
                                            int g2MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g2MaxSize)));
                                            return true;
                                        }
                                    }else if (player.hasPermission("clanslite.maxclansize.group1")) {
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-1")) {
                                            int g1MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-1");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", String.valueOf(g1MaxSize)));
                                            return true;
                                        }
                                    }
                                }
                            }


                            if (Clans.getFloodgateApi() != null){
                                if (Clans.bedrockPlayers.containsKey(invitedPlayer)){
                                    String bedrockInvitedPlayerUUIDString = Clans.bedrockPlayers.get(invitedPlayer);
                                    if (ClanInviteUtil.createInvite(player.getUniqueId().toString(), bedrockInvitedPlayerUUIDString) != null){
                                        String confirmationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-successful")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(confirmationString);
                                        String invitationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invited-player-invite-pending")).replace("%CLANOWNER%", player.getName());
                                        invitedPlayer.sendMessage(invitationString);
                                        return true;
                                    }else {
                                        String failureString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-failed")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(failureString);
                                        return true;
                                    }
                                }else {
                                    if (ClanInviteUtil.createInvite(player.getUniqueId().toString(), invitedPlayer.getUniqueId().toString()) != null) {
                                        String confirmationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-successful")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(confirmationString);
                                        String invitationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invited-player-invite-pending")).replace("%CLANOWNER%", player.getName());
                                        invitedPlayer.sendMessage(invitationString);
                                        return true;
                                    }else {
                                        String failureString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-failed")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(failureString);
                                        return true;
                                    }
                                }
                            }

                            if (ClanInviteUtil.createInvite(player.getUniqueId().toString(), invitedPlayer.getUniqueId().toString()) != null) {
                                String confirmationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-successful")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                player.sendMessage(confirmationString);
                                String invitationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invited-player-invite-pending")).replace("%CLANOWNER%", player.getName());
                                invitedPlayer.sendMessage(invitationString);
                                return true;
                            } else {
                                String failureString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-failed")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                player.sendMessage(failureString);
                                return true;
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-no-valid-player")));
            }
            return true;

        }
        return false;
    }
}
