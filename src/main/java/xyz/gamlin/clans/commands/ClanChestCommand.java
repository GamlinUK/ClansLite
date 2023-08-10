package xyz.gamlin.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.commands.clanChestLockSubCommands.ChestAccessListSubCommand;
import xyz.gamlin.clans.commands.clanChestLockSubCommands.ChestBuySubCommand;
import xyz.gamlin.clans.commands.clanChestLockSubCommands.ChestLockSubCommand;
import xyz.gamlin.clans.commands.clanChestLockSubCommands.ChestUnlockSubCommand;
import xyz.gamlin.clans.utils.ColorUtils;

public class ClanChestCommand implements CommandExecutor {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            if (!clansConfig.getBoolean("protections.chests.enabled")){
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }
            if (args.length < 1){
                sendUsage(player);
                return true;
            }else {
                switch (args[0]){
                    case "lock":
                        return new ChestLockSubCommand().clanChestLockSubCommand(sender);
                    case "unlock":
                        return new ChestUnlockSubCommand().chestUnlockSubCommand(sender);
                    case "buylock":
                        return new ChestBuySubCommand().chestBuySubCommand(sender, args);
                    case "accesslist":
                        return new ChestAccessListSubCommand().chestAccessListSubCommand(sender);
                    default:
                        sendUsage(player);
                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            if (!clansConfig.getBoolean("protections.chests.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
        }
        return true;
    }

    private void sendUsage(Player player){
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-1")));
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-2")));
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-3")));
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-4")));
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-5")));
    }
}
