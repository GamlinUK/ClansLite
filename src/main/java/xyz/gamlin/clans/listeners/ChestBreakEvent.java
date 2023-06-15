package xyz.gamlin.clans.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
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
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Chest;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ChestBreakEvent implements Listener {

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    Logger logger = Clans.getPlugin().getLogger();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";
    private static final String X_PLACEHOLDER = "%X%";
    private static final String Y_PLACEHOLDER = "%Y%";
    private static final String Z_PLACEHOLDER = "%Z%";

    @EventHandler
    public void onChestBreak(BlockBreakEvent event){
        if (!clansConfig.getBoolean("protections.chests.enabled")){
            return;
        }
        if (event.getBlock().getType().equals(Material.CHEST)){
            Location chestLocation = event.getBlock().getLocation();
            int x = (int) Math.round(chestLocation.getX());
            int y = (int) Math.round(chestLocation.getY());
            int z = (int) Math.round(chestLocation.getZ());

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
                            logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                            logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onTNTDestruction(EntityExplodeEvent event){
        if (!clansConfig.getBoolean("protections.chests.enabled")){
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
                            try {
                                if (ClansStorageUtil.removeProtectedChest(owningClanOwnerUUID, chestLocation)){
                                    container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanName"));
                                    container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"));
                                    tileState.update();
                                }
                            }catch (IOException e){
                                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreeperDestruction(EntityExplodeEvent event){
        if (!clansConfig.getBoolean("protections.chests.enabled")){
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
                            try {
                                if (ClansStorageUtil.removeProtectedChest(owningClanOwnerUUID, chestLocation)){
                                    container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanName"));
                                    container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"));
                                    tileState.update();
                                }
                            }catch (IOException e){
                                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
