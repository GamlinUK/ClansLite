package xyz.gamlin.clans.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.models.ClanPlayer;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;
import xyz.gamlin.clans.utils.UsermapStorageUtil;

public class PlayerClanExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "clansLite";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Loving11ish";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.3.5";
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
            if (clanOwner != null){
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r ");
                }else {
                    return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r");
                }
            }else if (clanMember != null){
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r ");
                }else {
                    return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r");
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
        return null;
    }
}
