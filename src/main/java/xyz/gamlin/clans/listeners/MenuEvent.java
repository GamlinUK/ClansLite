package xyz.gamlin.clans.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import xyz.gamlin.clans.menuSystem.Menu;

public class MenuEvent implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }
            Menu menu = (Menu) holder;
            menu.handleMenu(event);
        }
    }
}
