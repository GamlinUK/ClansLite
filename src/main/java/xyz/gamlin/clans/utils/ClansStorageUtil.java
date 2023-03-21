package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.api.events.ClanDisbandEvent;
import xyz.gamlin.clans.api.events.ClanOfflineDisbandEvent;
import xyz.gamlin.clans.api.events.ClanTransferOwnershipEvent;
import xyz.gamlin.clans.models.Clan;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ClansStorageUtil {

    private static final Logger logger = Clans.getPlugin().getLogger();

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

    private static Map<UUID, Clan> clansList = new HashMap<>();

    private static final FileConfiguration clansStorage = Clans.getPlugin().clansFileManager.getClansConfig();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    private static final FileConfiguration clansConfig = Clans.getPlugin().getConfig();

    public static void saveClans() throws IOException {
        for (Map.Entry<UUID, Clan> entry : clansList.entrySet()){
            clansStorage.set("clans.data." + entry.getKey() + ".clanOwner", entry.getValue().getClanOwner());
            clansStorage.set("clans.data." + entry.getKey() + ".clanFinalName", entry.getValue().getClanFinalName());
            clansStorage.set("clans.data." + entry.getKey() + ".clanPrefix", entry.getValue().getClanPrefix());
            clansStorage.set("clans.data." + entry.getKey() + ".clanMembers", entry.getValue().getClanMembers());
            clansStorage.set("clans.data." + entry.getKey() + ".clanAllies", entry.getValue().getClanAllies());
            clansStorage.set("clans.data." + entry.getKey() + ".clanEnemies", entry.getValue().getClanEnemies());
            clansStorage.set("clans.data." + entry.getKey() + ".friendlyFire", entry.getValue().isFriendlyFireAllowed());
            clansStorage.set("clans.data." + entry.getKey() + ".clanPoints", entry.getValue().getClanPoints());
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
            int clanPoints = clansStorage.getInt("clans.data." + key + ".clanPoints");
            String clanHomeWorld = clansStorage.getString("clans.data." + key + ".clanHome.worldName");
            double clanHomeX = clansStorage.getDouble("clans.data." + key + ".clanHome.x");
            double clanHomeY = clansStorage.getDouble("clans.data." + key + ".clanHome.y");
            double clanHomeZ = clansStorage.getDouble("clans.data." + key + ".clanHome.z");
            float clanHomeYaw = (float) clansStorage.getDouble("clans.data." + key + ".clanHome.yaw");
            float clanHomePitch = (float) clansStorage.getDouble("clans.data." + key + ".clanHome.pitch");

            Clan clan = new Clan(key, clanFinalName);
            if (!clansStorage.getBoolean("name-strip-colour-complete")||clanFinalName.contains("&")||clanFinalName.contains("#")){
                clan.setClanFinalName(stripClanNameColorCodes(clan));
            }
            clan.setClanPrefix(clanPrefix);
            clan.setClanMembers(clanMembers);
            clan.setClanAllies(clanAllies);
            clan.setClanEnemies(clanEnemies);
            clan.setFriendlyFireAllowed(friendlyFire);
            clan.setClanPoints(clanPoints);
            clan.setClanHomeWorld(clanHomeWorld);
            clan.setClanHomeX(clanHomeX);
            clan.setClanHomeY(clanHomeY);
            clan.setClanHomeZ(clanHomeZ);
            clan.setClanHomeYaw(clanHomeYaw);
            clan.setClanHomePitch(clanHomePitch);

            clansList.put(uuid, clan);
        });
        if (!clansStorage.getBoolean("name-strip-colour-complete")){
            clansStorage.set("name-strip-colour-complete", true);
        }
    }

    public static Clan createClan(Player player, String clanName){
        UUID ownerUUID = player.getUniqueId();
        String ownerUUIDString = player.getUniqueId().toString();
        Clan newClan = new Clan(ownerUUIDString, clanName);
        clansList.put(ownerUUID, newClan);

        return newClan;
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
                if (clansList.containsKey(uuid)){
                    fireClanDisbandEvent(player);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanDisbandEvent"));
                    }
                    clansList.remove(uuid);
                    clansStorage.set("clans.data." + key, null);
                    Clans.getPlugin().clansFileManager.saveClansConfig();
                    return true;
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean deleteOfflineClan(OfflinePlayer offlinePlayer) throws IOException{
        UUID uuid = offlinePlayer.getUniqueId();
        String key = uuid.toString();
        if (findClanByOfflineOwner(offlinePlayer) != null){
            if (clansList.containsKey(uuid)){
                fireOfflineClanDisbandEvent(offlinePlayer);
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                    logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired OfflineClanDisbandEvent"));
                }
                clansList.remove(uuid);
                clansStorage.set("clans.data." + key, null);
                Clans.getPlugin().clansFileManager.saveClansConfig();
                return true;
            }else {
                logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                return false;
            }
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
        return clansList.get(uuid);
    }

    public static Clan findClanByOfflineOwner(OfflinePlayer offlinePlayer){
        UUID uuid = offlinePlayer.getUniqueId();
        return clansList.get(uuid);
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

    public static String stripClanNameColorCodes(Clan clan){
        String clanFinalName = clan.getClanFinalName();
        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")||!clansStorage.getBoolean("name-strip-colour-complete")
                ||clanFinalName.contains("&")||clanFinalName.contains("#")){
            logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFound Colour Code To Strip"));
            logger.info(ColorUtils.translateColorCodes("&aOriginal Name: ") + clanFinalName);
            logger.info(ColorUtils.translateColorCodes("&aNew Name: ") + (clanFinalName == null?null:STRIP_COLOR_PATTERN.matcher(clanFinalName).replaceAll("")));
        }
        return clanFinalName == null?null:STRIP_COLOR_PATTERN.matcher(clanFinalName).replaceAll("");
    }

    public static Clan transferClanOwner(Clan originalClan, Player originalClanOwner, Player newClanOwner) throws IOException{
        if (findClanByOwner(originalClanOwner) != null){
            if (!isClanOwner(originalClanOwner)){
                String originalOwnerKey = originalClanOwner.getUniqueId().toString();
                UUID originalOwnerUUID = originalClanOwner.getUniqueId();
                UUID newOwnerUUID = newClanOwner.getUniqueId();

                String clanFinalName = originalClan.getClanFinalName();
                String clanPrefix = originalClan.getClanPrefix();
                ArrayList<String> clanMembers = new ArrayList<>(originalClan.getClanMembers());
                ArrayList<String> clanAllies = new ArrayList<>(originalClan.getClanAllies());
                ArrayList<String> clanEnemies = new ArrayList<>(originalClan.getClanEnemies());
                boolean friendlyFire = originalClan.isFriendlyFireAllowed();
                int clanPoints = originalClan.getClanPoints();
                String clanHomeWorld = originalClan.getClanHomeWorld();
                double clanHomeX = originalClan.getClanHomeX();
                double clanHomeY = originalClan.getClanHomeY();
                double clanHomeZ = originalClan.getClanHomeZ();
                float clanHomeYaw = originalClan.getClanHomeYaw();
                float clanHomePitch = originalClan.getClanHomePitch();

                Clan newClan = new Clan(newClanOwner.getName(), clanFinalName);
                newClan.setClanPrefix(clanPrefix);
                newClan.setClanMembers(clanMembers);
                newClan.setClanAllies(clanAllies);
                newClan.setClanEnemies(clanEnemies);
                newClan.setFriendlyFireAllowed(friendlyFire);
                newClan.setClanPoints(clanPoints);
                newClan.setClanHomeWorld(clanHomeWorld);
                newClan.setClanHomeX(clanHomeX);
                newClan.setClanHomeY(clanHomeY);
                newClan.setClanHomeZ(clanHomeZ);
                newClan.setClanHomeYaw(clanHomeYaw);
                newClan.setClanHomePitch(clanHomePitch);

                clansList.put(newOwnerUUID, newClan);

                if (clansList.containsKey(originalOwnerUUID)){
                    fireClanTransferOwnershipEvent(originalClanOwner, newClanOwner, newClan);
                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")){
                        logger.info(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanTransferOwnershipEvent"));
                    }
                    clansList.remove(originalOwnerUUID);
                    clansStorage.set("clans.data." + originalOwnerKey, null);
                    Clans.getPlugin().clansFileManager.saveClansConfig();
                }else {
                    originalClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clans-update-error-1")));
                }
                return newClan;
            }
        }
        return null;
    }

    public static boolean hasEnoughPoints(Clan clan, int points){
        if (clan.getClanPoints() >= points){
            return true;
        }
        return false;
    }

    public static void addPoints(Clan clan, int points){
       int currentPointValue = clan.getClanPoints();
       clan.setClanPoints(currentPointValue + points);
    }

    public static void withdrawPoints(Clan clan, int points){
        int currentPointValue = clan.getClanPoints();
        if (currentPointValue != 0){
            if (hasEnoughPoints(clan, points)){
                clan.setClanPoints(currentPointValue - points);
            }
        }
    }

    public static void setPoints(Clan clan, int points){
        clan.setClanPoints(points);
    }

    public static void resetPoints(Clan clan){
        clan.setClanPoints(0);
    }

    public static Set<Map.Entry<UUID, Clan>> getClans(){
        return clansList.entrySet();
    }

    public static Set<UUID> getRawClansList(){
        return clansList.keySet();
    }

    public static Collection<Clan> getClanList(){
        return clansList.values();
    }

    private static void fireClanDisbandEvent(Player player) {
        Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);
        ClanDisbandEvent clanDisbandEvent = new ClanDisbandEvent(player, clanByOwner.getClanFinalName());
        Bukkit.getPluginManager().callEvent(clanDisbandEvent);
    }

    private static void fireOfflineClanDisbandEvent(OfflinePlayer offlinePlayer){
        Clan clanByOfflineOwner = ClansStorageUtil.findClanByOfflineOwner(offlinePlayer);
        ClanOfflineDisbandEvent clanOfflineDisbandEvent = new ClanOfflineDisbandEvent(offlinePlayer, clanByOfflineOwner.getClanFinalName());
        Bukkit.getPluginManager().callEvent(clanOfflineDisbandEvent);
    }

    private static void fireClanTransferOwnershipEvent(Player originalClanOwner, Player newClanOwner, Clan newClan){
        ClanTransferOwnershipEvent clanTransferOwnershipEvent = new ClanTransferOwnershipEvent(originalClanOwner, originalClanOwner, newClanOwner, newClan);
        Bukkit.getPluginManager().callEvent(clanTransferOwnershipEvent);
    }
}
