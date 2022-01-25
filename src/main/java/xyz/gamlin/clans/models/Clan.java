package xyz.gamlin.clans.models;

import java.util.ArrayList;

public class Clan {

    private String clanFinalOwner;
    private String clanFinalName;
    private String clanPrefix;
    private ArrayList<String> clanMembers;
    private ArrayList<String> clanAllies;
    private boolean friendlyFire;


    public Clan(String clanOwner, String clanName) {
        clanFinalOwner = clanOwner;
        clanFinalName = clanName;
        clanPrefix = clanFinalName;
        clanMembers = new ArrayList<>();
        clanAllies = new ArrayList<>();
        friendlyFire = true;
    }

    public String getClanOwner() {
        return clanFinalOwner;
    }

    public String getClanFinalName() {
        return clanFinalName;
    }

    public void setClanFinalName(String newClanFinalName) {
        clanFinalName = newClanFinalName;
    }

    public String getClanPrefix() {
        return clanPrefix;
    }

    public void setClanPrefix(String newClanPrefix) {
        clanPrefix = newClanPrefix;
    }

    public ArrayList<String> getClanMembers() {
        return clanMembers;
    }

    public void setClanMembers(ArrayList<String> clanMembersList) {
        clanMembers = clanMembersList;
    }

    public void addClanMember(String clanMember) {
        clanMembers.add(clanMember);
    }

    public Boolean removeClanMember(String clanMember) {
        return clanMembers.remove(clanMember);
    }

    public ArrayList<String> getClanAllies() {
        return clanAllies;
    }

    public void addClanAlly(String ally){
        clanAllies.add(ally);
    }

    public void removeClanAlly(String allyUUID){
        clanAllies.remove(allyUUID);
    }

    public void setClanAllies(ArrayList<String> clanAlliesList) {
        clanAllies = clanAlliesList;
    }

    public boolean isFriendlyFireAllowed() {
        return friendlyFire;
    }

    public void setFriendlyFireAllowed(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }
}
