package me.loving11ish.clans.externalhooks;

import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class PlaceholderAPI {

    private final static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final static FileConfiguration config = Clans.getPlugin().getConfig();

    public static boolean isPlaceholderAPIEnabled() {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin");
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound PlaceholderAPI main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"));
            }
            return true;
        }catch (ClassNotFoundException e){
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find PlaceholderAPI main class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"));
            }
            return false;
        }
    }
}
