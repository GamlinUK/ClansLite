package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanAllyRemoveEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan clan;
    private final Player exAllyClanCreatedBy;
    private final Clan exAllyClan;



    public ClanAllyRemoveEvent(Player createdBy, Clan clan, Clan exAllyClan, Player exAllyClanCreatedBy) {
        this.createdBy = createdBy;
        this.clan = clan;
        this.exAllyClanCreatedBy = exAllyClanCreatedBy;
        this.exAllyClan = exAllyClan;
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

    public Player getExAllyClanCreatedBy() {
        return exAllyClanCreatedBy;
    }

    public Clan getExAllyClan() {
        return exAllyClan;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
