package me.loving11ish.clans.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Chest;
import me.loving11ish.clans.models.Clan;

public class ChestLockEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan owningClan;
    private final Chest chest;
    private final Location chestLocation;

    public ChestLockEvent(Player createdBy, Clan clan, Chest chest) {
        this.createdBy = createdBy;
        this.owningClan = clan;
        this.chest = chest;
        this.chestLocation = new Location(Bukkit.getWorld(chest.getChestWorldName()), chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ());
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Clan getOwningClan() {
        return owningClan;
    }

    public Chest getChest() {
        return chest;
    }

    public Location getChestLocation() {
        return chestLocation;
    }
}
