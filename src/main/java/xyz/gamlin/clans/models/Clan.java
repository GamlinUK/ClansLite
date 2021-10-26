package xyz.gamlin.clans.models;

import java.util.ArrayList;
import java.util.UUID;

public class Clan {

    private String clanName;
    private UUID clanOwner;
    private ArrayList<UUID> clanMembers;
    private String clanPrefix;

    public Clan(UUID clanOwner, String clanName) {
        this.clanName = clanName;
        this.clanOwner = clanOwner;
        this.clanPrefix = clanName;
        this.clanMembers = new ArrayList<>();
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public UUID getClanOwner() {
        return clanOwner;
    }

    public void setClanOwner(UUID clanOwner) {
        this.clanOwner = clanOwner;
    }

    public ArrayList<UUID> getClanMembers() {
        return clanMembers;
    }

    public void setClanMembers(ArrayList<UUID> clanMembers) {
        this.clanMembers = clanMembers;
    }

    public Boolean addClanMember(UUID clanMember) {
        return this.clanMembers.add(clanMember);
    }

    public Boolean removeClanMember(UUID clanMember) {
        return this.clanMembers.remove(clanMember);
    }

    public String getClanPrefix() {
        return clanPrefix;
    }

    public void setClanPrefix(String clanPrefix) {
        this.clanPrefix = clanPrefix;
    }
}
