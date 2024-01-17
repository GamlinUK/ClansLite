package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanTransferOwnershipEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Player originalClanOwner;
    private final Player newClanOwner;
    private final Clan newClan;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ClanTransferOwnershipEvent(Player createdBy, Player originalClanOwner, Player newClanOwner, Clan newClan) {
        this.createdBy = createdBy;
        this.originalClanOwner = originalClanOwner;
        this.newClanOwner = newClanOwner;
        this.newClan = newClan;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Player getOriginalClanOwner() {
        return originalClanOwner;
    }

    public Player getNewClanOwner() {
        return newClanOwner;
    }

    public Clan getNewClan() {
        return newClan;
    }
}
