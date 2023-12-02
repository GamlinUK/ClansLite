package me.loving11ish.clans.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Chest;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.io.IOException;
import java.util.ArrayList;

public class ChestBreakEvent implements Listener {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";
    private static final String X_PLACEHOLDER = "%X%";
    private static final String Y_PLACEHOLDER = "%Y%";
    private static final String Z_PLACEHOLDER = "%Z%";

    @EventHandler
    public void onChestBreak(BlockBreakEvent event){
        if (!Clans.isChestsEnabled()){
            return;
        }
        if (event.getBlock().getType().equals(Material.CHEST)){
            Location chestLocation = event.getBlock().getLocation();
            double x = Math.round(chestLocation.getX());
            double y = Math.round(chestLocation.getY());
            double z = Math.round(chestLocation.getZ());

            if (!ClansStorageUtil.isChestLocked(chestLocation)){
                return;
            }

            TileState tileState = (TileState) event.getBlock().getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            String owningClanName = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanName"), PersistentDataType.STRING);
            String owningClanOwnerUUID = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"), PersistentDataType.STRING);
            Player player = event.getPlayer();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());

            Chest chest = ClansStorageUtil.getChestByLocation(chestLocation);

            if (chest != null){
                if (!ClansStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)){
                    if (!(player.hasPermission("clanslite.bypass.chests")||player.hasPermission("clanslite.bypass.*")
                                ||player.hasPermission("clanslite.bypass")||player.hasPermission("clanslite.*")||player.isOp())){
                        event.setCancelled(true);
                        if (owningClanName != null){
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan")
                                    .replace(CLAN_PLACEHOLDER, owningClanName)));
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan-name-unknown")));
                        }
                    }else {
                        try {
                            if (ClansStorageUtil.removeProtectedChest(owningClanOwnerUUID, chestLocation, player)){
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-protection-removed-successfully")
                                        .replace(X_PLACEHOLDER, String.valueOf(x))
                                        .replace(Y_PLACEHOLDER, String.valueOf(y))
                                        .replace(Z_PLACEHOLDER, String.valueOf(z))));
                                container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanName"));
                                container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"));
                                tileState.update();
                            }
                        }catch (IOException e){
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onTNTDestruction(EntityExplodeEvent event){
        if (!Clans.isChestsEnabled()){
            return;
        }
        if (event.getEntity() instanceof TNTPrimed){
            for (Block block : new ArrayList<>(event.blockList())){
                if (block.getType().equals(Material.CHEST)){
                    Location chestLocation = block.getLocation();
                    if (ClansStorageUtil.isChestLocked(chestLocation)){
                        TileState tileState = (TileState) block.getState();
                        PersistentDataContainer container = tileState.getPersistentDataContainer();
                        String owningClanOwnerUUID = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"), PersistentDataType.STRING);
                        if (!clansConfig.getBoolean("protections.chests.enable-TNT-destruction")){
                            event.blockList().remove(block);
                        }else {
                            removeLockedChest(owningClanOwnerUUID, chestLocation, container, tileState);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreeperDestruction(EntityExplodeEvent event){
        if (!Clans.isChestsEnabled()){
            return;
        }
        if (event.getEntity() instanceof Creeper){
            for (Block block : new ArrayList<>(event.blockList())){
                if (block.getType().equals(Material.CHEST)){
                    Location chestLocation = block.getLocation();
                    if (ClansStorageUtil.isChestLocked(chestLocation)){
                        TileState tileState = (TileState) block.getState();
                        PersistentDataContainer container = tileState.getPersistentDataContainer();
                        String owningClanOwnerUUID = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"), PersistentDataType.STRING);
                        if (!clansConfig.getBoolean("protections.chests.enable-creeper-destruction")){
                            event.blockList().remove(block);
                        }else {
                            removeLockedChest(owningClanOwnerUUID, chestLocation, container, tileState);
                        }
                    }
                }
            }
        }
    }

    private void removeLockedChest(String owningClanOwnerUUID, Location chestLocation, PersistentDataContainer container, TileState tileState){
        try {
            if (ClansStorageUtil.removeProtectedChest(owningClanOwnerUUID, chestLocation)){
                container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanName"));
                container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"));
                tileState.update();
            }
        }catch (IOException e){
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
            e.printStackTrace();
        }
    }
}
