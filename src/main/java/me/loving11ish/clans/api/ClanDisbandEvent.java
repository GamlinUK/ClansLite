package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDisbandEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final String clanName;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ClanDisbandEvent(Player createdBy, String clanName) {
        this.createdBy = createdBy;
        this.clanName = clanName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public String getClan() {
        return clanName;
    }
}
