package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanHomePreTeleportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan clan;

    public ClanHomePreTeleportEvent(Player createdBy, Clan clan) {
        this.createdBy = createdBy;
        this.clan = clan;
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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
