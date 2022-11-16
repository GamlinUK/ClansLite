package xyz.gamlin.clans.models;

public class ClanPlayer {

    private String javaUUID;
    private String lastPlayerName;
    private boolean isBedrockPlayer;
    private String bedrockUUID;
    private int pointBalance;

    public ClanPlayer(String UUID, String playerName) {
        javaUUID = UUID;
        lastPlayerName = playerName;
        isBedrockPlayer = false;
        bedrockUUID = null;
        pointBalance = 0;
    }

    public String getJavaUUID() {
        return javaUUID;
    }

    public void setJavaUUID(String javaUUID) {
        this.javaUUID = javaUUID;
    }

    public String getLastPlayerName() {
        return lastPlayerName;
    }

    public void setLastPlayerName(String lastPlayerName) {
        this.lastPlayerName = lastPlayerName;
    }

    public int getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(int pointBalance) {
        this.pointBalance = pointBalance;
    }

    public boolean isBedrockPlayer() {
        return isBedrockPlayer;
    }

    public void setBedrockPlayer(boolean bedrockPlayer) {
        isBedrockPlayer = bedrockPlayer;
    }

    public String getBedrockUUID() {
        return bedrockUUID;
    }

    public void setBedrockUUID(String bedrockUUID) {
        this.bedrockUUID = bedrockUUID;
    }
}
