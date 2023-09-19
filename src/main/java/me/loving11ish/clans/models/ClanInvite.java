package me.loving11ish.clans.models;

import java.util.Date;

public class ClanInvite {

    private String inviter;
    private String invitee;
    final Date inviteTime;

    public ClanInvite(String inviter, String invitee) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.inviteTime = new Date();
    }

    public String getInviter() {
        return inviter;
    }

    @Deprecated
    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getInvitee() {
        return invitee;
    }

    @Deprecated
    public void setInvitee(String invitee) {
        this.invitee = invitee;
    }

    public Date getInviteTime() {
        return inviteTime;
    }
}
