package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ClanHomeDeleteEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ClanDelHomeSubCommand {
    
    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public boolean deleteClanHomeSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (clansConfig.getBoolean("clan-home.enabled")){
                if (ClansStorageUtil.findClanByOwner(player) != null){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    if (ClansStorageUtil.isHomeSet(clanByOwner)){
                        fireClanHomeDeleteEvent(player, clanByOwner);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeDeleteEvent"));
                        }
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
