package me.loving11ish.clans.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Chest;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ChestOpenEvent implements Listener {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event){
        if (!Clans.isChestsEnabled()){
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Block block = event.getClickedBlock();
            if (block != null){
                if (block.getType().equals(Material.CHEST)){
                    Location chestLocation = block.getLocation();

                    if (!ClansStorageUtil.isChestLocked(chestLocation)){
                        return;
                    }

                    TileState tileState = (TileState) block.getState();
                    PersistentDataContainer container = tileState.getPersistentDataContainer();
                    String owningClanName = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanName"), PersistentDataType.STRING);
                    Player player = event.getPlayer();
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());

                    Chest chest = ClansStorageUtil.getChestByLocation(chestLocation);

                    if (chest != null) {
                        if (!ClansStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)) {
                            if (!(player.hasPermission("clanslite.bypass.chests") || player.hasPermission("clanslite.bypass.*")
                                    || player.hasPermission("clanslite.bypass") || player.hasPermission("clanslite.*") || player.isOp())) {
                                event.setCancelled(true);
                                if (owningClanName != null) {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan")
                                            .replace(CLAN_PLACEHOLDER, owningClanName)));
                                }else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan-name-unknown")));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
