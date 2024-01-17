package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;

public class ClanFriendlyFireAttackEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Player attackingPlayer;
    private final Player victimPlayer;
    private final Clan attackingClan;
    private final Clan victimClan;

    public ClanFriendlyFireAttackEvent(Player createdBy, Player attackingPlayer, Player victimPlayer, Clan attackingClan, Clan victimClan) {
        this.createdBy = createdBy;
        this.attackingPlayer = attackingPlayer;
        this.victimPlayer = victimPlayer;
        this.attackingClan = attackingClan;
        this.victimClan = victimClan;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Player getAttackingPlayer() {
        return attackingPlayer;
    }

    public Player getVictimPlayer() {
        return victimPlayer;
    }

    public Clan getAttackingClan() {
        return attackingClan;
    }

    public Clan getVictimClan() {
        return victimClan;
    }
}
