package me.loving11ish.clans.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

import java.util.UUID;

public class PlayerConnectionEvent implements Listener {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Clans.connectedPlayers.put(player, player.getName());
        if (!(UsermapStorageUtil.isUserExisting(player))){
            UsermapStorageUtil.addToUsermap(player);
            return;
        }
        if (UsermapStorageUtil.hasPlayerNameChanged(player)){
            UsermapStorageUtil.updatePlayerName(player);
            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated player name"));
            }
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBedrockPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (Clans.getFloodgateApi() != null){
            if (Clans.getFloodgateApi().isFloodgatePlayer(uuid)){
                if (!(UsermapStorageUtil.isUserExisting(player))){
                    UsermapStorageUtil.addBedrockPlayerToUsermap(player);
                    return;
                }
                if (UsermapStorageUtil.hasPlayerNameChanged(player)){
                    UsermapStorageUtil.updatePlayerName(player);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated bedrock player name"));
                    }
                }
                if (UsermapStorageUtil.hasBedrockPlayerJavaUUIDChanged(player)){
                    UsermapStorageUtil.updateBedrockPlayerJavaUUID(player);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated bedrock player Java UUID"));
                    }
                }
                Clans.bedrockPlayers.put(player, Clans.getFloodgateApi().getPlayer(uuid).getJavaUniqueId().toString());
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aAdded bedrock player to connected bedrock players hashmap"));
                }
            }
        }
    }
}
