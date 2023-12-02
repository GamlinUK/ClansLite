package me.loving11ish.clans.websocketutils;

import com.github.lightlibs.simplehttpwrapper.SimpleHttpResponse;
import com.github.lightlibs.simplehttpwrapper.SimpleHttpWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class MojangAPIRequestUtils {

    private final static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final static FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    public static boolean canGetOfflinePlayerData(String uuid, String playerName) throws IOException {
        SimpleHttpResponse response = SimpleHttpWrapper.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid, null);
        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &ahttps request response code: &e" + response.getStatusCode()));
        }
        if (response.getStatusCode()/100 == 4 || response.getStatusCode() == 204){
            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUnable to get offlinePlayerData"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aServer/network is running offline"));
            }
            return false;
        }else {
            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aSuccessfully got offlinePlayerData for :&e" + uuid));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aServer/network is running online"));
            }
            JsonObject object = (JsonObject) JsonParser.parseString(response.getData());
            return object.get("name").getAsString().equalsIgnoreCase(playerName);
        }
    }
}
