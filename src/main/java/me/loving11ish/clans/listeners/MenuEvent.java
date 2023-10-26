package me.loving11ish.clans.listeners;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.menusystem.Menu;
import me.loving11ish.clans.menusystem.paginatedMenu.ClanListGUI;
import me.loving11ish.clans.utils.ColorUtils;

public class MenuEvent implements Listener {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration guiConfig = Clans.getPlugin().clanGUIFileManager.getClanGUIConfig();
    FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
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
                WrappedTask wrappedTask = ClanListGUI.autoGUIRefreshTask;
                if (!wrappedTask.isCancelled()){
                    wrappedTask.cancel();
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aAuto refresh task cancelled"));
                    }
                }
            }
        }
    }
}
