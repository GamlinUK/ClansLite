package me.loving11ish.clans.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanOfflineDisbandEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final OfflinePlayer createdBy;
    private final String clanName;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ClanOfflineDisbandEvent(OfflinePlayer createdBy, String clanName) {
        this.createdBy = createdBy;
        this.clanName = clanName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public OfflinePlayer getCreatedBy() {
        return createdBy;
    }

    public String getClan() {
        return clanName;
    }
}
