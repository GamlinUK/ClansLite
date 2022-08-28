package xyz.gamlin.clans.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.gamlin.clans.Clans;

public class PlayerDisconnectEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Clans.connectedPlayers.remove(player);
    }
}
