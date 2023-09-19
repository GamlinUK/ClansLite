package me.loving11ish.clans.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestUnlockEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Location removedLockLocation;

    public ChestUnlockEvent(Player createdBy, Location removedLockLocation) {
        this.createdBy = createdBy;
        this.removedLockLocation = removedLockLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Location getRemovedLockLocation() {
        return removedLockLocation;
    }
}
