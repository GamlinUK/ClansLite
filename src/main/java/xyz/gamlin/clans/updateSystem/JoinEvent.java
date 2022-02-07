package xyz.gamlin.clans.updateSystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ColorUtils;

public class JoinEvent implements Listener {

    private static final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("clanslite.update")||player.hasPermission("clanslite.*")||player.isOp()) {
            if (clansConfig.getBoolean("plugin-update-notifications.enabled")){
                new UpdateChecker(Clans.getPlugin(), 85571).getVersion(version -> {
                    try {
                        if (!(Clans.getPlugin().getDescription().getVersion().equalsIgnoreCase(version))) {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.1")));
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.2")));
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.3")));
                        }
                    }catch (NullPointerException e){
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("Update-check-failure")));
                    }
                });
            }
        }
    }
}
