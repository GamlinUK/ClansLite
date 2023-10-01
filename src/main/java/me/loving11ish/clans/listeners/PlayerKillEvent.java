package me.loving11ish.clans.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.Nullable;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.PlayerPointsAwardedEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

public class PlayerKillEvent implements Listener {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private Integer nonEnemyPointValue = clansConfig.getInt("points.player-points.non-enemy-clan-point-amount-on-kill");
    private Integer enemyPointValue = clansConfig.getInt("points.player-points.enemy-clan-point-amount-on-kill");

    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent event){
        if (!clansConfig.getBoolean("points.player-points.enabled")){
            return;
        }
        if (event.getDamager() instanceof Player){
            Player killer = (Player) event.getDamager();
            if (event.getEntity() instanceof Player){
                Player victim = (Player) event.getEntity();
                if (victim.getLastDamage() >= victim.getHealth()){

                    if (ClansStorageUtil.findClanByPlayer(killer) == null||ClansStorageUtil.findClanByPlayer(victim) == null){
                        killer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                .replace("%PLAYER%", victim.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                        if (clansConfig.getBoolean("points.player-points.take-points-from-victim")){
                            if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)){
                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                        .replace("%KILLER%", killer.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                            }else {
                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")));
                                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                        .replace("%VICTIM%", victim.getName())));
                            }
                        }
                        UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue);
                        firePlayerPointsAwardedEvent(killer, killer, victim, null, null, nonEnemyPointValue, false);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"));
                        }
                    }

                    else if (ClansStorageUtil.findClanByOwner(killer) != null || ClansStorageUtil.findClanByOwner(victim) != null){
                        Clan killerClanOwner = ClansStorageUtil.findClanByOwner(killer);
                        Clan victimClanOwner = ClansStorageUtil.findClanByOwner(victim);
                        if (killerClanOwner != null||victimClanOwner != null){
                            if (killerClanOwner.getClanEnemies().contains(victimClanOwner.getClanOwner())||victimClanOwner.getClanEnemies().contains(killerClanOwner.getClanOwner())) {
                                killer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-killer-enemy-received-success")
                                        .replace("%PLAYER%", victim.getName()).replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())));
                                if (clansConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                    if (UsermapStorageUtil.withdrawPoints(victim, enemyPointValue)) {
                                        victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-enemy-withdrawn-success")
                                                .replace("%KILLER%", killer.getName()).replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())));
                                    } else {
                                        victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")));
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                .replace("%VICTIM%", victim.getName())));
                                    }
                                }
                                UsermapStorageUtil.addPointsToOnlinePlayer(killer, enemyPointValue);
                                firePlayerPointsAwardedEvent(killer, killer, victim, killerClanOwner, victimClanOwner, enemyPointValue, true);
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"));
                                }
                            }
                        }else {
                            killer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                    .replace("%PLAYER%", victim.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                            if (clansConfig.getBoolean("points.player-points.take-points-from-victim")){
                                if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)){
                                    victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                            .replace("%KILLER%", killer.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                                }else {
                                    victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")));
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                            .replace("%VICTIM%", victim.getName())));
                                }
                            }
                            UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue);
                            firePlayerPointsAwardedEvent(killer, killer, victim, killerClanOwner, victimClanOwner, nonEnemyPointValue, false);
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"));
                            }
                        }
                    }

                    else {
                        if (ClansStorageUtil.findClanByPlayer(killer) != null || ClansStorageUtil.findClanByPlayer(victim) != null){
                            Clan killerClan = ClansStorageUtil.findClanByPlayer(killer);
                            Clan victimClan = ClansStorageUtil.findClanByPlayer(victim);
                            if (killerClan != null||victimClan != null){
                                if (killerClan.getClanEnemies() != null && !killerClan.getClanEnemies().isEmpty() || victimClan.getClanEnemies() != null && !victimClan.getClanEnemies().isEmpty()){
                                    if (killerClan.getClanEnemies().contains(victimClan.getClanOwner()) || victimClan.getClanEnemies().contains(killerClan.getClanOwner())) {
                                        killer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-killer-enemy-received-success")
                                                .replace("%PLAYER%", victim.getName()).replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())));
                                        if (clansConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                            if (UsermapStorageUtil.withdrawPoints(victim, enemyPointValue)) {
                                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-enemy-withdrawn-success")
                                                        .replace("%KILLER%", killer.getName()).replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())));
                                            }else {
                                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")));
                                                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                        .replace("%VICTIM%", victim.getName())));
                                            }
                                        }
                                        UsermapStorageUtil.addPointsToOnlinePlayer(killer, enemyPointValue);
                                        firePlayerPointsAwardedEvent(killer, killer, victim, killerClan, victimClan, enemyPointValue, true);
                                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"));
                                        }
                                    }else {
                                        killer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                                .replace("%PLAYER%", victim.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                                        if (clansConfig.getBoolean("points.player-points.take-points-from-victim")){
                                            if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)){
                                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                                        .replace("%KILLER%", killer.getName()).replace("%POINTVALUE%", nonEnemyPointValue.toString())));
                                            }else {
                                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")));
                                                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                        .replace("%VICTIM%", victim.getName())));
                                            }
                                        }
                                        UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue);
                                        firePlayerPointsAwardedEvent(killer, killer, victim, killerClan, victimClan, nonEnemyPointValue, false);
                                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void firePlayerPointsAwardedEvent(Player createdBy, Player killer, Player victim, @Nullable Clan killerClan, @Nullable Clan victimClan, int pointValue, boolean isEnemyPointReward){
        PlayerPointsAwardedEvent playerPointsAwardedEvent = new PlayerPointsAwardedEvent(createdBy, killer, victim, killerClan, victimClan, pointValue, isEnemyPointReward);
        Bukkit.getPluginManager().callEvent(playerPointsAwardedEvent);
    }
}
