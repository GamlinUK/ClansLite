package me.loving11ish.clans.commands.clanChestLockSubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ChestBuyEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class ChestBuySubCommand {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private final int purchasePrice = clansConfig.getInt("protections.chests.clan-points-purchase-value");
    private static final String AMOUNT_PLACEHOLDER = "%AMOUNT%";

    public boolean chestBuySubCommand(CommandSender sender, String[] args){

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length > 1){
                if (args[1] != null){
                    int amountOfChests = Integer.parseInt(args[1]);
                    if (amountOfChests != 0){
                        Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                        Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);

                        if (clanByOwner != null){
                            addNewChestLock(clanByOwner, player, amountOfChests);
                            return true;
                        }else if (clanByPlayer != null){
                            addNewChestLock(clanByPlayer, player, amountOfChests);
                            return true;
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-clan")));
                            return true;
                        }
                    }
                }
            }else {
                if (args.length == 1){
                    Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
                    Clan clanByPlayer = ClansStorageUtil.findClanByPlayer(player);

                    if (clanByOwner != null){
                        addNewChestLock(clanByOwner, player);
                        return true;
                    }else if (clanByPlayer != null){
                        addNewChestLock(clanByPlayer, player);
                        return true;
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-clan")));
                        return true;
                    }
                }
            }
        }
        return true;
    }

    private void addNewChestLock(Clan clan, Player player, int amountOfChests){
        int maxAllowedChests = clan.getMaxAllowedProtectedChests();
        if (ClansStorageUtil.hasEnoughPoints(clan, purchasePrice * maxAllowedChests)){
            if (ClansStorageUtil.withdrawPoints(clan, purchasePrice * maxAllowedChests)){
                clan.setMaxAllowedProtectedChests(maxAllowedChests + amountOfChests);
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-purchased-successfully")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))));
                fireChestBuyEvent(player, clan, maxAllowedChests, maxAllowedChests + amountOfChests);
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ChestBuyEvent"));
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-enough-points")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))));
            }
        }else {
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-enough-points")
                    .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))));
        }
    }

    private void addNewChestLock(Clan clan, Player player){
        int maxAllowedChests = clan.getMaxAllowedProtectedChests();
        if (ClansStorageUtil.hasEnoughPoints(clan, purchasePrice * maxAllowedChests)){
            if (ClansStorageUtil.withdrawPoints(clan, purchasePrice * maxAllowedChests)){
                clan.setMaxAllowedProtectedChests(maxAllowedChests + 1);
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-purchased-successfully")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))));
                fireChestBuyEvent(player, clan, maxAllowedChests, maxAllowedChests + 1);
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-enough-points")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))));
            }
        }else {
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-enough-points")
                    .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))));
        }
    }

    private static void fireChestBuyEvent(Player player, Clan clan, int oldClanMaxAllowedChests, int newChestCount){
        ChestBuyEvent chestBuyEvent = new ChestBuyEvent(player, clan, oldClanMaxAllowedChests, newChestCount);
        Bukkit.getPluginManager().callEvent(chestBuyEvent);
    }
}
