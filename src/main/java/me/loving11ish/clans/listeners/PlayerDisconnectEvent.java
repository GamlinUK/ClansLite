package me.loving11ish.clans.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;

public class PlayerDisconnectEvent implements Listener {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Clans.connectedPlayers.remove(player);
        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aPlayer removed from connected players list"));
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBedrockPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (Clans.getFloodgateApi() != null){
            if (Clans.bedrockPlayers.containsKey(player)){
                Clans.bedrockPlayers.remove(player);
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aBedrock player removed from bedrock players list"));
                }
            }
        }
    }
}
