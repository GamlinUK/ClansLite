package xyz.gamlin.clans.commands;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.commands.clanSubCommands.*;
import xyz.gamlin.clans.menuSystem.paginatedMenu.ClanListGUI;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClanCommand implements CommandExecutor {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static WrappedTask task1;

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static List<String> bannedTags;

    public static void updateBannedTagsList(){
        FoliaLib foliaLib = new FoliaLib(Clans.getPlugin());
        task1 = foliaLib.getImpl().runLaterAsync(() ->
                bannedTags = Clans.getPlugin().getConfig().getStringList("clan-tags.disallowed-tags"), 1L, TimeUnit.SECONDS);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 1) {
                if (clansConfig.getBoolean("use-global-GUI-system")) {
                    new ClanListGUI(Clans.getPlayerMenuUtility(player)).open();
                    return true;
                }
                if (clansConfig.getBoolean("clan-home.enabled") && clansConfig.getBoolean("protections.pvp.pvp-command-enabled")
                        && clansConfig.getBoolean("points.player-points.enabled")){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-14")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-15")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-16")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-17")));
                    return true;
                }else if (clansConfig.getBoolean("clan-home.enabled")){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-15")));
                    return true;
                }else if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-14")));
                    return true;
                }else if (clansConfig.getBoolean("points.player-points.enabled")){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-16")));
                    return true;
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                }
                return true;
            }else {
                switch(args[0]) {
                    case "create":
                        return new ClanCreateSubCommand().createClanSubCommand(sender, args, bannedTags);
                    case "disband":
                        return new ClanDisbandSubCommand().disbandClanSubCommand(sender);
                    case "invite":
                        return new ClanInviteSubCommand().clanInviteSubCommand(sender, args);
                    case "prefix":
                        return new ClanPrefixSubCommand().clanPrefixSubCommand(sender, args, bannedTags);
                    case "transfer":
                        return new ClanTransferOwnerSubCommand().transferClanOwnerSubCommand(sender, args);
                    case "list":
                        return new ClanListSubCommand().clanListSubCommand(sender);
                    case "join":
                        return new ClanJoinSubCommand().clanJoinSubCommand(sender);
                    case "kick":
                        return new ClanKickSubCommand().clanKickSubCommand(sender, args);
                    case "info":
                        return new ClanInfoSubCommand().clanInfoSubCommand(sender);
                    case "leave":
                        return new ClanLeaveSubCommand().clanLeaveSubCommand(sender);
                    case "ally":
                        return new ClanAllySubCommand().clanAllySubCommand(sender, args);
                    case "enemy":
                        return new ClanEnemySubCommand().clanEnemySubCommand(sender, args);
                    case "pvp":
                        return new ClanPvpSubCommand().clanPvpSubCommand(sender);
                    case "sethome":
                        return new ClanSetHomeSubCommand().setClanHomeSubCommand(sender);
                    case "delhome":
                        return new ClanDelHomeSubCommand().deleteClanHomeSubCommand(sender);
                    case "home":
                        return new ClanHomeSubCommand().tpClanHomeSubCommand(sender);
                    case "points":
                        return new ClanPointSubCommand().clanPointSubCommand(sender, args);
                    case "playerpoints":
                        return new ClanPlayerPointsSubCommand().clanPlayerPointsSubCommand(sender, args);
                    default:
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-command-usage")));

                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
        }
        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
