package xyz.gamlin.clans.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.UsermapStorageUtil;

import java.util.UUID;
import java.util.logging.Logger;

public class PlayerConnectionEvent implements Listener {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    Logger logger = Clans.getPlugin().getLogger();

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
                logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated player name"));
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
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated bedrock player name"));
                    }
                }
                if (UsermapStorageUtil.hasBedrockPlayerJavaUUIDChanged(player)){
                    UsermapStorageUtil.updateBedrockPlayerJavaUUID(player);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aUpdated bedrock player Java UUID"));
                    }
                }
                Clans.bedrockPlayers.put(player, Clans.getFloodgateApi().getPlayer(uuid).getJavaUniqueId().toString());
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                    logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aAdded bedrock player to connected bedrock players hashmap"));
                }
            }
        }
    }
}
