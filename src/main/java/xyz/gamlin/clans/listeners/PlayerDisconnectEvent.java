package xyz.gamlin.clans.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.gamlin.clans.Clans;

public class PlayerDisconnectEvent implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Clans.connectedPlayers.remove(player);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBedrockPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (Clans.getFloodgateApi() != null){
            if (Clans.bedrockPlayers.containsKey(player)){
                Clans.bedrockPlayers.remove(player);
            }
        }
    }
}
