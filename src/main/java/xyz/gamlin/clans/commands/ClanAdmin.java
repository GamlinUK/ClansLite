package xyz.gamlin.clans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;

import java.io.IOException;

public class ClanAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Clans plugin = Clans.getPlugin();
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage("§6ClansLite: Saving clans data...");
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("§6ClansLite: Clans saved!");
                }
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("The clan command must be run in game!");
        }

        return true;
    }
}
