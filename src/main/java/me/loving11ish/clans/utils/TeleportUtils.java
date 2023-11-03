package me.loving11ish.clans.utils;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ClanHomeTeleportEvent;
import me.loving11ish.clans.models.Clan;

import java.util.concurrent.TimeUnit;

public class TeleportUtils {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private FoliaLib foliaLib = Clans.getFoliaLib();

    public WrappedTask wrappedTask;

    FileConfiguration config = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public void teleportAsync(Player player, Clan clan, Location location) {
        Location originLocation = player.getLocation();
        PaperLib.teleportAsync(player, location);
        fireClanHomeTeleportEvent(false, player, clan, originLocation, location);
        if (config.getBoolean("general.developer-debug-mode.enabled")){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent in sync mode"));
        }
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("non-timed-teleporting-complete")));
    }

    public void teleportAsyncTimed(Player player, Clan clan, Location location) {
        Location originLocation = player.getLocation();
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("timed-teleporting-begin-tp")));
        wrappedTask = foliaLib.getImpl().runTimerAsync(new Runnable() {
            int time = config.getInt("clan-home.delay-before-teleport.time");
            @Override
            public void run() {
                if (!Clans.getPlugin().teleportQueue.containsKey(player.getUniqueId())){
                    Clans.getPlugin().teleportQueue.put(player.getUniqueId(), getWrappedTask());
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aPlayer "  + player.getName() + " has been added to teleport queue"));
                    }
                }
                if (time == 0) {
                    Clans.getPlugin().teleportQueue.remove(player.getUniqueId());
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aPlayer "  + player.getName() + " has been removed from the teleport queue"));
                    }
                    PaperLib.teleportAsync(player, location);
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("timed-teleporting-complete")));
                    if (foliaLib.isFolia()){
                        fireClanHomeTeleportEvent(true, player, clan, originLocation, location);
                        if (config.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aDetected running on Folia"));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                        }
                    }else {
                        fireClanHomeTeleportEvent(true, player, clan, originLocation, location);
                        if (config.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aDetected not running on Folia"));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                        }
                    }
                    getWrappedTask().cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + getWrappedTask().toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &ateleportPlayerAsyncTimed task canceled"));
                    }
                    return;
                }else {
                    time --;
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &ateleportPlayerAsyncTimed task running"));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + getWrappedTask().toString()));
                    }
                }
            }
        }, 0, 1L, TimeUnit.SECONDS);
    }

    public WrappedTask getWrappedTask() {
        return wrappedTask;
    }

    private void fireClanHomeTeleportEvent(boolean isAsync, Player player, Clan clan, Location originLocation, Location homeLocation) {
        ClanHomeTeleportEvent homeTeleportEvent = new ClanHomeTeleportEvent(isAsync, player, clan, originLocation, homeLocation);
        Bukkit.getPluginManager().callEvent(homeTeleportEvent);
    }
}
