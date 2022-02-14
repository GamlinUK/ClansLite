package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.ClanInvite;

import java.util.*;
import java.util.logging.Logger;

public class ClanInviteUtil {

    private static final Logger logger = Clans.getPlugin().getLogger();

    private static Map<UUID, ClanInvite> invitesList = new HashMap<>();
    
    public static ClanInvite createInvite(String inviterUUID, String inviteeUUID){
        UUID uuid = UUID.fromString(inviterUUID);
        clearExpiredInvites();
        if (!invitesList.containsKey(uuid)){
            invitesList.put(uuid, new ClanInvite(inviterUUID, inviteeUUID));
            return invitesList.get(uuid);
        }
        return null;
    }

    public static boolean searchInvitee(String inviteeUUID){
        for (ClanInvite invite : invitesList.values()){
            if (invite.getInvitee().equals(inviteeUUID)){
                return true;
            }
        }
        return false;
    }

    public static void clearExpiredInvites(){
        int expiryTime = 25 * 1000;
        Date currentTime = new Date();
        for (ClanInvite clanInvite : invitesList.values()){
            if (currentTime.getTime() - clanInvite.getInviteTime().getTime() > expiryTime){
                invitesList.remove(clanInvite);
            }
        }
    }

    public static Set<Map.Entry<UUID, ClanInvite>> getInvites(){
        return invitesList.entrySet();
    }

    public static Player getInviteOwner(String inviteeUUID){
        if (inviteeUUID.length() < 37){
            UUID uuid = UUID.fromString(inviteeUUID);
            if (invitesList.containsKey(uuid)){
                return Bukkit.getPlayer(uuid);
            }
        }else {
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4An error occurred whilst getting an Invite Owner."));
            logger.warning(ColorUtils.translateColorCodes("&6ClansLite: &4Error: &3The provided UUID is too long."));
        }
        return null;
    }

}
