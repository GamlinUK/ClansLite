package me.loving11ish.clans.externalhooks;

import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class FloodgateAPI {

    private final static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final static FileConfiguration config = Clans.getPlugin().getConfig();

    public static boolean isFloodgateEnabled() {
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound FloodgateApi class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"));
            }
            return true;
        }catch (ClassNotFoundException e) {
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aCould not find FloodgateApi class at:"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"));
            }
            return false;
        }
    }
}
