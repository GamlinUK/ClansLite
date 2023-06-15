package xyz.gamlin.clans.commands.clanChestLockSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.api.ChestLockEvent;
import xyz.gamlin.clans.models.Chest;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.logging.Logger;

public class ChestLockSubCommand {

    Logger logger = Clans.getPlugin().getLogger();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String LIMIT_PLACEHOLDER = "%LIMIT%";
    private static final String X_PLACEHOLDER = "%X%";
    private static final String Y_PLACEHOLDER = "%Y%";
    private static final String Z_PLACEHOLDER = "%Z%";

    public boolean clanChestLockSubCommand(CommandSender sender){
        if (sender instanceof Player player){
            Block block = player.getTargetBlockExact(5);
            Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
            Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);
            if (clanByOwner != null){
                if (block != null){
                    if (block.getType().equals(Material.CHEST)){
                        int maxAllowedChests = clanByOwner.getMaxAllowedProtectedChests();
                        if (ClansStorageUtil.getAllProtectedChestsByClan(clanByOwner).size() >= maxAllowedChests){
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-max-amount-reached")
                                    .replace(LIMIT_PLACEHOLDER, String.valueOf(maxAllowedChests))));
                            return true;
                        }
                        lockTargetedChest(clanByOwner, block, player);
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                        return true;
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                    return true;
                }
            }else {
                if (clanByPlayer != null){
                    if (block != null){
                        if (block.getType().equals(Material.CHEST)){
                            int maxAllowedChests = clanByPlayer.getMaxAllowedProtectedChests();
                            if (ClansStorageUtil.getAllProtectedChestsByClan(clanByPlayer).size() >= maxAllowedChests){
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-max-amount-reached")
                                        .replace(LIMIT_PLACEHOLDER, String.valueOf(maxAllowedChests))));
                                return true;
                            }
                            lockTargetedChest(clanByPlayer, block, player);
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")));
                    }
                    return true;
                }
            }
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-clan")));
            return true;
        }
        return true;
    }

    private void lockTargetedChest(Clan clan, Block block, Player player){
        Location location = block.getLocation();
        int x = (int) Math.round(location.getX());
        int y = (int) Math.round(location.getY());
        int z = (int) Math.round(location.getZ());
        if (ClansStorageUtil.addProtectedChest(clan, location, player)){
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-protected-successfully")
                    .replace(X_PLACEHOLDER, String.valueOf(x))
                    .replace(Y_PLACEHOLDER, String.valueOf(y))
                    .replace(Z_PLACEHOLDER, String.valueOf(z))));
            TileState tileState = (TileState) block.getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            container.set(new NamespacedKey(Clans.getPlugin(), "owningClanName"), PersistentDataType.STRING, clan.getClanFinalName());
            container.set(new NamespacedKey(Clans.getPlugin(), "owningClanOwnerUUID"), PersistentDataType.STRING, clan.getClanOwner());
            tileState.update();
            fireChestLockEvent(player, clan, ClansStorageUtil.getChestByLocation(location));
            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ChestLockEvent"));
            }
        }
    }

    private static void fireChestLockEvent(Player player, Clan clan, Chest chest){
        ChestLockEvent chestLockEvent = new ChestLockEvent(player, clan, chest);
        Bukkit.getPluginManager().callEvent(chestLockEvent);
    }
}
