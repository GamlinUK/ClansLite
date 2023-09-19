package me.loving11ish.clans.menuSystem.menu;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.menuSystem.Menu;
import me.loving11ish.clans.menuSystem.PlayerMenuUtility;
import me.loving11ish.clans.menuSystem.paginatedMenu.ClanListGUI;
import me.loving11ish.clans.utils.ColorUtils;

public class ClanJoinRequestMenu extends Menu {

    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    FileConfiguration guiConfig = Clans.getPlugin().clanGUIFileManager.getClanGUIConfig();

    public ClanJoinRequestMenu(PlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName(){
        return ColorUtils.translateColorCodes(guiConfig.getString("clan-join.name"));
    }

    @Override
    public int getSlots(){
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
            Player targetClanOwner = playerMenuUtility.getOfflineClanOwner().getPlayer();
            if (targetClanOwner != null){
                targetClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-request")
                        .replace("%PLAYER%", player.getName())));
                player.closeInventory();
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-sent-successfully")
                        .replace("%CLANOWNER%", targetClanOwner.getName())));
            }else {
                player.closeInventory();
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-invite-request-failed")));
            }
        }else if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)){
            new ClanListGUI(Clans.getPlayerMenuUtility(player)).open();
        }
    }

    @Override
    public void setMenuItems(){

        ItemStack sendJoinRequestItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta sendMeta = sendJoinRequestItem.getItemMeta();
        sendMeta.setDisplayName(ColorUtils.translateColorCodes(guiConfig.getString("clan-join.icons.send-request-name")));
        sendJoinRequestItem.setItemMeta(sendMeta);

        ItemStack cancelJoinRequestItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta cancelMeta = sendJoinRequestItem.getItemMeta();
        cancelMeta.setDisplayName(ColorUtils.translateColorCodes(guiConfig.getString("clan-join.icons.cancel-request-name")));
        cancelJoinRequestItem.setItemMeta(cancelMeta);

        inventory.setItem(0, sendJoinRequestItem);
        inventory.setItem(8, cancelJoinRequestItem);
    }
}
