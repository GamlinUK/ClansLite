package me.loving11ish.clans.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;
import me.loving11ish.clans.models.Clan;

public class PlayerPointsAwardedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player createdBy;
    private final Player killer;
    private final Player victim;
    @Nullable
    private final Clan killerClan;
    @Nullable
    private final Clan victimClan;
    private final int pointValue;
    private final boolean isEnemyPointReward;

    public PlayerPointsAwardedEvent(Player createdBy, Player killer, Player victim, @Nullable Clan killerClan, @Nullable Clan victimClan, int pointValue, boolean isEnemyPointReward) {
        this.createdBy = createdBy;
        this.killer = killer;
        this.victim = victim;
        this.killerClan = killerClan;
        this.victimClan = victimClan;
        this.pointValue = pointValue;
        this.isEnemyPointReward = isEnemyPointReward;
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

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
    }

    @Nullable
    public Clan getKillerClan() {
        return killerClan;
    }

    @Nullable
    public Clan getVictimClan() {
        return victimClan;
    }

    public int getPointValue() {
        return pointValue;
    }

    public boolean isEnemyPointReward() {
        return isEnemyPointReward;
    }
}
