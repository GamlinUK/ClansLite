package xyz.gamlin.clans.listeners;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.menuSystem.Menu;
import xyz.gamlin.clans.menuSystem.paginatedMenu.ClanListGUI;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.logging.Logger;

public class MenuEvent implements Listener {

    FileConfiguration guiConfig = Clans.getPlugin().clanGUIFileManager.getClanGUIConfig();
    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    Logger logger = Clans.getPlugin().getLogger();

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }
            menu.handleMenu(event);
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu){
            if (((Menu) holder).getMenuName().equalsIgnoreCase(ColorUtils.translateColorCodes(guiConfig.getString("clan-list.name")))){
                WrappedTask wrappedTask = ClanListGUI.task5;
                if (!wrappedTask.isCancelled()){
                    wrappedTask.cancel();
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aAuto refresh task cancelled"));
                    }
                }
            }
        }
    }
}
