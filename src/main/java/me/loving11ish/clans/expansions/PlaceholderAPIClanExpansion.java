package me.loving11ish.clans.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.models.ClanPlayer;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

public class PlaceholderAPIClanExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "clansLite";
    }

    @Override
    public @NotNull String getAuthor() {
        return Clans.getPlugin().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return Clans.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        FileConfiguration configFile = Clans.getPlugin().getConfig();
        Clan clanOwner = ClansStorageUtil.findClanByOfflineOwner(player);
        Clan clanMember = ClansStorageUtil.findClanByOfflinePlayer(player);
        ClanPlayer clanPlayer = UsermapStorageUtil.getClanPlayerByBukkitOfflinePlayer(player);

        if (params.equalsIgnoreCase("pluginVersion")) {
            //%clansLite_pluginVersion%
            return Clans.getPlugin().getDescription().getVersion();
        }

        if (params.equalsIgnoreCase("pluginAuthor")) {
            //%clansLite_pluginAuthor%
            return Clans.getPlugin().getDescription().getAuthors().get(0);
        }

        if (params.equalsIgnoreCase("baseServerVersion")) {
            //%clansLite_baseServerVersion%
            return String.valueOf(Clans.getVersionCheckerUtils().getVersion());
        }

        if (params.equalsIgnoreCase("serverPackage")) {
            //%clansLite_serverPackage%
            return Clans.getVersionCheckerUtils().getServerPackage();
        }

        if (params.equalsIgnoreCase("clanName")){
            //%clansLite_clanName%
            if (clanOwner != null){
                return ColorUtils.translateColorCodes(clanOwner.getClanFinalName() + "&r ");
            }else if (clanMember != null){
                return ColorUtils.translateColorCodes(clanMember.getClanFinalName() + "&r ");
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanPrefix")){
            //%clansLite_clanPrefix%
            String openBracket = configFile.getString("clan-tags.brackets-opening");
            String closeBracket = configFile.getString("clan-tags.brackets-closing");
            if (clanOwner != null){
                if (configFile.getBoolean("clan-tags.prefix-add-brackets")){
                    if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                        return ColorUtils.translateColorCodes(openBracket + clanOwner.getClanPrefix() + closeBracket +"&r ");
                    }else {
                        return ColorUtils.translateColorCodes(openBracket + clanOwner.getClanPrefix() + closeBracket +"&r");
                    }
                }else {
                    if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                        return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r ");
                    }else {
                        return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r");
                    }
                }
            }else if (clanMember != null){
                if (configFile.getBoolean("clan-tags.prefix-add-brackets")){
                    if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                        return ColorUtils.translateColorCodes(openBracket + clanMember.getClanPrefix() + closeBracket +"&r ");
                    }else {
                        return ColorUtils.translateColorCodes(openBracket + clanMember.getClanPrefix() + closeBracket +"&r");
                    }
                }else {
                    if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                        return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r ");
                    }else {
                        return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r");
                    }
                }
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("friendlyFire")){
            //%clansLite_friendlyFire%
            if (clanOwner != null){
                return String.valueOf(clanOwner.isFriendlyFireAllowed());
            }else if (clanMember != null){
                return String.valueOf(clanMember.isFriendlyFireAllowed());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanHomeSet")){
            //%clansLite_clanHomeSet%
            if (clanOwner != null){
                return String.valueOf(ClansStorageUtil.isHomeSet(clanOwner));
            }else if (clanMember != null){
                return String.valueOf(ClansStorageUtil.isHomeSet(clanMember));
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanMembersSize")){
            //%clansLite_clanMembersSize%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getClanMembers().size());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getClanMembers().size());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanAllySize")){
            //%clansLite_clanAllySize%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getClanAllies().size());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getClanAllies().size());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanEnemySize")){
            //%clansLite_clanEnemySize%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getClanEnemies().size());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getClanEnemies().size());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("playerPointBalance")){
            //%clansLite_playerPointBalance%
            if (clanPlayer != null){
                return String.valueOf(clanPlayer.getPointBalance());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanPointBalance")){
            //%clansLite_clanPointBalance%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getClanPoints());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getClanPoints());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanChestMaxAllowed")){
            //%clansLite_clanChestMaxAllowed%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getMaxAllowedProtectedChests());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getMaxAllowedProtectedChests());
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("clanChestCurrentLocked")){
            //%clansLite_clanChestCurrentLocked%
            if (clanOwner != null){
                return String.valueOf(clanOwner.getProtectedChests().size());
            }else if (clanMember != null){
                return String.valueOf(clanMember.getProtectedChests().size());
            }else {
                return "";
            }
        }
        return null;
    }
}
