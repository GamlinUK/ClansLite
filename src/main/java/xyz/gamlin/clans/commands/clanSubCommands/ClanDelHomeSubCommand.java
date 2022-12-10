package xyz.gamlin.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.api.events.ClanHomeDeleteEvent;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

public class ClanDelHomeSubCommand {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean deleteClanHomeSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (clansConfig.getBoolean("clan-home.enabled")){
                if (ClansStorageUtil.findClanByOwner(player) != null){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    if (ClansStorageUtil.isHomeSet(clanByOwner)){
                        fireClanHomeDeleteEvent(player, clanByOwner);
                        ClansStorageUtil.deleteHome(clanByOwner);
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-deleted-clan-home")));
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
            }
            return true;

        }
        return false;
    }

    private static void fireClanHomeDeleteEvent(Player player, Clan clan) {
        ClanHomeDeleteEvent clanHomeDeleteEvent = new ClanHomeDeleteEvent(player, clan);
        Bukkit.getPluginManager().callEvent(clanHomeDeleteEvent);
    }
}
