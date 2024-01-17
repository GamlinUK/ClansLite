package me.loving11ish.clans.listeners;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;

public class PlayerMessageEvent implements Listener {

    private final FileConfiguration configFile = Clans.getPlugin().getConfig();

    @EventHandler
    public void onChatPlayer (AsyncPlayerChatEvent event) {
        String clanMergeTag = "{CLAN}";
        Player player = event.getPlayer();
        String format = event.getFormat();
        String openBracket = configFile.getString("clan-tags.brackets-opening");
        String closeBracket = configFile.getString("clan-tags.brackets-closing");
        if (ClansStorageUtil.findClanByPlayer(player) == null){
            format = StringUtils.replace(format, clanMergeTag, "");
        }
        else if (ClansStorageUtil.findClanByOwner(player) != null){
            Clan clan = ClansStorageUtil.findClanByOwner(player);
            if (configFile.getBoolean("clan-tags.prefix-add-brackets")){
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(openBracket + clan.getClanPrefix() + closeBracket + "&r "));
                }else {
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(openBracket + clan.getClanPrefix() + closeBracket + "&r"));
                }
                event.setFormat(format);
                return;
            }else {
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + " &r"));
                }else {
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + "&r"));
                }
                event.setFormat(format);
                return;
            }

        }else {
            Clan clan = ClansStorageUtil.findClanByPlayer(player);
            if (configFile.getBoolean("clan-tags.prefix-add-brackets")){
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(openBracket + clan.getClanPrefix() + closeBracket + "&r "));
                }else {
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(openBracket + clan.getClanPrefix() + closeBracket + "&r"));
                }
                event.setFormat(format);
                return;
            }else {
                if (configFile.getBoolean("clan-tags.prefix-add-space-after")){
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + " &r"));
                }else {
                    format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + "&r"));
                }
                event.setFormat(format);
                return;
            }
        }
        event.setFormat(format);
    }
}
