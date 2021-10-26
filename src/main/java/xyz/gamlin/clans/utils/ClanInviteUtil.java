package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.models.ClanInvite;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ClanInviteUtil {

    private static ArrayList<ClanInvite> clansInvites = new ArrayList<>();
    
    public static ClanInvite createInvite(UUID inviter, UUID invitee) {

        // Remove expired invites
        clearExpiredInvites();

        // If a clan invite is already pending
        if (searchInvitee(invitee)) {
            return null;
        }

        ClanInvite clanInvite = new ClanInvite(inviter, invitee);
        clansInvites.add(clanInvite);

        return clanInvite;
    }
    
    public static boolean searchInvitee(UUID invitee) {
        for (ClanInvite invite : clansInvites) {
            if (invite.getInvitee().equals(invitee)) {
                return true;
            }
        }
        return false;
    }

    public static void clearExpiredInvites() {
        int expiryTime = 25 * 1000;
        Date currentTime = new Date();
        clansInvites.removeIf(invite -> (currentTime.getTime() - invite.getInviteTime().getTime()) > expiryTime);
    }

    public static Player getInviteOwner(UUID invitee) {
        for (ClanInvite invite : clansInvites) {
            if (invite.getInvitee() == invitee) {
                return Bukkit.getPlayer(invite.getInviter());
            }
        }

        return null;
    }

    public void removeInvite(UUID invitee) {
        clansInvites.removeIf(invite -> invite.getInvitee().equals(invitee));
    }

}
