package me.loving11ish.clans.commands.clanSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ClanHomePreTeleportEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.TeleportUtils;

import java.util.HashMap;
import java.util.UUID;

public class ClanHomeSubCommand {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    
    private static final String TIME_LEFT = "%TIMELEFT%";

    private static ClanHomePreTeleportEvent homePreTeleportEvent = null;

    HashMap<UUID, Long> homeCoolDownTimer = new HashMap<>();

    public boolean tpClanHomeSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (clansConfig.getBoolean("clan-home.enabled")){
                UUID uuid = player.getUniqueId();
                if (ClansStorageUtil.findClanByOwner(player) != null){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    if (clanByOwner.getClanHomeWorld() != null){
                        fireClanHomePreTPEvent(player, clanByOwner);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomePreTPEvent"));
                        }
                        if (homePreTeleportEvent.isCancelled()){
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aClanHomePreTPEvent cancelled by external source"));
                            }
                            return true;
                        }
                        World world = Bukkit.getWorld(clanByOwner.getClanHomeWorld());
                        double x = clanByOwner.getClanHomeX();
                        double y = clanByOwner.getClanHomeY() + 0.2;
                        double z = clanByOwner.getClanHomeZ();
                        float yaw = clanByOwner.getClanHomeYaw();
                        float pitch = clanByOwner.getClanHomePitch();
                        if (clansConfig.getBoolean("clan-home.cool-down.enabled")){
                            if (homeCoolDownTimer.containsKey(uuid)){
                                if (!(player.hasPermission("clanslite.bypass.homecooldown")||player.hasPermission("clanslite.bypass.*")
                                        ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                    if (homeCoolDownTimer.get(uuid) > System.currentTimeMillis()){
                                        long timeLeft = (homeCoolDownTimer.get(uuid) - System.currentTimeMillis()) / 1000;
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("home-cool-down-timer-wait")
                                                .replace(TIME_LEFT, Long.toString(timeLeft))));
                                    }else {
                                        homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                        Location location = new Location(world, x, y, z, yaw, pitch);
                                        if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                            if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                                    ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                                TeleportUtils teleportUtils = new TeleportUtils();
                                                teleportUtils.teleportAsyncTimed(player, clanByOwner, location);
                                            }else {
                                                TeleportUtils teleportUtils = new TeleportUtils();
                                                teleportUtils.teleportAsync(player, clanByOwner, location);
                                            }
                                        }else {
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsync(player, clanByOwner, location);
                                        }
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                        if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                                ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsyncTimed(player, clanByOwner, location);
                                        }else {
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsync(player, clanByOwner, location);
                                        }
                                    }else {
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsync(player, clanByOwner, location);
                                    }
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                    if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                            ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsyncTimed(player, clanByOwner, location);
                                    }else {
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsync(player, clanByOwner, location);
                                    }
                                }else {
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsync(player, clanByOwner, location);
                                }
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                        ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsyncTimed(player, clanByOwner, location);
                                }else {
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsync(player, clanByOwner, location);
                                }
                            }else {
                                TeleportUtils teleportUtils = new TeleportUtils();
                                teleportUtils.teleportAsync(player, clanByOwner, location);
                            }
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                    }
                }else if (ClansStorageUtil.findClanByPlayer(player) != null){
                    Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
                    fireClanHomePreTPEvent(player, clanByPlayer);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomePreTPEvent"));
                    }
                    if (homePreTeleportEvent.isCancelled()){
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aClanHomePreTPEvent cancelled by external source"));
                        }
                        return true;
                    }
                    if (clanByPlayer.getClanHomeWorld() != null){
                        World world = Bukkit.getWorld(clanByPlayer.getClanHomeWorld());
                        double x = clanByPlayer.getClanHomeX();
                        double y = clanByPlayer.getClanHomeY() + 0.2;
                        double z = clanByPlayer.getClanHomeZ();
                        float yaw = clanByPlayer.getClanHomeYaw();
                        float pitch = clanByPlayer.getClanHomePitch();
                        if (clansConfig.getBoolean("clan-home.cool-down.enabled")){
                            if (homeCoolDownTimer.containsKey(uuid)){
                                if (!(player.hasPermission("clanslite.bypass.homecooldown")||player.hasPermission("clanslite.bypass.*")
                                        ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                    if (homeCoolDownTimer.get(uuid) > System.currentTimeMillis()){
                                        long timeLeft = (homeCoolDownTimer.get(uuid) - System.currentTimeMillis()) / 1000;
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("home-cool-down-timer-wait")
                                                .replace(TIME_LEFT, Long.toString(timeLeft))));
                                    }else {
                                        homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                        Location location = new Location(world, x, y, z, yaw, pitch);
                                        if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                            if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                                    ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                                TeleportUtils teleportUtils = new TeleportUtils();
                                                teleportUtils.teleportAsyncTimed(player, clanByPlayer, location);
                                            }else {
                                                TeleportUtils teleportUtils = new TeleportUtils();
                                                teleportUtils.teleportAsync(player, clanByPlayer, location);
                                            }
                                        }else {
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsync(player, clanByPlayer, location);
                                        }
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                        if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                                ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsyncTimed(player, clanByPlayer, location);
                                        }else {
                                            TeleportUtils teleportUtils = new TeleportUtils();
                                            teleportUtils.teleportAsync(player, clanByPlayer, location);
                                        }
                                    }else {
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsync(player, clanByPlayer, location);
                                    }
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                    if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                            ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsyncTimed(player, clanByPlayer, location);
                                    }else {
                                        TeleportUtils teleportUtils = new TeleportUtils();
                                        teleportUtils.teleportAsync(player, clanByPlayer, location);
                                    }
                                }else {
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsync(player, clanByPlayer, location);
                                }
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            if (clansConfig.getBoolean("clan-home.delay-before-teleport.enabled")){
                                if (!(player.hasPermission("clanslite.bypass.homedelay")||player.hasPermission("clanslite.bypass.*")
                                        ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsyncTimed(player, clanByPlayer, location);
                                }else {
                                    TeleportUtils teleportUtils = new TeleportUtils();
                                    teleportUtils.teleportAsync(player, clanByPlayer, location);
                                }
                            }else {
                                TeleportUtils teleportUtils = new TeleportUtils();
                                teleportUtils.teleportAsync(player, clanByPlayer, location);
                            }
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-tp-not-in-clan")));
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
            }
            return true;

        }
        return false;
    }

    private static void fireClanHomePreTPEvent(Player player, Clan clan) {
        ClanHomePreTeleportEvent clanHomePreTeleportEvent = new ClanHomePreTeleportEvent(player, clan);
        Bukkit.getPluginManager().callEvent(clanHomePreTeleportEvent);
        homePreTeleportEvent = clanHomePreTeleportEvent;
    }
}
