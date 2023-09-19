package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

import java.util.ArrayList;

public class ClanChatMessageSendEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan clan;
    private final String prefix;
    private final String message;
    private final ArrayList<String> recipients;

    public ClanChatMessageSendEvent(Player createdBy, Clan clan, String prefix, String message, ArrayList<String> recipients){
        this.createdBy = createdBy;
        this.clan = clan;
        this.prefix = prefix;
        this.message = message;
        this.recipients = recipients;
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

    public Clan getClan() {
        return clan;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }
}
