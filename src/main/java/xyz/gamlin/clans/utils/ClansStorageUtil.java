package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.api.ClanDisbandEvent;
import xyz.gamlin.clans.api.ClanOfflineDisbandEvent;
import xyz.gamlin.clans.api.ClanTransferOwnershipEvent;
import xyz.gamlin.clans.models.Chest;
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
            if (entry.getValue().getMaxAllowedProtectedChests() > 0){
                HashMap<String, Chest> chests = entry.getValue().getProtectedChests();
                clansStorage.set("clans.data." + entry.getKey() + ".maxAllowedProtectedChests", entry.getValue().getMaxAllowedProtectedChests());
                for (Map.Entry<String, Chest> chestLocation : chests.entrySet()){
                    if (chestLocation.getValue().getChestWorldName() == null){
                        logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("chest-location-save-failed-1")
                                .replace("%CLAN%", chestLocation.getValue().getChestWorldName().toString())));
                        logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("chest-location-save-failed-2")
                                .replace("%CLAN%", chestLocation.getValue().getChestWorldName().toString())));
                        continue;
                    }
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestUUID", chestLocation.getKey());
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestWorld", chestLocation.getValue().getChestWorldName());
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestX", chestLocation.getValue().getChestLocationX());
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestY", chestLocation.getValue().getChestLocationY());
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestZ", chestLocation.getValue().getChestLocationZ());
                    clansStorage.set("clans.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".playersWithAccess", chestLocation.getValue().getPlayersWithAccess());
                }
            }
        }
        Clans.getPlugin().clansFileManager.saveClansConfig();
    }

    public static void restoreClans() throws IOException {
        clansList.clear();
        clansStorage.getConfigurationSection("clans.data").getKeys(false).forEach(key ->{
            HashMap<String, Chest> protectedChests = new HashMap<>();
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
            int maxAllowedProtectedChests = clansStorage.getInt("clans.data." + key + ".maxAllowedProtectedChests");

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
            if (clanHomeWorld != null){
                clan.setClanHomeWorld(clanHomeWorld);
                clan.setClanHomeX(clanHomeX);
                clan.setClanHomeY(clanHomeY);
                clan.setClanHomeZ(clanHomeZ);
                clan.setClanHomeYaw(clanHomeYaw);
                clan.setClanHomePitch(clanHomePitch);
            }
            clan.setMaxAllowedProtectedChests(maxAllowedProtectedChests);

            ConfigurationSection chestSection = clansStorage.getConfigurationSection("clans.data." + key + ".protectedChests");
            if (chestSection != null){
                clansStorage.getConfigurationSection("clans.data." + key + ".protectedChests").getKeys(false).forEach(configChest -> {
                    String chestUUID = clansStorage.getString("clans.data." + key + ".protectedChests." + configChest + ".chestUUID");
                    String chestWorld = clansStorage.getString("clans.data." + key + ".protectedChests." + configChest + ".chestWorld");
                    double chestX = clansStorage.getDouble("clans.data." + key + ".protectedChests." + configChest + ".chestX");
                    double chestY = clansStorage.getDouble("clans.data." + key + ".protectedChests." + configChest + ".chestY");
                    double chestZ = clansStorage.getDouble("clans.data." + key + ".protectedChests." + configChest + ".chestZ");
                    List<String> playersWithAccessConfigSection = clansStorage.getStringList("clans.data." + key + ".protectedChests." + configChest + ".playersWithAccess");
                    ArrayList<String> playersWithAccess = new ArrayList<>(playersWithAccessConfigSection);
                    World world = Bukkit.getWorld(chestWorld);
                    if (world != null){
                        Location location = new Location(world, chestX, chestY, chestZ);
                        Chest chest = new Chest(clan, location);
                        chest.setUUID(chestUUID);
                        chest.setChestLocationX(chestX);
                        chest.setChestLocationY(chestY);
                        chest.setChestLocationZ(chestZ);
                        chest.setPlayersWithAccess(playersWithAccess);
                        protectedChests.put(chestUUID, chest);
                    }else {
                        logger.warning(ColorUtils.translateColorCodes(messagesConfig.getString("chest-location-load-failed")
                                .replace("%WORLD%", chestWorld).replace("%CLAN%", clanFinalName)));
                    }
                });
            }

            clan.setProtectedChests(protectedChests);

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
            if (isClanOwner(originalClanOwner)){
                if (!isClanOwner(newClanOwner) && findClanByPlayer(newClanOwner) == null){
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
                    int maxAllowedProtectedChests = originalClan.getMaxAllowedProtectedChests();
                    HashMap<String, Chest> protectedChests = originalClan.getProtectedChests();

                    Clan newClan = new Clan(newOwnerUUID.toString(), clanFinalName);
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
                    newClan.setMaxAllowedProtectedChests(maxAllowedProtectedChests);
                    newClan.setProtectedChests(protectedChests);

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
                }else {
                    originalClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-ownership-transfer-failed-target-in-clan")));
                }
            }else {
                originalClanOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-must-be-owner")));
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

    public static boolean withdrawPoints(Clan clan, int points){
        int currentPointValue = clan.getClanPoints();
        if (currentPointValue != 0){
            if (hasEnoughPoints(clan, points)){
                clan.setClanPoints(currentPointValue - points);
                return true;
            }
            return false;
        }
        return false;
    }

    public static void setPoints(Clan clan, int points){
        clan.setClanPoints(points);
    }

    public static void resetPoints(Clan clan){
        clan.setClanPoints(0);
    }

    public static Location getChestLocation(Chest chest){
        String worldName = chest.getChestWorldName();
        double chestX = chest.getChestLocationX();
        double chestY = chest.getChestLocationY();
        double chestZ = chest.getChestLocationZ();
        World world = Bukkit.getWorld(worldName);
        if (world != null){
            return new Location(world, chestX, chestY, chestZ);
        }
        return null;
    }

    public static boolean isChestLocked(Clan clan, Location location){
        HashMap<String, Chest> clanChestList = clan.getProtectedChests();
        for (Map.Entry<String, Chest> chest : clanChestList.entrySet()){
            Location chestLocation = getChestLocation(chest.getValue());
            if (chestLocation != null){
                if (chestLocation.equals(location)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isChestLocked(Location location){
        List<Chest> allChests = getGlobalLockedChests();
        for (Chest chest : allChests){
            if (chest != null){
                String worldName = chest.getChestWorldName();
                World world = Bukkit.getWorld(worldName);
                Location chestLocation = new Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ());
                if (chestLocation.equals(location)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean addProtectedChest(Clan clan, Location location, Player player){
        HashMap<String, Chest> clanChestList = clan.getProtectedChests();
        if (isChestLocked(clan, location)){
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-chest-already-protected")));
            return false;
        }else {
            UUID newChestUUID = UUID.randomUUID();
            String chestUUID = newChestUUID.toString();
            Chest chest = new Chest(clan, location);
            clanChestList.put(chestUUID, chest);
            return true;
        }
    }

    public static boolean removeProtectedChest(String clanOwnerUUID, Location location) throws IOException{
        UUID uuid = UUID.fromString(clanOwnerUUID);
        Clan clan = findClanByOfflineOwner(Bukkit.getOfflinePlayer(uuid));
        String key = clan.getClanOwner();
        HashMap<String, Chest> clanChestList = clan.getProtectedChests();
        if (isChestLocked(clan, location)){
            for (Map.Entry<String, Chest> chest : clanChestList.entrySet()){
                String worldName = chest.getValue().getChestWorldName();
                World world = Bukkit.getWorld(worldName);
                Location chestLocation = new Location(world, chest.getValue().getChestLocationX(), chest.getValue().getChestLocationY(), chest.getValue().getChestLocationZ());
                if (chestLocation.equals(getChestByLocation(clan, location))){
                    String chestUUID = chest.getKey();
                    clanChestList.remove(chestUUID);
                    clan.setProtectedChests(clanChestList);
                    clansList.replace(UUID.fromString(clan.getClanOwner()), clan);
                    clansStorage.set("clans.data." + key + ".protectedChests." + chestUUID, null);
                    Clans.getPlugin().clansFileManager.saveClansConfig();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean removeProtectedChest(Clan clan, Location location, Player player) throws IOException{
        String key = clan.getClanOwner();
        if (isChestLocked(clan, location)){
            if (removeProtectedChest(key, location)){
                return true;
            }
        }else {
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-chest-not-protected")));
            return false;
        }
        return false;
    }

    public static boolean removeProtectedChest(String clanOwnerUUID, Location location, Player player) throws IOException{
        UUID uuid = UUID.fromString(clanOwnerUUID);
        Clan clan = findClanByOfflineOwner(Bukkit.getOfflinePlayer(uuid));
        if (removeProtectedChest(clan, location, player)){
            return true;
        }
        return false;
    }

    public static Set<Map.Entry<String, Chest>> getAllProtectedChestsByClan(Clan clan){
        return clan.getProtectedChests().entrySet();
    }

    public static Location getChestByLocation(Clan clan, Location location){
        HashMap<String, Chest> clanChestList = clan.getProtectedChests();
        for (Map.Entry<String, Chest> chest : clanChestList.entrySet()){
            String worldName = chest.getValue().getChestWorldName();
            World world = Bukkit.getWorld(worldName);
            Location chestLocation = new Location(world, chest.getValue().getChestLocationX(), chest.getValue().getChestLocationY(), chest.getValue().getChestLocationZ());
            if (chestLocation.equals(location)){
                return chestLocation;
            }
        }
        return null;
    }

    public static Chest getChestByLocation(Location location){
        List<Chest> allChests = getGlobalLockedChests();
        for (Chest chest : allChests){
            String worldName = chest.getChestWorldName();
            World world = Bukkit.getWorld(worldName);
            Location chestLocation = new Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ());
            if (location.equals(chestLocation)){
                return chest;
            }
        }
        return null;
    }

    public static List<Location> getAllProtectedChestsLocationsByClan(Clan clan){
        HashMap<String, Chest> clanChestList = clan.getProtectedChests();
        List<Location> allChestLocations = new ArrayList<>();
        for (Map.Entry<String, Chest> chest : clanChestList.entrySet()){
            String worldName = chest.getValue().getChestWorldName();
            World world = Bukkit.getWorld(worldName);
            Location chestLocation = new Location(world, chest.getValue().getChestLocationX(), chest.getValue().getChestLocationY(), chest.getValue().getChestLocationZ());
            allChestLocations.add(chestLocation);
        }
        return allChestLocations;
    }

    public static List<String> getPlayersWithChestAccessByChest(Chest chest){
        return chest.getPlayersWithAccess();
    }

    public static List<OfflinePlayer> getOfflinePlayersWithChestAccessByChest(Chest chest){
        List<String> playersWithAccess = getPlayersWithChestAccessByChest(chest);
        List<OfflinePlayer> offlinePlayersWithAccess = new ArrayList<>();
        for (String string : playersWithAccess){
            UUID uuid = UUID.fromString(string);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            offlinePlayersWithAccess.add(offlinePlayer);
        }
        return offlinePlayersWithAccess;
    }

    public static boolean hasAccessToLockedChest(OfflinePlayer offlinePlayer, Chest chest){
        return getOfflinePlayersWithChestAccessByChest(chest).contains(offlinePlayer);
    }

    public static List<Location> getGlobalLockedChestLocations(){
        List<Chest> allLockedChest = getGlobalLockedChests();
        List<Location> allLockedChestLocations = new ArrayList<>();
        for (Chest chest : allLockedChest){
            String worldName = chest.getChestWorldName();
            World world = Bukkit.getWorld(worldName);
            Location location = new Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ());
            allLockedChestLocations.add(location);
        }
        return allLockedChestLocations;
    }

    public static List<Chest> getGlobalLockedChests(){
        List<Chest> allLockedChests = new ArrayList<>();
        for (Clan clan : clansList.values()){
            for (Map.Entry<String, Chest> chests : clan.getProtectedChests().entrySet()){
                allLockedChests.add(chests.getValue());
            }
        }
        return allLockedChests;
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
