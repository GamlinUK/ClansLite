package xyz.gamlin.clans.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;

import java.io.IOException;
import java.util.*;

public class ClansStorageUtil {

    private static Map<UUID, Clan> clansList = new HashMap<>();

    private static final FileConfiguration clansStorage = Clans.getPlugin().clansFileManager.getClansConfig();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public static void saveClans() throws IOException {
        for (Map.Entry<UUID, Clan> entry : clansList.entrySet()){
            clansStorage.set("clans.data." + entry.getKey() + ".clanOwner", entry.getValue().getClanOwner());
            clansStorage.set("clans.data." + entry.getKey() + ".clanFinalName", entry.getValue().getClanFinalName());
            clansStorage.set("clans.data." + entry.getKey() + ".clanPrefix", entry.getValue().getClanPrefix());
            clansStorage.set("clans.data." + entry.getKey() + ".clanMembers", entry.getValue().getClanMembers());
            clansStorage.set("clans.data." + entry.getKey() + ".clanAllies", entry.getValue().getClanAllies());
            clansStorage.set("clans.data." + entry.getKey() + ".clanEnemies", entry.getValue().getClanEnemies());
            clansStorage.set("clans.data." + entry.getKey() + ".friendlyFire", entry.getValue().isFriendlyFireAllowed());
            if (entry.getValue().getClanHomeWorld() != null){
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.worldName", entry.getValue().getClanHomeWorld());
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.x", entry.getValue().getClanHomeX());
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.y", entry.getValue().getClanHomeY());
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.z", entry.getValue().getClanHomeZ());
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.yaw", entry.getValue().getClanHomeYaw());
                clansStorage.set("clans.data." + entry.getKey() + ".clanHome.pitch", entry.getValue().getClanHomePitch());
            }
        }
        Clans.getPlugin().clansFileManager.saveClansConfig();
    }

    public static void restoreClans() throws IOException {
        clansList.clear();
        clansStorage.getConfigurationSection("clans.data").getKeys(false).forEach(key ->{
            UUID uuid = UUID.fromString(key);
            String clanFinalName = clansStorage.getString("clans.data." + key + ".clanFinalName");
            String clanPrefix = clansStorage.getString("clans.data." + key + ".clanPrefix");
            List<String> clanMembersConfigSection = clansStorage.getStringList("clans.data." + key + ".clanMembers");
            List<String> clanAlliesConfigSection = clansStorage.getStringList("clans.data." + key + ".clanAllies");
            List<String> clanEnemiesConfigSection = clansStorage.getStringList("clans.data." + key + ".clanEnemies");
            ArrayList<String> clanMembers = new ArrayList<>(clanMembersConfigSection);
            ArrayList<String> clanAllies = new ArrayList<>(clanAlliesConfigSection);
            ArrayList<String> clanEnemies = new ArrayList<>(clanEnemiesConfigSection);
            boolean friendlyFire = clansStorage.getBoolean("clans.data." + key + ".friendlyFire");
            String clanHomeWorld = clansStorage.getString("clans.data." + key + ".clanHome.worldName");
            double clanHomeX = clansStorage.getDouble("clans.data." + key + ".clanHome.x");
            double clanHomeY = clansStorage.getDouble("clans.data." + key + ".clanHome.y");
            double clanHomeZ = clansStorage.getDouble("clans.data." + key + ".clanHome.z");
            float clanHomeYaw = (float) clansStorage.getDouble("clans.data." + key + ".clanHome.yaw");
            float clanHomePitch = (float) clansStorage.getDouble("clans.data." + key + ".clanHome.pitch");
            Clan clan = new Clan(key, clanFinalName);
            clan.setClanPrefix(clanPrefix);
            clan.setClanMembers(clanMembers);
            clan.setClanAllies(clanAllies);
            clan.setClanEnemies(clanEnemies);
            clan.setFriendlyFireAllowed(friendlyFire);
            clan.setClanHomeWorld(clanHomeWorld);
            clan.setClanHomeX(clanHomeX);
            clan.setClanHomeY(clanHomeY);
            clan.setClanHomeZ(clanHomeZ);
            clan.setClanHomeYaw(clanHomeYaw);
            clan.setClanHomePitch(clanHomePitch);
            clansList.put(uuid, clan);
        });
    }

    public static void createClan(Player player, String clanName){
        UUID ownerUUID = player.getUniqueId();
        String ownerUuidString = player.getUniqueId().toString();
        clansList.put(ownerUUID, new Clan(ownerUuidString, clanName));
    }

    public static boolean isClanExisting(Player player){
        UUID uuid = player.getUniqueId();
        if (clansList.containsKey(uuid)){
            return true;
        }
        return false;
    }

