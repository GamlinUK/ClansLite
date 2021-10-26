package xyz.gamlin.clans.utils;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ClansStorageUtil {

    private static ArrayList<Clan> clans = new ArrayList<>();

    public static Clan createClan(Player player, String clanName) {
        if (findClanByPlayer(player) != null) {
            return null;
        }
        if (findClanByName(clanName) != null) {
            return null;
        }

        Clan clan = new Clan(player.getUniqueId(), clanName);
        clans.add(clan);
        try {
            saveClans();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clan;
    }

    public static boolean deleteClan(Player player) {
        for (Clan clan : clans) {
            if (clan.getClanOwner().equals(player.getUniqueId())) {
                clans.remove(clan);
                return true;
            }
        }
        try {
            saveClans();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addClanMember(Clan clan, Player player) {
        if (clan.addClanMember(player.getUniqueId())) {
            try {
                saveClans();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }

    public static Clan findClanByOwner(Player player) {
        for (Clan clan : clans) {
            if (clan.getClanOwner().equals(player.getUniqueId())) {
                return clan;
            }
        }
        return null;
    }

    public static Clan findClanByPlayer(Player player) {
        for (Clan clan : clans) {
            if (findClanByOwner(player) != null) {
                return clan;
            }
            if (clan.getClanMembers() != null) {
                for (UUID member : clan.getClanMembers()) {
                    if (member.equals(player.getUniqueId())) {
                        return clan;
                    }
                }
            }
        }
        return null;
    }

    public static Clan findClanByName(String name) {
        for (Clan clan : clans) {
            if (clan.getClanName().equals(name)) {
                return clan;
            }
        }
        return null;
    }

    public static boolean updatePrefix(Player player, String newPrefix) {
        Clan playerClan = findClanByOwner(player);
        if (playerClan != null) {
            playerClan.setClanPrefix(newPrefix);
            try {
                saveClans();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void saveClans() throws IOException {
        Gson gson = new Gson();
        File file = new File(Clans.getPlugin().getDataFolder().getAbsolutePath() + "/clans.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(clans, writer);
        writer.flush();
        writer.close();
    }

    public static ArrayList<Clan> getClansRaw() {
        return clans;
    }

    public static ArrayList getClans() {
        ArrayList clanData = new ArrayList();
        for (Clan clan : clans) {
            String clanName = clan.getClanName();
            ArrayList<UUID> clanSize = clan.getClanMembers();
            String clanSizeString = "";
            if (clanSize == null) {
                clanSizeString = MessageFormat.format("{0} §3member", 1);
            } else {
                clanSizeString = MessageFormat.format("{0} §3members", clanSize.size() + 1);
            }
            String clanInfo = MessageFormat.format("§6{0} §3 - {1}", clanName, clanSizeString);
            clanData.add(clanInfo);
        }
        return clanData;
    }

    public static Boolean playerIsInClan(Player player) {
        return findClanByPlayer(player) != null;
    }

    public static void loadClans() throws IOException {
        Gson gson = new Gson();
        File file = new File(Clans.getPlugin().getDataFolder().getAbsolutePath() + "/clans.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Clan[] n = gson.fromJson(reader, Clan[].class);
            clans = new ArrayList<>(Arrays.asList(n));
        }

    }

}
