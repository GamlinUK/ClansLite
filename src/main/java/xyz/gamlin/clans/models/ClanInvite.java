package xyz.gamlin.clans.models;

import java.util.Date;
import java.util.UUID;

public class ClanInvite {

    private UUID inviter;
    private UUID invitee;
    final Date inviteTime;

    public ClanInvite(UUID inviter, UUID invitee) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.inviteTime = new Date();
    }

    public UUID getInviter() {
        return inviter;
    }

    public void setInviter(UUID inviter) {
        this.inviter = inviter;
    }

    public UUID getInvitee() {
        return invitee;
    }

    public void setInvitee(UUID invitee) {
        this.invitee = invitee;
    }

    public Date getInviteTime() {
        return inviteTime;
    }
}
