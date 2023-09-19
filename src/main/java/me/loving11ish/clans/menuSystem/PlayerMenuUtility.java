package me.loving11ish.clans.menuSystem;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    public OfflinePlayer offlineClanOwner;
    public OfflinePlayer offlineCLanMember;

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    public OfflinePlayer getOfflineClanOwner() {
        return offlineClanOwner;
    }

    public OfflinePlayer getOfflineCLanMember() {
        return offlineCLanMember;
    }

    public void setOfflineClanOwner(OfflinePlayer offlineClanOwner) {
        this.offlineClanOwner = offlineClanOwner;
    }

    public void setOfflineCLanMember(OfflinePlayer offlineCLanMember) {
        this.offlineCLanMember = offlineCLanMember;
    }
}
