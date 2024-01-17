package me.loving11ish.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.commands.clanChestLockSubCommands.ChestAccessListSubCommand;
import me.loving11ish.clans.commands.clanChestLockSubCommands.ChestBuySubCommand;
import me.loving11ish.clans.commands.clanChestLockSubCommands.ChestLockSubCommand;
import me.loving11ish.clans.commands.clanChestLockSubCommands.ChestUnlockSubCommand;
import me.loving11ish.clans.utils.ColorUtils;

import java.util.List;

public class ClanChestCommand implements CommandExecutor {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!Clans.isChestsEnabled()){
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }
            if (args.length < 1){
                player.sendMessage(ColorUtils.translateColorCodes(sendUsage()));
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
                        player.sendMessage(ColorUtils.translateColorCodes(sendUsage()));
                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender instanceof ConsoleCommandSender) {
            if (!Clans.isChestsEnabled()){
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
        }
        return true;
    }

    private String sendUsage() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> configStringList = messagesConfig.getStringList("chest-command-incorrect-usage");
        for (String string : configStringList){
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
