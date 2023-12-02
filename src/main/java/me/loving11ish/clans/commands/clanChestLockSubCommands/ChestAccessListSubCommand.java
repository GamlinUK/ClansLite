package me.loving11ish.clans.commands.clanChestLockSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Chest;
import me.loving11ish.clans.models.ClanPlayer;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

import java.util.List;

public class ChestAccessListSubCommand {

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String PLAYER_PLACEHOLDER = "%PLAYER%";

    public boolean chestAccessListSubCommand(CommandSender sender){

        if (sender instanceof Player){
            Player player = (Player) sender;
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
            Block block = player.getTargetBlockExact(5);
            if (block != null){
                if (block.getType().equals(Material.CHEST)){
                    Location location = block.getLocation();
                    if (ClansStorageUtil.isChestLocked(location)){
                        Chest chest = ClansStorageUtil.getChestByLocation(location);
                        if (chest != null){
                            if (ClansStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)){
                                List<OfflinePlayer> offlinePlayersWithAccess = ClansStorageUtil.getOfflinePlayersWithChestAccessByChest(chest);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(messagesConfig.getString("players-with-access-list.header"));
                                for (OfflinePlayer offlinePlayerWithAccess : offlinePlayersWithAccess){
                                    ClanPlayer clanPlayer = UsermapStorageUtil.getClanPlayerByBukkitOfflinePlayer(offlinePlayerWithAccess);
                                    if (clanPlayer != null){
                                        String playerName = clanPlayer.getLastPlayerName();
                                        stringBuilder.append(messagesConfig.getString("players-with-access-list.player-entry")
                                                .replace(PLAYER_PLACEHOLDER, playerName));
                                    }
                                }
                                stringBuilder.append(messagesConfig.getString("players-with-access-list.footer"));
                                player.sendMessage(ColorUtils.translateColorCodes(stringBuilder.toString()));
                                return true;
                            }else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-clan-name-unknown")));
                            }
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
}
