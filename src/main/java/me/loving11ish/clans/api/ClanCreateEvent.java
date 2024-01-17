package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan clan;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ClanCreateEvent(Player createdBy, Clan clanName) {
        this.createdBy = createdBy;
        this.clan = clanName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Clan getClan() {
        return clan;
    }

}
