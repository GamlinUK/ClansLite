package xyz.gamlin.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClanInviteUtil;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class ClanCommand implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration clansConfig = Clans.getPlugin().getConfig();
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length < 1) {
                sender.sendMessage(ColorUtils.translateColorCodes(
                        "&6ClansLite usage:&3" +
                                "\n/clan create <name>" +
                                "\n/clan disband" +
                                "\n/clan invite <player>" +
                                "\n/clan kick <player>" +
                                "\n/clan info" +
                                "\n/clan list" +
                                "\n/clan prefix <prefix>"
                ));
            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length >= 2) {
                        if (args[1].length() < 3) {
                            player.sendMessage(ColorUtils.translateColorCodes("&3Clan name too short - minimum length is &63 characters&3."));
                            return true;
                        } else if (args[1].length() > 16) {
                            player.sendMessage(ColorUtils.translateColorCodes("&3Clan name too long - maximum length is &616 characters&3."));
                            return true;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 1; i < (args.length - 1); i++) {
                                stringBuilder.append(args[i]).append(" ");
                            }
                            stringBuilder.append(args[args.length - 1]);

                            if (ClansStorageUtil.createClan(player, stringBuilder.toString()) != null) {
                                String clanCreated = ColorUtils.translateColorCodes("&3Clan &6{0} &3was Created!").replace("{0}", args[1]);
                                player.sendMessage(clanCreated);
                            } else {
                                String clanNotCreated = ColorUtils.translateColorCodes("&3Clan &6{0} &3was NOT created, please make sure you're not already in a clan!").replace("{0}", args[1]);
                                player.sendMessage(clanNotCreated);
                            }
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("disband")) {
                    if (ClansStorageUtil.deleteClan(player)) {
                        sender.sendMessage(ColorUtils.translateColorCodes("&3Clan was disbanded!"));
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes("&3Failed to disband clan - Please make sure you're the owner!"));
                    }
                    return true;

                }
                if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length == 2) {
                        if (args[1].length() < 1) {

                            sender.sendMessage(ColorUtils.translateColorCodes("&3Please specify a player to invite!"));
                            return true;

                        }

                        if (ClansStorageUtil.findClanByOwner(player) == null) {

                            sender.sendMessage(ColorUtils.translateColorCodes("&3You must be a clan owner to invite people!"));
                            return true;

                        } else {

                            String invitedPlayerStr = args[1];

                            if (invitedPlayerStr.equalsIgnoreCase(player.getName())) {

                                sender.sendMessage(ColorUtils.translateColorCodes("&3You can't invite yourself!"));

                            } else {

                                Player invitedPlayer = Bukkit.getPlayer(invitedPlayerStr);

                                if (invitedPlayer == null) {

                                    String playerNotFound = ColorUtils.translateColorCodes("&3Player &6{0} &3was not found, make sure they are online!").replace("{0}", invitedPlayerStr);
                                    sender.sendMessage(playerNotFound);

                                } else if (ClansStorageUtil.findClanByPlayer(invitedPlayer) != null) {

                                    String playerNotFound = ColorUtils.translateColorCodes("&3Player &6{0} §3is already in a clan!").replace("{0}", invitedPlayerStr);
                                    sender.sendMessage(playerNotFound);

                                } else {

                                    Clan clan = ClansStorageUtil.findClanByOwner(player);

                                    if (clan.getClanMembers().size() >= clansConfig.getInt("max-clan-size")) {

                                        Integer maxSize = clansConfig.getInt("max-clan-size");
                                        player.sendMessage(ColorUtils.translateColorCodes("&3You have reached the clam members size limit " + "&a(" + maxSize + ") &3!"));
                                        return true;

                                    }

                                    if (ClanInviteUtil.createInvite(player.getUniqueId(), invitedPlayer.getUniqueId()) != null) {

                                        String confirmationString = ColorUtils.translateColorCodes("&3You have invited &6{0}&3 to your clan!").replace("{0}", invitedPlayer.getName());
                                        player.sendMessage(confirmationString);
                                        String invitationString = ColorUtils.translateColorCodes("&3You have been invited to a clan by &6{0}&3 - use /clan join").replace("{0}", player.getName());
                                        invitedPlayer.sendMessage(invitationString);

                                    } else {

                                        String failureString = ColorUtils.translateColorCodes("&3Failed to send invite to &6{0}&3, this player might already have an invitation!").replace("{0}", invitedPlayer.getName());
                                        player.sendMessage(failureString);

                                    }
                                }
                            }
                        }

                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes("&3Please specify a player to invite!"));
                    }

                    return true;

                }
                if (args[0].equalsIgnoreCase("prefix")) {
                    if (args.length == 2) {
                        if (args[1].length() >= 3 && args[1].length() <= 16) {
                            // C/U prefix
                            Clan playerClan = ClansStorageUtil.findClanByOwner(player);
                            ClansStorageUtil.updatePrefix(player, args[1]);
                            String prefixConfirmation = ColorUtils.translateColorCodes("&3Successfully changed clan prefix to &6{0}&3!").replace("{0}", playerClan.getClanPrefix());
                            sender.sendMessage(prefixConfirmation);
                            return true;
                        } else if (args[1].length() > 16) {
                            sender.sendMessage(ColorUtils.translateColorCodes("&3Clan prefix too long - maximum length is &616 characters&3."));
                            return true;
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes("&3Clan prefix too short - minimum length is 763 characters&3."));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes("&3Clan prefix too short - minimum length is &63 characters&3."));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    ArrayList clans = ClansStorageUtil.getClans();
                    StringBuilder clansString = new StringBuilder();
                    if (clans.size() == 0) {
                        sender.sendMessage(ColorUtils.translateColorCodes("&3No clans found!"));
                    } else {
                        clansString.append(ColorUtils.translateColorCodes("&3&lCurrent clans:\n"));
                        clans.forEach((clan) -> clansString.append(ColorUtils.translateColorCodes(clan + "\n")));
                        sender.sendMessage(clansString.toString());
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (ClanInviteUtil.searchInvitee(player.getUniqueId())) {
                        Clan clan = ClansStorageUtil.findClanByPlayer(ClanInviteUtil.getInviteOwner(player.getUniqueId()));
                        if (clan != null) {
                            if (ClansStorageUtil.addClanMember(clan, player)) {
                                String joinMessage = ColorUtils.translateColorCodes("&3Successfully joined &6{0}&3!").replace("{0}", clan.getClanName());
                                player.sendMessage(joinMessage);
                            } else {
                                String failureMessage = ColorUtils.translateColorCodes("§3Failed to join §6{0}§3").replace("{0}", clan.getClanName());
                                player.sendMessage(failureMessage);
                            }
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes("&3Failed to join a clan - no clan was found!"));
                        }
                    } else {
                        player.sendMessage(ColorUtils.translateColorCodes("&3Failed to join a clan - no invite was found!"));
                    }

                    return true;
                }
                if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length == 2) {
                        if (args[1].length() < 1) {
                            Clan targetClan = ClansStorageUtil.findClanByOwner(player);
                            if (ClansStorageUtil.findClanByOwner(player) != null) {
                                Player playerToKick = Bukkit.getPlayer(args[1]);
                                if (playerToKick != null) {

                                    Clan playerClan = ClansStorageUtil.findClanByPlayer(playerToKick);

                                    if (targetClan.equals(playerClan)) {

                                        targetClan.removeClanMember(playerToKick.getUniqueId());
                                        String playerKickedMessage = ColorUtils.translateColorCodes("&3Player &6{0}&3 was kicked from your clan.").replace("{0}", args[1]);
                                        player.sendMessage(playerKickedMessage);

                                        if (playerToKick.isOnline()) {
                                            String kickMessage = ColorUtils.translateColorCodes("&3You were kicked from &6{0}.").replace("{0}", targetClan.getClanName());
                                            playerToKick.sendMessage(kickMessage);
                                            return true;
                                        }
                                    } else {
                                        String differentClanMessage = ColorUtils.translateColorCodes("§3Player §6{0}§3 is not in your clan.").replace("{0}", args[1]);
                                        player.sendMessage(differentClanMessage);
                                    }

                                } else {
                                    String playerNotFound = ColorUtils.translateColorCodes("&3Could not find player &6{0}&3.").replace("{0}", args[1]);
                                    player.sendMessage(playerNotFound);
                                }

                            } else {
                                player.sendMessage(ColorUtils.translateColorCodes("&3You are not an owner of a clan!"));
                            }

                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("info")) {

                    Clan clan = ClansStorageUtil.findClanByPlayer(player);

                    if (clan != null) {

                        ArrayList<UUID> clanMembers = clan.getClanMembers();
                        StringBuilder clanInfo = new StringBuilder(ColorUtils.translateColorCodes("&7-----\n&6&l{0}&r&7 ({1})&r").replace("{0}", clan.getClanName()).replace("{1}", clan.getClanPrefix()));
                        Player clanOwner = Bukkit.getPlayer(clan.getClanOwner());

                        if (clanOwner != null) {
                            clanInfo.append(ColorUtils.translateColorCodes("\n\n&3Owner: &a{0}").replace("{0}", clanOwner.getName()));
                        } else {
                            String offlineOwner = Bukkit.getOfflinePlayer(clan.getClanOwner()).getName();
                            clanInfo.append(ColorUtils.translateColorCodes("\n\n&3Owner: &c{0}").replace("{0}", offlineOwner));
                        }

                        if (clanMembers.size() > 0) {

                            clanInfo.append(ColorUtils.translateColorCodes("\n\n&3Members:"));

                            for (UUID clanMember : clanMembers) {

                                if (clanMember != null) {

                                    Player clanPlayer = Bukkit.getPlayer(clanMember);

                                    if (clanPlayer != null) {

                                        clanInfo.append(ColorUtils.translateColorCodes("\n&a{0}").replace("{0}", clanPlayer.getName()));

                                    } else {

                                        String offlinePlayer = Bukkit.getOfflinePlayer(clanMember).getName();
                                        clanInfo.append(ColorUtils.translateColorCodes("\n&c{0}").replace("{0}", offlinePlayer));

                                    }
                                }

                            }
                        }

                        player.sendMessage(clanInfo.toString());

                    } else {
                        player.sendMessage(ColorUtils.translateColorCodes("&3You are not in a clan!"));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("leave")) {
                    if (ClansStorageUtil.findClanByOwner(player) != null) {
                        player.sendMessage(ColorUtils.translateColorCodes("&3You are the owner of a clan, use &6/clan disband&3."));
                    }
                    Clan targetClan = ClansStorageUtil.findClanByPlayer(player);
                    if (targetClan != null) {
                        if (targetClan.removeClanMember(player.getUniqueId())) {
                            String leaveMessage = ColorUtils.translateColorCodes("&3You have left &6{0}.").replace("{0}", targetClan.getClanName());
                            player.sendMessage(leaveMessage);
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes("&3Failed to leave clan, please try again later."));
                        }
                    }
                    return true;
                } else {
                    player.sendMessage(ColorUtils.translateColorCodes("&3Unrecognised argument please use &6/clan&3."));
                }
            }

        }

        if (sender instanceof ConsoleCommandSender) {
            logger.warning(ColorUtils.translateColorCodes("&4Sorry, that command can only be run by a player!"));
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }

}
