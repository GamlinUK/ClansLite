package me.loving11ish.clans.listeners;

import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.websocketutils.MojangAPIRequestUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.IOException;
import java.util.UUID;

public class PlayerPreConnectionEvent implements Listener {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    private boolean firstPlayerConnected = true;

    @EventHandler
    public void onPlayerPreConnect(AsyncPlayerPreLoginEvent event) {
        if (firstPlayerConnected){
            try {
                UUID uuid = event.getUniqueId();
                if (MojangAPIRequestUtils.canGetOfflinePlayerData(uuid.toString(), event.getName())){
                    Clans.setOnlineMode(true);
                    if (Clans.getVersionCheckerUtils().getVersion() >= 14){
                        Clans.setGUIEnabled(clansConfig.getBoolean("use-global-GUI-system"));
                        Clans.setChestsEnabled(clansConfig.getBoolean("protections.chests.enabled"));
                        if (Clans.isGUIEnabled()){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Global GUI system enabled!"));
                        }else {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
                        }
                        if (Clans.isChestsEnabled()){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &3Chest protection system enabled!"));
                        }else {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
                        }
                    }else {
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cYour current server version does not support PersistentDataContainers!"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
                    }
                }else if (!MojangAPIRequestUtils.canGetOfflinePlayerData(uuid.toString(), event.getName())){
                    Clans.setOnlineMode(false);
                    if (Clans.getVersionCheckerUtils().getVersion() >= 14){
                        Clans.setGUIEnabled(false);
                        Clans.setChestsEnabled(false);
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
                    }else {
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &cYour current server version does not support PersistentDataContainers!"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lChest protection system disabled!"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &c&lGlobal GUI system disabled!"));
                    }
                    console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4This plugin is only supported on online servers or servers running in an online network situation!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Please set &e'online-mode=true' &4in &e'server.properties'"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Or ensure your proxy setup is correct and your proxy is set to online mode!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4&lNO SUPPORT WILL BE GIVEN UNLESS THE ABOVE IS CHANGED/SETUP CORRECTLY!"));
                    console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
                }
            }catch (IOException e){
                console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Unable to reach Mojang player database!"));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4See stacktrace below for more details."));
                e.printStackTrace();
                console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            }
            firstPlayerConnected = false;
        }
    }
}
