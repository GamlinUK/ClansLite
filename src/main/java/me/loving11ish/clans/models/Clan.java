package me.loving11ish.clans.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Clan {

    private final String clanFinalOwner;
    private String clanFinalName;
    private String clanPrefix;
    private ArrayList<String> clanMembers;
    private ArrayList<String> clanAllies;
    private ArrayList<String> clanEnemies;
    private boolean friendlyFire;
    private int clanPoints;
    private String clanHomeWorld;
    private double clanHomeX;
    private double clanHomeY;
    private double clanHomeZ;
    private float clanHomeYaw;
    private float clanHomePitch;
    private int maxAllowedProtectedChests;
    private HashMap<String, Chest> protectedChests = new HashMap<>();

    public Clan(String clanOwner, String clanName) {
        clanFinalOwner = clanOwner;
        clanFinalName = clanName;
        clanPrefix = clanFinalName;
        clanMembers = new ArrayList<>();
        clanAllies = new ArrayList<>();
        clanEnemies = new ArrayList<>();
        friendlyFire = true;
        clanPoints = 0;
        clanHomeWorld = null;
        maxAllowedProtectedChests = 0;
    }

    public String getClanOwner(){
        return clanFinalOwner;
    }

    public String getClanFinalName(){
        return clanFinalName;
    }

    public void setClanFinalName(String newClanFinalName){
        clanFinalName = newClanFinalName;
    }

    public String getClanPrefix(){
        return clanPrefix;
    }

    public void setClanPrefix(String newClanPrefix){
        clanPrefix = newClanPrefix;
    }

    public ArrayList<String> getClanMembers(){
        return clanMembers;
    }

    public void setClanMembers(ArrayList<String> clanMembersList){
        clanMembers = clanMembersList;
    }

    public void addClanMember(String clanMember){
        clanMembers.add(clanMember);
    }

    public Boolean removeClanMember(String clanMember){
        return clanMembers.remove(clanMember);
    }

    public ArrayList<String> getClanAllies(){
        return clanAllies;
    }

    public void addClanAlly(String ally){
        clanAllies.add(ally);
    }

    public void removeClanAlly(String allyUUID){
        clanAllies.remove(allyUUID);
    }

    public void setClanAllies(ArrayList<String> clanAlliesList){
        clanAllies = clanAlliesList;
    }

    public void addClanEnemy(String enemy){
        clanEnemies.add(enemy);
    }

    public void removeClanEnemy(String enemyUUID){
        clanEnemies.remove(enemyUUID);
    }

    public void setClanEnemies(ArrayList<String> clanEnemiesList){
        clanEnemies = clanEnemiesList;
    }

    public ArrayList<String> getClanEnemies(){
        return clanEnemies;
    }

    public boolean isFriendlyFireAllowed(){
        return friendlyFire;
    }

    public void setFriendlyFireAllowed(boolean friendlyFire){
        this.friendlyFire = friendlyFire;
    }

    public int getClanPoints() {
        return clanPoints;
    }

    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    public String getClanHomeWorld(){
        return clanHomeWorld;
    }

    public void setClanHomeWorld(String clanHomeWorld){
        this.clanHomeWorld = clanHomeWorld;
    }

    public double getClanHomeX(){
        return clanHomeX;
    }

    public void setClanHomeX(double clanHomeX){
        this.clanHomeX = clanHomeX;
    }

    public double getClanHomeY(){
        return clanHomeY;
    }

    public void setClanHomeY(double clanHomeY){
        this.clanHomeY = clanHomeY;
    }

    public double getClanHomeZ(){
        return clanHomeZ;
    }

    public void setClanHomeZ(double clanHomeZ){
        this.clanHomeZ = clanHomeZ;
    }

    public float getClanHomeYaw(){
        return clanHomeYaw;
    }

    public void setClanHomeYaw(float clanHomeYaw){
        this.clanHomeYaw = clanHomeYaw;
    }

    public float getClanHomePitch(){
        return clanHomePitch;
    }

    public void setClanHomePitch(float clanHomePitch){
        this.clanHomePitch = clanHomePitch;
    }

    public int getMaxAllowedProtectedChests() {
        return maxAllowedProtectedChests;
    }

    public void setMaxAllowedProtectedChests(int maxAllowedProtectedChests) {
        this.maxAllowedProtectedChests = maxAllowedProtectedChests;
    }

    public HashMap<String, Chest> getProtectedChests() {
        return protectedChests;
    }

    public void setProtectedChests(HashMap<String, Chest> protectedChests) {
        this.protectedChests = protectedChests;
    }
}
