package me.loving11ish.clans.externalhooks;

import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class PlugManXAPI {

    private final static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final static FileConfiguration config = Clans.getPlugin().getConfig();

    public static boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound PlugManX main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dcom.rylinaux.plugman.PlugMan"));
            }
            return true;
        }catch (ClassNotFoundException e) {
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find PlugManX main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dcom.rylinaux.plugman.PlugMan"));
            }
            return false;
        }
    }
}
