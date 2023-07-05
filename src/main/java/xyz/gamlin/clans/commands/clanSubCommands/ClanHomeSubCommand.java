package xyz.gamlin.clans.commands.clanSubCommands;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.api.ClanHomePreTeleportEvent;
import xyz.gamlin.clans.api.ClanHomeTeleportEvent;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class ClanHomeSubCommand {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    Logger logger = Clans.getPlugin().getLogger();
    private static final String TIME_LEFT = "%TIMELEFT%";

    private static ClanHomePreTeleportEvent homePreTeleportEvent = null;

    HashMap<UUID, Long> homeCoolDownTimer = new HashMap<>();

    public boolean tpClanHomeSubCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            if (clansConfig.getBoolean("clan-home.enabled")){
                UUID uuid = player.getUniqueId();
                if (ClansStorageUtil.findClanByOwner(player) != null){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    if (clanByOwner.getClanHomeWorld() != null){
                        fireClanHomePreTPEvent(player, clanByOwner);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomePreTPEvent"));
                        }
                        if (homePreTeleportEvent.isCancelled()){
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aClanHomePreTPEvent cancelled by external source"));
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
                                        fireClanHomeTeleportEvent(player, clanByOwner, location, player.getLocation());
                                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                            logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                        }
                                        PaperLib.teleportAsync(player, location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    fireClanHomeTeleportEvent(player, clanByOwner, location, player.getLocation());
                                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                    }
                                    PaperLib.teleportAsync(player, location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                fireClanHomeTeleportEvent(player, clanByOwner, location, player.getLocation());
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                    logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                }
                                PaperLib.teleportAsync(player, location);
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            fireClanHomeTeleportEvent(player, clanByOwner, location, player.getLocation());
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                            }
                            PaperLib.teleportAsync(player, location);
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                    }
                }else if (ClansStorageUtil.findClanByPlayer(player) != null){
                    Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
                    fireClanHomePreTPEvent(player, clanByPlayer);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomePreTPEvent"));
                    }
                    if (homePreTeleportEvent.isCancelled()){
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aClanHomePreTPEvent cancelled by external source"));
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
                                        fireClanHomeTeleportEvent(player, clanByPlayer, location, player.getLocation());
                                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                            logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                        }
                                        PaperLib.teleportAsync(player, location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    fireClanHomeTeleportEvent(player, clanByPlayer, location, player.getLocation());
                                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                    }
                                    PaperLib.teleportAsync(player, location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                fireClanHomeTeleportEvent(player, clanByPlayer, location, player.getLocation());
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                    logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                                }
                                PaperLib.teleportAsync(player, location);
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            fireClanHomeTeleportEvent(player, clanByPlayer, location, player.getLocation());
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanHomeTeleportEvent"));
                            }
                            PaperLib.teleportAsync(player, location);
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
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

    private static void fireClanHomeTeleportEvent(Player player, Clan clan, Location homeLocation, Location tpFromLocation) {
        ClanHomeTeleportEvent clanHomeTeleportEvent = new ClanHomeTeleportEvent(player, clan, homeLocation, tpFromLocation);
        Bukkit.getPluginManager().callEvent(clanHomeTeleportEvent);
    }
}
