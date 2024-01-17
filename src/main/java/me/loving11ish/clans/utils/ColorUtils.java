package me.loving11ish.clans.utils;

import org.bukkit.ChatColor;

public class ColorUtils {

    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */

    public static String translateColorCodes(String text){
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; i++){
            if (texts[i].equalsIgnoreCase("&")){
                i++;
                if (texts[i].charAt(0) == '#'){
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                }else{
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            }else{
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }
}
