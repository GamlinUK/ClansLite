package xyz.gamlin.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.models.ClanInvite;
import xyz.gamlin.clans.utils.ClanInviteUtil;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class ClanCommand implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();

    public static Integer taskID1;

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    int MIN_CHAR_LIMIT = clansConfig.getInt("clan-tags.min-character-limit");
    int MAX_CHAR_LIMIT = clansConfig.getInt("clan-tags.max-character-limit");

    private static final String CLAN_PLACEHOLDER = "%CLAN%";
    private static final String INVITED_PLAYER = "%INVITED%";
    private static final String PLAYER_TO_KICK = "%KICKEDPLAYER%";
    private static final String OWNER = "%OWNER%";
    private static final String CLAN_OWNER = "%CLANOWNER%";
    private static final String CLAN_MEMBER = "%MEMBER%";
    private static final String ALLY_CLAN = "%ALLYCLAN%";
    private static final String ALLY_OWNER = "%ALLYOWNER%";
    private static final String ENEMY_CLAN = "%ENEMYCLAN%";
    private static final String ENEMY_OWNER = "%ENEMYOWNER%";
    private static final String TIME_LEFT = "%TIMELEFT%";

    private static List<String> bannedTags;

    HashMap<UUID, Long> homeCoolDownTimer = new HashMap<>();

    Set<Map.Entry<UUID, Clan>> clans = ClansStorageUtil.getClans();
    ArrayList<String> clanNamesList = new ArrayList<>();
    ArrayList<String> clansPrefixList = new ArrayList<>();

    public static void updateBannedTagsList(){
        taskID1 = Bukkit.getScheduler().scheduleSyncDelayedTask(Clans.getPlugin(), new Runnable() {
            @Override
            public void run() {
                bannedTags = Clans.getPlugin().getConfig().getStringList("clan-tags.disallowed-tags");
            }
        }, 10);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length < 1) {
                if (clansConfig.getBoolean("clan-home.enabled") && clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(
                            "&6ClansLite usage:&3" +
                                    "\n/clan create <name>" +
                                    "\n/clan disband" +
                                    "\n/clan invite <player>" +
                                    "\n/clan kick <player>" +
                                    "\n/clan info" +
                                    "\n/clan list" +
                                    "\n/clan prefix <prefix>" +
                                    "\n/clan ally [add|remove] <clan-owner>" +
                                    "\n/clan enemy [add|remove] <clan-owner>" +
                                    "\n/clan pvp" +
                                    "\n/clan [sethome|home]"
                    ));
                    return true;
                }else if (clansConfig.getBoolean("clan-home.enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(
                            "&6ClansLite usage:&3" +
                                    "\n/clan create <name>" +
                                    "\n/clan disband" +
                                    "\n/clan invite <player>" +
                                    "\n/clan kick <player>" +
                                    "\n/clan info" +
                                    "\n/clan list" +
                                    "\n/clan prefix <prefix>" +
                                    "\n/clan ally [add|remove] <clan-owner>" +
                                    "\n/clan enemy [add|remove] <clan-owner>" +
                                    "\n/clan [sethome|home]"
                    ));
                    return true;
                }else if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(
                            "&6ClansLite usage:&3" +
                                    "\n/clan create <name>" +
                                    "\n/clan disband" +
                                    "\n/clan invite <player>" +
                                    "\n/clan kick <player>" +
                                    "\n/clan info" +
                                    "\n/clan list" +
                                    "\n/clan prefix <prefix>" +
                                    "\n/clan ally [add|remove] <clan-owner>" +
                                    "\n/clan enemy [add|remove] <clan-owner>" +
                                    "\n/clan pvp"
                    ));
                    return true;
                }else {
                    sender.sendMessage(ColorUtils.translateColorCodes(
                            "&6ClansLite usage:&3" +
                                    "\n/clan create <name>" +
                                    "\n/clan disband" +
                                    "\n/clan invite <player>" +
                                    "\n/clan kick <player>" +
                                    "\n/clan info" +
                                    "\n/clan list" +
                                    "\n/clan prefix <prefix>" +
                                    "\n/clan ally [add|remove] <clan-owner>" +
                                    "\n/clan enemy [add|remove] <clan-owner>"
                    ));
                }
                return true;
            }else {

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("create")) {
                    clans.forEach((clans) ->
                            clanNamesList.add(clans.getValue().getClanFinalName()));
                    if (args.length >= 2) {
                        if (bannedTags.contains(args[1])){
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-is-banned").replace(CLAN_PLACEHOLDER, args[1])));
                            return true;
                        }
                        if (clanNamesList.contains(args[1])){
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-already-taken").replace(CLAN_PLACEHOLDER, args[1])));
                            return true;
                        }
                        if (args[1].length() < MIN_CHAR_LIMIT) {
                            Integer minCharLimit = clansConfig.getInt("clan-tags.min-character-limit");
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-too-short").replace("%CHARMIN%", minCharLimit.toString())));
                            return true;
                        } else if (args[1].length() > MAX_CHAR_LIMIT) {
                            Integer maxCharLimit = clansConfig.getInt("clan-tags.max-character-limit");
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-name-too-long").replace("%CHARMAX%", maxCharLimit.toString())));
                            return true;
                        }else {
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 1; i < (args.length - 1); i++) {
                                stringBuilder.append(args[i]).append(" ");
                            }
                            stringBuilder.append(args[args.length - 1]);

                            if (!ClansStorageUtil.isClanExisting(player)) {
                                ClansStorageUtil.createClan(player, args[1]);
                                String clanCreated = ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-successfully")).replace(CLAN_PLACEHOLDER, args[1]);
                                player.sendMessage(clanCreated);
                            } else {
                                String clanNotCreated = ColorUtils.translateColorCodes(messagesConfig.getString("clan-creation-failed")).replace(CLAN_PLACEHOLDER, args[1]);
                                player.sendMessage(clanNotCreated);
                            }
                            clanNamesList.clear();
                            return true;
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("disband")) {
                    try {
                        if (ClansStorageUtil.deleteClan(player)) {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")));
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-disband-failure")));
                        }
                    } catch (IOException e) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                        e.printStackTrace();
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("invite")) {
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
                                    if (!clansConfig.getBoolean("clan-size.tiered-clan-system.enabled")){
                                        if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.default-max-clan-size")) {
                                            Integer maxSize = clansConfig.getInt("clan-size.default-max-clan-size");
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", maxSize.toString()));
                                            return true;
                                        }
                                    }else {
                                        if (player.hasPermission("clanslite.maxclansize.group6")){
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-6")){
                                                Integer g6MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-6");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g6MaxSize.toString()));
                                                return true;
                                            }
                                        }else if (player.hasPermission("clanslite.maxclansize.group5")){
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2")){
                                                Integer g5MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-5");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g5MaxSize.toString()));
                                                return true;
                                            }
                                        }else if (player.hasPermission("clanslite.maxclansize.group4")) {
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4")) {
                                                Integer g4MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g4MaxSize.toString()));
                                                return true;
                                            }
                                        }else if (player.hasPermission("clanslite.maxclansize.group3")) {
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-4")) {
                                                Integer g3MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-3");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g3MaxSize.toString()));
                                                return true;
                                            }
                                        }else if (player.hasPermission("clanslite.maxclansize.group2")) {
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2")) {
                                                Integer g2MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-2");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g2MaxSize.toString()));
                                                return true;
                                            }
                                        }else if (player.hasPermission("clanslite.maxclansize.group1")) {
                                            if (clan.getClanMembers().size() >= clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-1")) {
                                                Integer g1MaxSize = clansConfig.getInt("clan-size.tiered-clan-system.permission-group-list.group-1");
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-max-size-reached")).replace("%LIMIT%", g1MaxSize.toString()));
                                                return true;
                                            }
                                        }
                                    }

                                    if (ClanInviteUtil.createInvite(player.getUniqueId().toString(), invitedPlayer.getUniqueId().toString()) != null) {
                                        String confirmationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-successful")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(confirmationString);
                                        String invitationString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invited-player-invite-pending")).replace("%CLANOWNER%", player.getName());
                                        invitedPlayer.sendMessage(invitationString);
                                    } else {
                                        String failureString = ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-failed")).replace(INVITED_PLAYER, invitedPlayer.getName());
                                        player.sendMessage(failureString);
                                    }
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-no-valid-player")));
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("prefix")) {
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
                            if (args[1].length() >= 3 && args[1].length() <= 32) {
                                // C/U prefix
                                Clan playerClan = ClansStorageUtil.findClanByOwner(player);
                                ClansStorageUtil.updatePrefix(player, args[1]);
                                String prefixConfirmation = ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-change-successful")).replace("%CLANPREFIX%", playerClan.getClanPrefix());
                                sender.sendMessage(prefixConfirmation);
                                clansPrefixList.clear();
                                return true;
                            } else if (args[1].length() > 32) {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-too-long")));
                                clansPrefixList.clear();
                                return true;
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-prefix-too-short")));
                                clansPrefixList.clear();
                                return true;
                            }
                        }else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("must-be-owner-to-change-prefix")));
                            clansPrefixList.clear();
                            return true;
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invalid-prefix")));
                        clansPrefixList.clear();
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("list")) {
                    Set<Map.Entry<UUID, Clan>> clans = ClansStorageUtil.getClans();
                    StringBuilder clansString = new StringBuilder();
                    if (clans.size() == 0) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("no-clans-to-list")));
                    } else {
                        clansString.append(ColorUtils.translateColorCodes(messagesConfig.getString("clans-list-header") + "\n"));
                        clans.forEach((clan) ->
                                clansString.append(ColorUtils.translateColorCodes(clan.getValue().getClanFinalName() + "\n")));
                        clansString.append(" ");
                        clansString.append(ColorUtils.translateColorCodes(messagesConfig.getString("clans-list-footer")));
                        sender.sendMessage(clansString.toString());
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("join")) {
                    StringBuilder inviterUUIDString = new StringBuilder();
                    Set<Map.Entry<UUID, ClanInvite>> clanInvitesList = ClanInviteUtil.getInvites();
                    if (ClanInviteUtil.searchInvitee(player.getUniqueId().toString())) {
                        clanInvitesList.forEach((invites) ->
                                inviterUUIDString.append(invites.getValue().getInviter()));
                        Clan clan = ClansStorageUtil.findClanByOwner(ClanInviteUtil.getInviteOwner(inviterUUIDString.toString()));
                        if (clan != null) {
                            if (ClansStorageUtil.addClanMember(clan, player)) {
                                ClanInviteUtil.removeInvite(inviterUUIDString.toString());
                                String joinMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-join-successful")).replace(CLAN_PLACEHOLDER, clan.getClanFinalName());
                                player.sendMessage(joinMessage);
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

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length == 2) {
                        if (args[1].length() > 1) {
                            Clan targetClan = ClansStorageUtil.findClanByOwner(player);
                            if (ClansStorageUtil.findClanByOwner(player) != null) {
                                Player playerToKick = Bukkit.getPlayer(args[1]);
                                if (playerToKick != null) {
                                    if (!player.getName().equalsIgnoreCase(args[1])){
                                        Clan playerClan = ClansStorageUtil.findClanByPlayer(playerToKick);
                                        if (targetClan.equals(playerClan)) {
                                            targetClan.removeClanMember(playerToKick.getUniqueId().toString());
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
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-kick-yourself")));
                                    }
                                }else {
                                    String playerNotFound = ColorUtils.translateColorCodes(messagesConfig.getString("could-not-find-specified-player")).replace(PLAYER_TO_KICK, args[1]);
                                    player.sendMessage(playerNotFound);
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

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("info")) {
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
                    if (clanByOwner != null) {
                        ArrayList<String> clanMembers = clanByOwner.getClanMembers();
                        ArrayList<String> clanAllies = clanByOwner.getClanAllies();
                        ArrayList<String> clanEnemies = clanByOwner.getClanEnemies();
                        StringBuilder clanInfo = new StringBuilder(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-header"))
                                .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanFinalName()))
                                .replace("%CLANPREFIX%", ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                        UUID clanOwnerUUID = UUID.fromString(clanByOwner.getClanOwner());
                        Player clanOwner = Bukkit.getPlayer(clanOwnerUUID);
                        if (clanOwner != null) {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-owner-online")).replace(OWNER, clanOwner.getName()));
                        }else {
                            UUID uuid = UUID.fromString(clanByOwner.getClanOwner());
                            String offlineOwner = Bukkit.getOfflinePlayer(uuid).getName();
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-owner-offline")).replace(OWNER, offlineOwner));
                        }
                        if (clanMembers.size() > 0) {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-header")));
                            for (String clanMember : clanMembers) {
                                if (clanMember != null) {
                                    UUID memberUUID = UUID.fromString(clanMember);
                                    Player clanPlayer = Bukkit.getPlayer(memberUUID);
                                    if (clanPlayer != null) {
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-online") + "\n").replace(CLAN_MEMBER, clanPlayer.getName()));
                                    } else {
                                        UUID uuid = UUID.fromString(clanMember);
                                        String offlinePlayer = Bukkit.getOfflinePlayer(uuid).getName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-offline") + "\n").replace(CLAN_MEMBER, offlinePlayer));
                                    }
                                }

                            }
                        }
                        if (clanAllies.size() > 0){
                            clanInfo.append(" ");
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-allies-header")));
                            for (String clanAlly : clanAllies){
                                if (clanAlly != null){
                                    Player allyOwner = Bukkit.getPlayer(clanAlly);
                                    if (allyOwner != null){
                                        Clan allyClan = ClansStorageUtil.findClanByOwner(allyOwner);
                                        String clanAllyName = allyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ally-members").replace(ALLY_CLAN, clanAllyName)));
                                    }else {
                                        UUID uuid = UUID.fromString(clanAlly);
                                        OfflinePlayer offlineOwnerPlayer = Bukkit.getOfflinePlayer(uuid);
                                        Clan offlineAllyClan = ClansStorageUtil.findClanByOfflineOwner(offlineOwnerPlayer);
                                        String offlineAllyName = offlineAllyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ally-members").replace(ALLY_CLAN, offlineAllyName)));
                                    }
                                }
                            }
                        }
                        if (clanEnemies.size() > 0){
                            clanInfo.append(" ");
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-enemies-header")));
                            for (String clanEnemy : clanEnemies){
                                if (clanEnemy != null){
                                    Player enemyOwner = Bukkit.getPlayer(clanEnemy);
                                    if (enemyOwner != null){
                                        Clan enemyClan = ClansStorageUtil.findClanByOwner(enemyOwner);
                                        String clanEnemyName = enemyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-enemy-members").replace(ENEMY_CLAN, clanEnemyName)));
                                    }else {
                                        UUID uuid = UUID.fromString(clanEnemy);
                                        OfflinePlayer offlineOwnerPlayer = Bukkit.getOfflinePlayer(uuid);
                                        Clan offlineEnemyClan = ClansStorageUtil.findClanByOfflineOwner(offlineOwnerPlayer);
                                        String offlineEnemyName = offlineEnemyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-enemy-members").replace(ENEMY_CLAN, offlineEnemyName)));
                                    }
                                }
                            }
                        }
                        clanInfo.append(" ");
                        if (clanByOwner.isFriendlyFireAllowed()){
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-pvp-status-enabled")));
                        }else {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-pvp-status-disabled")));
                        }
                        if (ClansStorageUtil.isHomeSet(clanByOwner)){
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-home-set-true")));
                        }else {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-home-set-false")));
                        }
                        clanInfo.append(" ");
                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-footer")));
                        player.sendMessage(clanInfo.toString());

                    }else if (clanByPlayer != null){
                        ArrayList<String> clanMembers = clanByPlayer.getClanMembers();
                        ArrayList<String> clanAllies = clanByPlayer.getClanAllies();
                        ArrayList<String> clanEnemies = clanByPlayer.getClanEnemies();
                        StringBuilder clanInfo = new StringBuilder(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-header"))
                                .replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByPlayer.getClanFinalName()))
                                .replace("%CLANPREFIX%", ColorUtils.translateColorCodes(clanByPlayer.getClanPrefix())));
                        UUID clanOwnerUUID = UUID.fromString(clanByPlayer.getClanOwner());
                        Player clanOwner = Bukkit.getPlayer(clanOwnerUUID);
                        if (clanOwner != null) {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-owner-online")).replace(OWNER, clanOwner.getName()));
                        } else {
                            UUID uuid = UUID.fromString(clanByPlayer.getClanOwner());
                            String offlineOwner = Bukkit.getOfflinePlayer(uuid).getName();
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-owner-offline")).replace(OWNER, offlineOwner));
                        }
                        if (clanMembers.size() > 0) {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-header")));
                            for (String clanMember : clanMembers) {
                                if (clanMember != null) {
                                    UUID memberUUID = UUID.fromString(clanMember);
                                    Player clanPlayer = Bukkit.getPlayer(memberUUID);
                                    if (clanPlayer != null) {
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-online") + "\n").replace(CLAN_MEMBER, clanPlayer.getName()));
                                    } else {
                                        UUID uuid = UUID.fromString(clanMember);
                                        String offlinePlayer = Bukkit.getOfflinePlayer(uuid).getName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-members-offline") + "\n").replace(CLAN_MEMBER, offlinePlayer));
                                    }
                                }

                            }
                        }
                        if (clanAllies.size() > 0){
                            clanInfo.append(" ");
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-allies-header")));
                            for (String clanAlly : clanAllies){
                                if (clanAlly != null){
                                    Player allyOwner = Bukkit.getPlayer(clanAlly);
                                    if (allyOwner != null){
                                        Clan allyClan = ClansStorageUtil.findClanByOwner(allyOwner);
                                        String clanAllyName = allyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ally-members").replace(ALLY_CLAN, clanAllyName)));
                                    }else {
                                        UUID uuid = UUID.fromString(clanAlly);
                                        OfflinePlayer offlineOwnerPlayer = Bukkit.getOfflinePlayer(uuid);
                                        Clan offlineAllyClan = ClansStorageUtil.findClanByOfflineOwner(offlineOwnerPlayer);
                                        String offlineAllyName = offlineAllyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ally-members").replace(ALLY_CLAN, offlineAllyName)));
                                    }
                                }
                            }
                        }
                        if (clanEnemies.size() > 0){
                            clanInfo.append(" ");
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-enemies-header")));
                            for (String clanEnemy : clanEnemies){
                                if (clanEnemy != null){
                                    Player enemyOwner = Bukkit.getPlayer(clanEnemy);
                                    if (enemyOwner != null){
                                        Clan enemyClan = ClansStorageUtil.findClanByOwner(enemyOwner);
                                        String clanEnemyName = enemyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-enemy-members").replace(ENEMY_CLAN, clanEnemyName)));
                                    }else {
                                        UUID uuid = UUID.fromString(clanEnemy);
                                        OfflinePlayer offlineOwnerPlayer = Bukkit.getOfflinePlayer(uuid);
                                        Clan offlineEnemyClan = ClansStorageUtil.findClanByOfflineOwner(offlineOwnerPlayer);
                                        String offlineEnemyName = offlineEnemyClan.getClanFinalName();
                                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-enemy-members").replace(ENEMY_CLAN, offlineEnemyName)));
                                    }
                                }
                            }
                        }
                        clanInfo.append(" ");
                        if (clanByPlayer.isFriendlyFireAllowed()){
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-pvp-status-enabled")));
                        }else {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-pvp-status-disabled")));
                        }
                        if (ClansStorageUtil.isHomeSet(clanByPlayer)){
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-home-set-true")));
                        }else {
                            clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-home-set-false")));
                        }
                        clanInfo.append(" ");
                        clanInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("clan-info-footer")));
                        player.sendMessage(clanInfo.toString());
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("not-in-clan")));
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("leave")) {
                    if (ClansStorageUtil.findClanByOwner(player) != null) {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-clan-owner")));
                        return true;
                    }
                    Clan targetClan = ClansStorageUtil.findClanByPlayer(player);
                    if (targetClan != null) {
                        if (targetClan.removeClanMember(player.getUniqueId().toString())) {
                            String leaveMessage = ColorUtils.translateColorCodes(messagesConfig.getString("clan-leave-successful")).replace(CLAN_PLACEHOLDER, targetClan.getClanFinalName());
                            player.sendMessage(leaveMessage);
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-leave-failed")));
                        }
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("ally")){
                    if (args.length > 2){
                        if (args[1].equalsIgnoreCase("add")){
                            if (args[2].length() > 1){
                                if (ClansStorageUtil.isClanOwner(player)){
                                    if (ClansStorageUtil.findClanByOwner(player) != null) {
                                        Clan clan = ClansStorageUtil.findClanByOwner(player);
                                        Player allyClanOwner = Bukkit.getPlayer(args[2]);
                                        if (allyClanOwner != null){
                                            if (ClansStorageUtil.findClanByOwner(allyClanOwner) != null){
                                                if (ClansStorageUtil.findClanByOwner(player) != ClansStorageUtil.findClanByOwner(allyClanOwner)){
                                                    Clan allyClan = ClansStorageUtil.findClanByOwner(allyClanOwner);
                                                    String allyOwnerUUIDString = allyClan.getClanOwner();
                                                    if (ClansStorageUtil.findClanByOwner(player).getClanAllies().size() >= clansConfig.getInt("max-clan-allies")){
                                                        Integer maxSize = clansConfig.getInt("max-clan-allies");
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ally-max-amount-reached")).replace("%LIMIT%", maxSize.toString()));
                                                        return true;
                                                    }
                                                    if (clan.getClanEnemies().contains(allyOwnerUUIDString)){
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-ally-enemy-clan")));
                                                        return true;
                                                    }
                                                    ClansStorageUtil.addClanAlly(player, allyClanOwner);
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("added-clan-to-your-allies").replace(ALLY_CLAN, allyClan.getClanFinalName())));
                                                    if (allyClanOwner.isOnline()){
                                                        allyClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-added-to-other-allies").replace(CLAN_OWNER, player.getName())));
                                                    }else {
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-to-add-clan-to-allies").replace(ALLY_OWNER, args[2])));
                                                    }
                                                }else {
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-ally-your-own-clan")));
                                                }
                                            }else {
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-player-not-clan-owner").replace(ALLY_OWNER, args[2])));
                                            }
                                        }else {
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("ally-clan-add-owner-offline").replace(ALLY_OWNER, args[2])));
                                        }
                                    }
                                }else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-ally-command-usage")));
                            }
                            return true;
                        }else if (args[1].equalsIgnoreCase("remove")){
                            if (args[2].length() > 1){
                                if (ClansStorageUtil.isClanOwner(player)){
                                    if (ClansStorageUtil.findClanByOwner(player) != null){
                                        Player allyClanOwner = Bukkit.getPlayer(args[2]);
                                        if (allyClanOwner != null){
                                            if (ClansStorageUtil.findClanByOwner(allyClanOwner) != null){
                                                Clan allyClan = ClansStorageUtil.findClanByOwner(allyClanOwner);
                                                List<String> alliedClans = ClansStorageUtil.findClanByOwner(player).getClanAllies();
                                                UUID allyClanOwnerUUID = allyClanOwner.getUniqueId();
                                                String allyClanOwnerString = allyClanOwnerUUID.toString();
                                                if (alliedClans.contains(allyClanOwnerString)){
                                                    ClansStorageUtil.removeClanAlly(player, allyClanOwner);
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("removed-clan-from-your-allies").replace(ALLY_CLAN, allyClan.getClanFinalName())));
                                                    if (allyClanOwner.isOnline()){
                                                        allyClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-removed-from-other-allies").replace(CLAN_OWNER, player.getName())));
                                                    }
                                                }else {
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-to-remove-clan-from-allies").replace(ALLY_OWNER, args[2])));
                                                }
                                            }else {
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-player-not-clan-owner").replace(ALLY_OWNER, args[2])));
                                            }
                                        }else {
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("ally-clan-remove-owner-offline").replace(ALLY_OWNER, args[2])));
                                        }
                                    }
                                }else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-ally-command-usage")));
                            }
                        }
                        return true;
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-ally-command-usage")));
                    }
                    return true;
                }


