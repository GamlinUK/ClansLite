package xyz.gamlin.clans.commands;

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
import java.util.logging.Logger;

public class ClanCommand implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();

    public static int taskID1;

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static List<String> bannedTags;

    public static void updateBannedTagsList(){
        taskID1 = Bukkit.getScheduler().scheduleSyncDelayedTask(Clans.getPlugin(),
                () -> bannedTags = Clans.getPlugin().getConfig().getStringList("clan-tags.disallowed-tags"), 10);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length < 1) {
                if (clansConfig.getBoolean("use-global-GUI-system")) {
                    new ClanListGUI(Clans.getPlayerMenuUtility(player)).open();
                    return true;
                }
                if (clansConfig.getBoolean("clan-home.enabled") && clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-14")));
                    return true;
                }else if (clansConfig.getBoolean("clan-home.enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-14")));
                    return true;
                }else if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-13")));
                    return true;
                }else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-1")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-2")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-3")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-4")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-5")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-6")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-7")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-8")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-9")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-10")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-11")));
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-command-incorrect-usage.line-12")));
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
                    default:
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
