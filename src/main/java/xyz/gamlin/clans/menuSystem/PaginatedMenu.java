package xyz.gamlin.clans.menuSystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 45;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuControls(){
        inventory.setItem(48, makeItem(Material.STONE_BUTTON, ChatColor.GREEN + "Previous Page"));
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close/Go Back"));
        inventory.setItem(50, makeItem(Material.STONE_BUTTON, ChatColor.GREEN + "Next Page"));
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