//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("enemy")){
                    if (args.length > 2){
                        if (args[1].equalsIgnoreCase("add")){
                            if (args[2].length() > 1){
                                if (ClansStorageUtil.isClanOwner(player)){
                                    if (ClansStorageUtil.findClanByOwner(player) != null){
                                        Clan clan = ClansStorageUtil.findClanByOwner(player);
                                        Player enemyClanOwner = Bukkit.getPlayer(args[2]);
                                        if (enemyClanOwner != null){
                                            if (ClansStorageUtil.findClanByOwner(enemyClanOwner) != null){
                                                if (ClansStorageUtil.findClanByOwner(player) != ClansStorageUtil.findClanByOwner(enemyClanOwner)){
                                                    Clan enemyClan = ClansStorageUtil.findClanByOwner(enemyClanOwner);
                                                    String enemyOwnerUUIDString = enemyClan.getClanOwner();
                                                    if (ClansStorageUtil.findClanByOwner(player).getClanEnemies().size() >= clansConfig.getInt("max-clan-enemies")){
                                                        Integer maxSize = clansConfig.getInt("max-clan-enemies");
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-enemy-max-amount-reached")).replace("%LIMIT%", maxSize.toString()));
                                                        return true;
                                                    }
                                                    if (clan.getClanAllies().contains(enemyOwnerUUIDString)){
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-enemy-allied-clan")));
                                                        return true;
                                                    }
                                                    ClansStorageUtil.addClanEnemy(player, enemyClanOwner);
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("added-clan-to-your-enemies").replace(ENEMY_CLAN, enemyClan.getClanFinalName())));
                                                    String titleMain = ColorUtils.translateColorCodes(messagesConfig.getString("added-enemy-clan-to-your-enemies-title-1").replace(CLAN_OWNER, enemyClanOwner.getName()));
                                                    String titleAux = ColorUtils.translateColorCodes(messagesConfig.getString("added-enemy-clan-to-your-enemies-title-2").replace(CLAN_OWNER, enemyClanOwner.getName()));
                                                    player.sendTitle(titleMain, titleAux, 10, 70, 20);
                                                    ArrayList<String> playerClanMembers = ClansStorageUtil.findClanByOwner(player).getClanMembers();
                                                    for (String playerClanMember : playerClanMembers){
                                                        if (playerClanMember != null){
                                                            UUID memberUUID = UUID.fromString(playerClanMember);
                                                            Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                                            if (playerClanPlayer != null){
                                                                playerClanPlayer.sendTitle(titleMain, titleAux, 10, 70, 20);
                                                            }
                                                        }
                                                    }
                                                    if (enemyClanOwner.isOnline()){
                                                        enemyClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-added-to-other-enemies").replace(CLAN_OWNER, player.getName())));
                                                        String titleMainEnemy = ColorUtils.translateColorCodes(messagesConfig.getString("clan-added-to-other-enemies-title-1").replace(CLAN_OWNER, player.getName()));
                                                        String titleAuxEnemy = ColorUtils.translateColorCodes(messagesConfig.getString("clan-added-to-other-enemies-title-2").replace(CLAN_OWNER, player.getName()));
                                                        enemyClanOwner.sendTitle(titleMainEnemy, titleAuxEnemy, 10, 70, 20);
                                                        ArrayList<String> enemyClanMembers = enemyClan.getClanMembers();
                                                        for (String enemyClanMember : enemyClanMembers){
                                                            if (enemyClanMember != null) {
                                                                UUID memberUUID = UUID.fromString(enemyClanMember);
                                                                Player enemyClanPlayer = Bukkit.getPlayer(memberUUID);
                                                                if (enemyClanPlayer != null) {
                                                                    enemyClanPlayer.sendTitle(titleMainEnemy, titleAuxEnemy, 10, 70, 20);
                                                                }
                                                            }
                                                        }
                                                    }else {
                                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-to-add-clan-to-enemies").replace(ENEMY_OWNER, args[2])));
                                                    }
                                                }else {
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-enemy-your-own-clan")));
                                                }
                                            }else {
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-enemy-player-not-clan-owner").replace(ENEMY_OWNER, args[2])));
                                            }
                                        }else {
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("enemy-clan-add-owner-offline").replace(ENEMY_OWNER, args[2])));
                                        }
                                    }
                                }else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-enemy-command-usage")));
                            }
                            return true;
                        }else if (args[1].equalsIgnoreCase("remove")){
                            if (args[2].length() > 1){
                                if (ClansStorageUtil.isClanOwner(player)){
                                    if (ClansStorageUtil.findClanByOwner(player) != null){
                                        Player enemyClanOwner = Bukkit.getPlayer(args[2]);
                                        if (enemyClanOwner != null){
                                            if (ClansStorageUtil.findClanByOwner(enemyClanOwner) != null){
                                                Clan enemyClan = ClansStorageUtil.findClanByOwner(enemyClanOwner);
                                                List<String> enemyClans = ClansStorageUtil.findClanByOwner(player).getClanEnemies();
                                                UUID enemyClanOwnerUUID = enemyClanOwner.getUniqueId();
                                                String enemyClanOwnerString = enemyClanOwnerUUID.toString();
                                                if (enemyClans.contains(enemyClanOwnerString)){
                                                    ClansStorageUtil.removeClanEnemy(player, enemyClanOwner);
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("removed-clan-from-your-enemies").replace(ENEMY_CLAN, enemyClan.getClanFinalName())));
                                                    String titleMain = ColorUtils.translateColorCodes(messagesConfig.getString("removed-enemy-clan-from-your-enemies-title-1").replace(CLAN_OWNER, enemyClanOwner.getName()));
                                                    String titleAux = ColorUtils.translateColorCodes(messagesConfig.getString("removed-enemy-clan-from-your-enemies-title-1").replace(CLAN_OWNER, enemyClanOwner.getName()));
                                                    player.sendTitle(titleMain, titleAux, 10, 70, 20);
                                                    ArrayList<String> playerClanMembers = ClansStorageUtil.findClanByOwner(player).getClanMembers();
                                                    for (String playerClanMember : playerClanMembers){
                                                        if (playerClanMember != null){
                                                            UUID memberUUID = UUID.fromString(playerClanMember);
                                                            Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                                            if (playerClanPlayer != null){
                                                                playerClanPlayer.sendTitle(titleMain, titleAux, 10, 70, 20);
                                                            }
                                                        }
                                                    }
                                                    if (enemyClanOwner.isOnline()){
                                                        enemyClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-removed-from-other-enemies").replace(ENEMY_OWNER, player.getName())));
                                                        String titleMainEnemy = ColorUtils.translateColorCodes(messagesConfig.getString("clan-removed-from-other-enemies-title-1").replace(CLAN_OWNER, player.getName()));
                                                        String titleAuxEnemy = ColorUtils.translateColorCodes(messagesConfig.getString("clan-removed-from-other-enemies-title-2").replace(CLAN_OWNER, player.getName()));
                                                        enemyClanOwner.sendTitle(titleMainEnemy, titleAuxEnemy, 10, 70, 20);
                                                        ArrayList<String> enemyClanMembers = enemyClan.getClanMembers();
                                                        for (String enemyClanMember : enemyClanMembers){
                                                            if (enemyClanMember != null) {
                                                                UUID memberUUID = UUID.fromString(enemyClanMember);
                                                                Player enemyClanPlayer = Bukkit.getPlayer(memberUUID);
                                                                if (enemyClanPlayer != null) {
                                                                    enemyClanPlayer.sendTitle(titleMain, titleAux, 10, 70, 20);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }else {
                                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-to-remove-clan-from-enemies").replace(ENEMY_OWNER, args[2])));
                                                }
                                            }else {
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-enemy-player-not-clan-owner").replace(ENEMY_OWNER, args[2])));
                                            }
                                        }else {
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("enemy-clan-remove-owner-offline").replace(ENEMY_OWNER, args[2])));
                                        }
                                    }
                                }else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-enemy-command-usage")));
                            }
                        }
                        return true;
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-clan-enemy-command-usage")));
                    }
                }


