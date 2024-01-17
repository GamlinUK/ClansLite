package me.loving11ish.clans.commands.clanChestLockSubCommands;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ChestUnlockEvent;
import me.loving11ish.clans.models.Chest;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

import java.io.IOException;

public class ChestUnlockSubCommand {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String X_PLACEHOLDER = "%X%";
    private static final String Y_PLACEHOLDER = "%Y%";
    private static final String Z_PLACEHOLDER = "%Z%";

    public boolean chestUnlockSubCommand(CommandSender sender){

        if (sender instanceof Player){
            Player player = (Player) sender;
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
            Block block = player.getTargetBlockExact(5);
            if (block != null){
                if (block.getType().equals(Material.CHEST)){
                    Location location = block.getLocation();
                    int x = (int) Math.round(location.getX());
                    int y = (int) Math.round(location.getY());
                    int z = (int) Math.round(location.getZ());

                    if (ClansStorageUtil.isChestLocked(location)){
                        Chest chest = ClansStorageUtil.getChestByLocation(location);
                        if (ClansStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)){

                            TileState tileState = (TileState) block.getState();
                            PersistentDataContainer container = tileState.getPersistentDataContainer();
                            String clanOwnerUUIDString = container.get(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"), PersistentDataType.STRING);
                            if (clanOwnerUUIDString != null){

                                try {
                                    if (ClansStorageUtil.removeProtectedChest(clanOwnerUUIDString, location, player)){
                                        fireChestUnlockEvent(player, location);
                                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ChestUnlockEvent"));
                                        }
                                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-protection-removed-successfully")
                                                .replace(X_PLACEHOLDER, String.valueOf(x))
                                                .replace(Y_PLACEHOLDER, String.valueOf(y))
                                                .replace(Z_PLACEHOLDER, String.valueOf(z))));
                                        container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanName"));
                                        container.remove(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"));
                                        tileState.update();
                                        return true;
                                    }
                                }catch (IOException e){
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-2")));
                                    e.printStackTrace();
                                }
                                return true;
                            }

                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan-name-unknown")));
                            return true;
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-chest-not-protected")));
                        return true;
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                    return true;
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                return true;
            }
        }
        return true;
    }

    private static void fireChestUnlockEvent(Player player, Location removedLockLocation){
        ChestUnlockEvent chestUnlockEvent = new ChestUnlockEvent(player, removedLockLocation);
        Bukkit.getPluginManager().callEvent(chestUnlockEvent);
    }
}
