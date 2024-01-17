package me.loving11ish.clans.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanHomeCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan clan;
    private final Location homeLocation;

    public ClanHomeCreateEvent(Player createdBy, Clan clan, Location homeLocation) {
        this.createdBy = createdBy;
        this.clan = clan;
        this.homeLocation = homeLocation;
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

    public Location getHomeLocation() {
        return homeLocation;
    }
}
