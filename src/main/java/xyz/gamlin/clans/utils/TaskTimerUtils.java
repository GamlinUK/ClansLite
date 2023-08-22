package xyz.gamlin.clans.utils;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.gamlin.clans.Clans;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TaskTimerUtils {

    static ConsoleCommandSender console = Bukkit.getConsoleSender();
    static FileConfiguration config = Clans.getPlugin().getConfig();
    static FoliaLib foliaLib = new FoliaLib(Clans.getPlugin());

    public static WrappedTask autoSaveTask;
    public static WrappedTask inviteClearTask;

    public static void runClansAutoSave(){
        autoSaveTask = foliaLib.getImpl().runTimerAsync(() -> {
            try {
                ClansStorageUtil.saveClans();
                if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-save-task-message.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")));
                }
                if (config.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + autoSaveTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                }
            } catch (IOException e) {
                console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")));
                e.printStackTrace();
                if (config.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + autoSaveTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                }
            }

            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + autoSaveTask.toString()));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
            }
        }, 1L, 900L, TimeUnit.SECONDS);
    }

    public static void runClanInviteClear() {
        inviteClearTask = foliaLib.getImpl().runTimerAsync(() -> {
            try {
                ClanInviteUtil.emptyInviteList();
                if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-invite-wipe-message.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-complete")));
                }
                if (config.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + inviteClearTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                }
            } catch (UnsupportedOperationException exception){
                console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")));
                if (config.getBoolean("general.developer-debug-mode.enabled")){
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + inviteClearTask.toString()));
                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                }
            }
            runClanInviteClear();
            if (config.getBoolean("general.developer-debug-mode.enabled")){
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + inviteClearTask.toString()));
                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
            }
        }, 1L, 900L, TimeUnit.SECONDS);
    }

}
