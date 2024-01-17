package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.ClanPlayer;

public class ClanChatSpyToggledEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final ClanPlayer clanPlayer;
    private final boolean clanChatSpyState;

    public ClanChatSpyToggledEvent(Player createdBy, ClanPlayer clanPlayer, boolean clanChatSpyState) {
        this.createdBy = createdBy;
        this.clanPlayer = clanPlayer;
        this.clanChatSpyState = clanChatSpyState;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public ClanPlayer getClanPlayer() {
        return clanPlayer;
    }

    public boolean isClanChatSpyState() {
        return clanChatSpyState;
    }
}
