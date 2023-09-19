package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.models.ClanPlayer;

public class ClanPointsAddedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Clan playerClan;
    private final ClanPlayer clanPlayer;
    private final int previousClanPlayerPointBalance;
    private final int newClanPlayerPointBalance;
    private final int depositPointValue;
    private final int previousClanPointBalance;
    private final int newClanPointBalance;

    public ClanPointsAddedEvent(Player createdBy, Clan playerClan, ClanPlayer clanPlayer,
                                int previousClanPlayerPointBalance, int newClanPlayerPointBalance,
                                int depositPointValue, int previousClanPointBalance, int newClanPointBalance) {
        this.createdBy = createdBy;
        this.playerClan = playerClan;
        this.clanPlayer = clanPlayer;
        this.previousClanPlayerPointBalance = previousClanPlayerPointBalance;
        this.newClanPlayerPointBalance = newClanPlayerPointBalance;
        this.depositPointValue = depositPointValue;
        this.previousClanPointBalance = previousClanPointBalance;
        this.newClanPointBalance = newClanPointBalance;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getCreatedBy() {
        return createdBy;
    }

    public Clan getPlayerClan() {
        return playerClan;
    }

    public ClanPlayer getClanPlayer() {
        return clanPlayer;
    }

    public int getPreviousClanPlayerPointBalance() {
        return previousClanPlayerPointBalance;
    }

    public int getNewClanPlayerPointBalance() {
        return newClanPlayerPointBalance;
    }

    public int getDepositPointValue() {
        return depositPointValue;
    }

    public int getPreviousClanPointBalance() {
        return previousClanPointBalance;
    }

    public int getNewClanPointBalance() {
        return newClanPointBalance;
    }
}
