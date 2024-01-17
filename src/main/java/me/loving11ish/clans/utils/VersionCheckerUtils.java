package me.loving11ish.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.regex.PatternSyntaxException;

public class VersionCheckerUtils {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final String serverPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private int version;

    public void setVersion() {
        try {
            version = Integer.parseInt(serverPackage.split("_")[1]);
        }catch (NumberFormatException | PatternSyntaxException e){
            console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Unable to process server version!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Some features may break unexpectedly!"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Report any issues to the developer!"));
            console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
        }
    }

    public String getServerPackage() {
        return serverPackage;
    }

    public int getVersion() {
        return version;
    }
}
