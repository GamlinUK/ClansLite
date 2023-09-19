package me.loving11ish.clans.models;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Chest {

    private String UUID;
    private String chestWorldName;
    private double chestLocationX;
    private double chestLocationY;
    private double chestLocationZ;
    private ArrayList<String> playersWithAccess = new ArrayList<>();

    public Chest(Clan owningClan, Location location) {
        this.UUID = java.util.UUID.randomUUID().toString();
        this.chestWorldName = location.getWorld().getName();
        this.chestLocationX = location.getX();
        this.chestLocationY = location.getY();
        this.chestLocationZ = location.getZ();

        String clanOwner = owningClan.getClanOwner();
        List<String> clanMembers = owningClan.getClanMembers();
        playersWithAccess.add(clanOwner);
        playersWithAccess.addAll(clanMembers);
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getChestWorldName() {
        return chestWorldName;
    }

    public void setChestWorldName(String chestWorldName) {
        this.chestWorldName = chestWorldName;
    }

    public double getChestLocationX() {
        return chestLocationX;
    }

    public void setChestLocationX(double chestLocationX) {
        this.chestLocationX = chestLocationX;
    }

    public double getChestLocationY() {
        return chestLocationY;
    }

    public void setChestLocationY(double chestLocationY) {
        this.chestLocationY = chestLocationY;
    }

    public double getChestLocationZ() {
        return chestLocationZ;
    }

    public void setChestLocationZ(double chestLocationZ) {
        this.chestLocationZ = chestLocationZ;
    }

    public ArrayList<String> getPlayersWithAccess() {
        return playersWithAccess;
    }

    public void setPlayersWithAccess(ArrayList<String> playersWithAccess) {
        this.playersWithAccess = playersWithAccess;
    }
}
