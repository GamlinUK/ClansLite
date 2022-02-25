package xyz.gamlin.clans.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

public class PlayerClanExpansion extends PlaceholderExpansion {

    private final Clans plugin;

    public PlayerClanExpansion(Clans plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clansLite";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Gamlin & Loving11ish";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.4";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        Clan clanOwner = ClansStorageUtil.findClanByOfflineOwner(player);
        Clan clanMember = ClansStorageUtil.findClanByOfflinePlayer(player);
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
                return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r ");
            }else if (clanMember != null){
                return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r ");
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
        return null;
    }
}
