package me.loving11ish.clans.listeners;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;

import java.util.UUID;

public class PlayerMovementEvent implements Listener {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration config = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!config.getBoolean("clan-home.delay-before-teleport.cancel-teleport-on-move")){
            return;
        }
        if (event.getFrom().getX() != event.getTo().getX()
                ||event.getFrom().getY() != event.getTo().getY()
                ||event.getFrom().getZ() != event.getTo().getZ()){
            if (Clans.getPlugin().teleportQueue.containsKey(uuid)){
                if (config.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aPlayer "  + player.getName() + " has a pending teleport"));
                }
                try {
                    WrappedTask wrappedTask = Clans.getPlugin().teleportQueue.get(uuid);
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + wrappedTask.toString()));
                    }
                    wrappedTask.cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task canceled"));
                    }
                    Clans.getPlugin().teleportQueue.remove(uuid);
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aPlayer " + player.getName() + " has had teleport canceled and removed from queue"));
                    }
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("timed-teleport-failed-player-moved")));

                }catch (Exception e){
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("move-event-cancel-failed")));
                    e.printStackTrace();
                }
            }
        }
    }
}
