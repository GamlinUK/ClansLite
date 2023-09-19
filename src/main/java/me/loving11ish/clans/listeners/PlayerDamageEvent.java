package me.loving11ish.clans.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ClanFriendlyFireAttackEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.util.*;

public class PlayerDamageEvent implements Listener {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player) {
            Player hurtPlayer = (Player) event.getEntity();
            String hurtUUID = hurtPlayer.getUniqueId().toString();
            if (event.getDamager() instanceof Player) {
                Player attackingPlayer = (Player) event.getDamager();
                attackingPlayer.setInvulnerable(false);
                Clan attackingClan = ClansStorageUtil.findClanByOwner(attackingPlayer);
                Clan victimClan = ClansStorageUtil.findClanByOwner(hurtPlayer);


                if (attackingClan != null){
                    ArrayList<String> attackingClanMembers = attackingClan.getClanMembers();
                    if (attackingClanMembers.contains(hurtUUID) || attackingClan.getClanOwner().equals(hurtUUID)){
                        if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                            if (!attackingClan.isFriendlyFireAllowed()){
                                if (clansConfig.getBoolean("protections.pvp.enable-bypass-permission")){
                                    if (attackingPlayer.hasPermission("clanslite.bypass.pvp")
                                            ||attackingPlayer.hasPermission("clanslite.bypass.*")
                                            ||attackingPlayer.hasPermission("clanslite.bypass")
                                            ||attackingPlayer.hasPermission("clanslite.*")
                                            ||attackingPlayer.isOp()){
                                        return;
                                    }
                                }
                                event.setCancelled(true);
                                fireClanFriendlyFireAttackEvent(hurtPlayer, attackingPlayer, hurtPlayer, attackingClan, victimClan);
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanFriendlyFireAttackEvent"));
                                }
                                attackingPlayer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("friendly-fire-is-disabled")));
                            }
                        }else {
                            event.setCancelled(false);
                        }
                    }
                }


                else {
                    Clan attackingClanByPlayer = ClansStorageUtil.findClanByPlayer(attackingPlayer);
                    Clan victimClanByPlayer = ClansStorageUtil.findClanByPlayer(hurtPlayer);
                    if (attackingClanByPlayer != null){
                        ArrayList<String> attackingMembers = attackingClanByPlayer.getClanMembers();
                        if (attackingMembers.contains(hurtUUID) || attackingClanByPlayer.getClanOwner().equals(hurtUUID)){
                            if (clansConfig.getBoolean("protections.pvp.pvp-command-enabled")){
                                if (!attackingClanByPlayer.isFriendlyFireAllowed()){
                                    if (clansConfig.getBoolean("protections.pvp.enable-bypass-permission")){
                                        if (attackingPlayer.hasPermission("clanslite.bypass.pvp")
                                                ||attackingPlayer.hasPermission("clanslite.bypass.*")
                                                ||attackingPlayer.hasPermission("clanslite.bypass")
                                                ||attackingPlayer.hasPermission("clanslite.*")
                                                ||attackingPlayer.isOp()){
                                            return;
                                        }
                                    }
                                    event.setCancelled(true);
                                    fireClanFriendlyFireAttackEvent(hurtPlayer, attackingPlayer, hurtPlayer, attackingClanByPlayer, victimClanByPlayer);
                                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanFriendlyFireAttackEvent"));
                                    }
                                    attackingPlayer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("friendly-fire-is-disabled")));
                                }
                            }else {
                                event.setCancelled(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void fireClanFriendlyFireAttackEvent(Player createdBy, Player attackingPlayer, Player victimPlayer, Clan attackingClan, Clan victimClan){
        ClanFriendlyFireAttackEvent clanFriendlyFireAttackEvent = new ClanFriendlyFireAttackEvent(createdBy, attackingPlayer, victimPlayer, attackingClan, victimClan);
        Bukkit.getPluginManager().callEvent(clanFriendlyFireAttackEvent);
    }
}