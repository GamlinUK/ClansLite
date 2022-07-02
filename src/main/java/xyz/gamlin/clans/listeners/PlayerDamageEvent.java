package xyz.gamlin.clans.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.*;

public class PlayerDamageEvent implements Listener {

    FileConfiguration config = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player) {
            Player hurtPlayer = (Player) event.getEntity();
            String hurtUUID = hurtPlayer.getUniqueId().toString();
            if (event.getDamager() instanceof Player) {
                Player attackingPlayer = (Player) event.getDamager();
                attackingPlayer.setInvulnerable(false);
                Clan attackingClan = ClansStorageUtil.findClanByOwner(attackingPlayer);


                if (attackingClan != null){
                    ArrayList<String> attackingClanMembers = attackingClan.getClanMembers();
                    if (attackingClanMembers.contains(hurtUUID) || attackingClan.getClanOwner().equals(hurtUUID)){
                        if (config.getBoolean("protections.pvp.pvp-command-enabled")){
                            if (!attackingClan.isFriendlyFireAllowed()){
                                if (config.getBoolean("protections.pvp.enable-bypass-permission")){
                                    if (attackingPlayer.hasPermission("clanslite.bypass.pvp")
                                            ||attackingPlayer.hasPermission("clanslite.bypass.*")
                                            ||attackingPlayer.hasPermission("clanslite.bypass")
                                            ||attackingPlayer.hasPermission("clanslite.*")
                                            ||attackingPlayer.isOp()){
                                        return;
                                    }
                                }
                                event.setCancelled(true);
                                attackingPlayer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("friendly-fire-is-disabled")));
                            }
                        }else {
                            event.setCancelled(false);
                        }
                    }
                }


                else {
                    Clan attackingClanByPlayer = ClansStorageUtil.findClanByPlayer(attackingPlayer);
                    if (attackingClanByPlayer != null){
                        ArrayList<String> attackingMembers = attackingClanByPlayer.getClanMembers();
                        if (attackingMembers.contains(hurtUUID) || attackingClanByPlayer.getClanOwner().equals(hurtUUID)){
                            if (config.getBoolean("protections.pvp.pvp-command-enabled")){
                                if (!attackingClanByPlayer.isFriendlyFireAllowed()){
                                    if (config.getBoolean("protections.pvp.enable-bypass-permission")){
                                        if (attackingPlayer.hasPermission("clanslite.bypass.pvp")
                                                ||attackingPlayer.hasPermission("clanslite.bypass.*")
                                                ||attackingPlayer.hasPermission("clanslite.bypass")
                                                ||attackingPlayer.hasPermission("clanslite.*")
                                                ||attackingPlayer.isOp()){
                                            return;
                                        }
                                    }
                                    event.setCancelled(true);
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
}