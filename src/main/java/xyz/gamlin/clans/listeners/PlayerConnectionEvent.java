package xyz.gamlin.clans.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.gamlin.clans.Clans;

public class PlayerConnectionEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Clans.connectedPlayers.put(player, player.getName());
    }
}
