package xyz.gamlin.clans.commands.SubCommands.Clan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.HashMap;
import java.util.UUID;

public class ClanHomeSubCommand {
    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private static final String TIME_LEFT = "%TIMELEFT%";
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    HashMap<UUID, Long> homeCoolDownTimer = new HashMap<>();
    public boolean tpClanHomeSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (clansConfig.getBoolean("clan-home.enabled")){
                UUID uuid = player.getUniqueId();
                if (ClansStorageUtil.findClanByOwner(player) != null){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    if (clanByOwner.getClanHomeWorld() != null){
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
                                        player.teleport(location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    player.teleport(location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                player.teleport(location);
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            player.teleport(location);
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")));
                    }
                }else if (ClansStorageUtil.findClanByPlayer(player) != null){
                    Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
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
                                        player.teleport(location);
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                    }
                                }else {
                                    Location location = new Location(world, x, y, z, yaw, pitch);
                                    player.teleport(location);
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                                }
                            }else {
                                homeCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-home.cool-down.time") * 1000));
                                Location location = new Location(world, x, y, z, yaw, pitch);
                                player.teleport(location);
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-teleported-to-home")));
                            }
                        }else {
                            Location location = new Location(world, x, y, z, yaw, pitch);
                            player.teleport(location);
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
}