    public static boolean deleteClan(Player player) throws IOException{
        UUID uuid = player.getUniqueId();
        String key = uuid.toString();
        if (findClanByOwner(player) != null){
            if (isClanOwner(player)){
             clansList.remove(uuid);
             clansStorage.set("clans.data." + key, null);
             Clans.getPlugin().clansFileManager.saveClansConfig();
            }
            return true;
        }
        return false;
    }

    public static boolean isClanOwner(Player player){
        UUID uuid = player.getUniqueId();
        String ownerUUID = uuid.toString();
        Clan clan = clansList.get(uuid);
        if (clan != null){
            if (clan.getClanOwner() == null){
                return false;
            }else {
                if (clan.getClanOwner().equals(ownerUUID)){
                    return true;
                }
            }
        }
        return false;
    }

    public static Clan findClanByOwner(Player player){
        UUID uuid = player.getUniqueId();
        Clan clan = clansList.get(uuid);
        return clan;
    }

    public static Clan findClanByOfflineOwner(OfflinePlayer offlinePlayer){
        UUID uuid = offlinePlayer.getUniqueId();
        Clan clan = clansList.get(uuid);
        return clan;
    }

    public static Clan findClanByPlayer(Player player){
        for (Clan clan : clansList.values()){
            if (findClanByOwner(player) != null) {
                return clan;
            }
            if (clan.getClanMembers() != null) {
                for (String member : clan.getClanMembers()) {
                    if (member.equals(player.getUniqueId().toString())) {
                        return clan;
                    }
                }
            }
        }
        return null;
    }

    public static Clan findClanByOfflinePlayer(OfflinePlayer player){
        for (Clan clan : clansList.values()){
            if (findClanByOfflineOwner(player) != null){
                return clan;
            }
            if (clan.getClanMembers() != null){
                for (String member : clan.getClanMembers()){
                    if (member.equals(player.getUniqueId().toString())){
                        return clan;
                    }
                }
            }
        }
        return null;
    }

    public static void updatePrefix(Player player, String prefix){
        UUID uuid = player.getUniqueId();
        if (!isClanOwner(player)){
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
            return;
        }
        Clan clan = clansList.get(uuid);
        clan.setClanPrefix(prefix);
    }

    public static boolean addClanMember(Clan clan, Player player){
        UUID uuid = player.getUniqueId();
        String memberUUID = uuid.toString();
        clan.addClanMember(memberUUID);
        return true;
    }

    public static void addClanEnemy(Player clanOwner, Player enemyClanOwner){
        UUID ownerUUID = clanOwner.getUniqueId();
        UUID enemyUUID = enemyClanOwner.getUniqueId();
        String enemyOwnerUUID = enemyUUID.toString();
        Clan clan = clansList.get(ownerUUID);
        clan.addClanEnemy(enemyOwnerUUID);
    }

    public static void removeClanEnemy(Player clanOwner, Player enemyClanOwner){
        UUID ownerUUID = clanOwner.getUniqueId();
        UUID enemyUUID = enemyClanOwner.getUniqueId();
        String enemyOwnerUUID = enemyUUID.toString();
        Clan clan = clansList.get(ownerUUID);
        clan.removeClanEnemy(enemyOwnerUUID);
    }

    public static Set<Map.Entry<UUID, Clan>> getClans(){
        return clansList.entrySet();
    }

    public static Set<UUID> getRawClansList(){
        return clansList.keySet();
    }

    public static void addClanAlly(Player clanOwner, Player allyClanOwner){
        UUID ownerUUID = clanOwner.getUniqueId();
        UUID uuid = allyClanOwner.getUniqueId();
        String allyUUID = uuid.toString();
        Clan clan = clansList.get(ownerUUID);
        clan.addClanAlly(allyUUID);
    }

    public static void removeClanAlly(Player clanOwner, Player allyClanOwner){
        UUID ownerUUID = clanOwner.getUniqueId();
        UUID uuid = allyClanOwner.getUniqueId();
        String allyUUID = uuid.toString();
        Clan clan = clansList.get(ownerUUID);
        clan.removeClanAlly(allyUUID);
    }

    public static boolean isHomeSet(Clan clan){
        if (clan.getClanHomeWorld() != null){
            return true;
        }
        return false;
    }

    public static void deleteHome(Clan clan){
        String key = clan.getClanOwner();
        clan.setClanHomeWorld(null);
        clansStorage.set("clans.data." + key + ".clanHome", null);
        Clans.getPlugin().clansFileManager.saveClansConfig();
    }
}
