package xyz.gamlin.clans.commands;

import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.ClanPlayer;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.UsermapStorageUtil;

import java.util.*;
import java.util.logging.Logger;

public class PlayerPointsCommand implements CommandExecutor, TabCompleter {

    Logger logger = Clans.getPlugin().getLogger();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String POINT_PLACEHOLDER = "%POINTVALUE%";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!clansConfig.getBoolean("points.player-points.enabled")){
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("listall")){
                    if (player.hasPermission("clanslite.points.listall")||player.hasPermission("clanslite.admin")
                            ||player.hasPermission("clanslite.*")||player.isOp()){
                        return listAllPoints(sender);
                    }
                }
            }else {
                ClanPlayer clanPlayer = UsermapStorageUtil.getClanPlayerByBukkitPlayer(player);
                if (clanPlayer != null){
                    int playerPointValue = clanPlayer.getPointBalance();
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-list-command")
                            .replace(POINT_PLACEHOLDER, String.valueOf(playerPointValue))));
                }
            }
        }

        else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("listall")){
                    return listAllPoints(sender);
                }
            }else {
                logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
            }
        }
        return true;
    }

    private boolean listAllPoints(CommandSender sender){
        HashMap<UUID, ClanPlayer> allUsersMap = new HashMap<>(UsermapStorageUtil.getUsermap());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ColorUtils.translateColorCodes(messagesConfig.getString("all-points-list-header")));
        for (Map.Entry<UUID, ClanPlayer> entry : allUsersMap.entrySet()){
            String clanPlayerName = entry.getValue().getLastPlayerName();
            String clanPlayerPointBalance = String.valueOf(entry.getValue().getPointBalance());
            stringBuilder.append(ColorUtils.translateColorCodes(messagesConfig.getString("all-points-list-entry")
                    .replace("%PLAYER%", clanPlayerName).replace(POINT_PLACEHOLDER, clanPlayerPointBalance)));
        }
        stringBuilder.append(ColorUtils.translateColorCodes(messagesConfig.getString("all-points-list-footer")));
        sender.sendMessage(ColorUtils.translateColorCodes(stringBuilder.toString()));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("clanslite.points.listall")||player.hasPermission("clanslite.admin")
                    ||player.hasPermission("clanslite.*")||player.isOp()){
                arguments.add("listall");
            }
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        }
        return null;
    }
}