//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("pvp")){
                    if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                        if (ClansStorageUtil.isClanOwner(player)){
                            if (ClansStorageUtil.findClanByOwner(player) != null){
                                Clan clan = ClansStorageUtil.findClanByOwner(player);
                                if (clan.isFriendlyFireAllowed()){
                                    clan.setFriendlyFireAllowed(false);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("disabled-friendly-fire")));
                                }else {
                                    clan.setFriendlyFireAllowed(true);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("enabled-friendly-fire")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-clan")));
                            }
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                    }
                    return true;
                }


//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("sethome")){
                    if (clansConfig.getBoolean("clan-home.enabled")){
                        if (ClansStorageUtil.isClanOwner(player)){
                            if (ClansStorageUtil.findClanByOwner(player) != null){
                                Clan clan = ClansStorageUtil.findClanByOwner(player);
                                clan.setClanHomeWorld(player.getLocation().getWorld().getName());
                                clan.setClanHomeX(player.getLocation().getX());
                                clan.setClanHomeY(player.getLocation().getY());
                                clan.setClanHomeZ(player.getLocation().getZ());
                                clan.setClanHomeYaw(player.getLocation().getYaw());
                                clan.setClanHomePitch(player.getLocation().getPitch());
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-set-clan-home")));
                            }
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("home")){
                    if (clansConfig.getBoolean("clan-home.enabled")){
                        UUID uuid = player.getUniqueId();
                        if (ClansStorageUtil.findClanByOwner(player) != null){
                            Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                            if (clanByOwner.getClanHomeWorld() != null){
                                World world = Bukkit.getWorld(clanByOwner.getClanHomeWorld());
                                double x = clanByOwner.getClanHomeX();
                                double y = clanByOwner.getClanHomeY() + 0.2;
                                double z = clanByOwner.getClanHomeZ();
                                float yaw = clanByOwner.getClanHomeYaw();
                                float pitch = clanByOwner.getClanHomePitch();
                                if (clansConfig.getBoolean("clan-home.cool-down.enabled")){
                                    if (homeCoolDownTimer.containsKey(uuid)){
                                        if (!(player.hasPermission("clanslite.bypass.homecooldown")||player.hasPermission("clanslite.bypass.*")
                                                ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                            if (homeCoolDownTimer.get(uuid) > System.currentTimeMillis()){
                                                Long timeLeft = (homeCoolDownTimer.get(uuid) - System.currentTimeMillis()) / 1000;
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("home-cool-down-timer-wait")
                                                        .replace(TIME_LEFT, timeLeft.toString())));
                                            }else {
                                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                                Location location = new Location(world, x, y, z, yaw, pitch);
                                                player.teleport(location);
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                            }
                                        }else {
                                            Location location = new Location(world, x, y, z, yaw, pitch);
                                            player.teleport(location);
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                        }
                                    }else {
                                        homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                        Location location = new Location(world, x, y, z, yaw, pitch);
                                        player.teleport(location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    player.teleport(location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                            }
                        }else if (ClansStorageUtil.findClanByPlayer(player) != null){
                            Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
                            if (clanByPlayer.getClanHomeWorld() != null){
                                World world = Bukkit.getWorld(clanByPlayer.getClanHomeWorld());
                                double x = clanByPlayer.getClanHomeX();
                                double y = clanByPlayer.getClanHomeY() + 0.2;
                                double z = clanByPlayer.getClanHomeZ();
                                float yaw = clanByPlayer.getClanHomeYaw();
                                float pitch = clanByPlayer.getClanHomePitch();
                                if (clansConfig.getBoolean("clan-home.cool-down.enabled")){
                                    if (homeCoolDownTimer.containsKey(uuid)){
                                        if (!(player.hasPermission("clanslite.bypass.homecooldown")||player.hasPermission("clanslite.bypass.*")
                                                ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                            if (homeCoolDownTimer.get(uuid) > System.currentTimeMillis()){
                                                Long timeLeft = (homeCoolDownTimer.get(uuid) - System.currentTimeMillis()) / 1000;
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("home-cool-down-timer-wait")
                                                        .replace(TIME_LEFT, timeLeft.toString())));
                                            }else {
                                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                                Location location = new Location(world, x, y, z, yaw, pitch);
                                                player.teleport(location);
                                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                            }
                                        }else {
                                            Location location = new Location(world, x, y, z, yaw, pitch);
                                            player.teleport(location);
                                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                        }
                                    }else {
                                        homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                        Location location = new Location(world, x, y, z, yaw, pitch);
                                        player.teleport(location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    player.teleport(location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                            }
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-tp-not-in-clan")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                    }
                    return true;
                }

//----------------------------------------------------------------------------------------------------------------------
                else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-command-usage")));
                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
        }
        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
