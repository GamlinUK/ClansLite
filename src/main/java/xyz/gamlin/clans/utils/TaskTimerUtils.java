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

    public static WrappedTask task1;
    public static WrappedTask task2;
    public static WrappedTask task3;
    public static WrappedTask task4;

    public static void runClansAutoSaveOne(){
        task1 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            int time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClansStorageUtil.saveClans();
                        if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-save-task-message.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")));
                        }
                        runClansAutoSaveTwo();
                        task1.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task1.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                        return;
                    } catch (IOException e) {
                        console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")));
                        e.printStackTrace();
                        runClansAutoSaveTwo();
                        task1.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task1.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                    }
                    runClansAutoSaveTwo();
                    task1.cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task1.toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                    }
                    return;
                }
                else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public static void runClansAutoSaveTwo(){
        task2 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            int time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClansStorageUtil.saveClans();
                        if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-save-task-message.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")));
                        }
                        runClansAutoSaveOne();
                        task2.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task2.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                        return;
                    } catch (IOException e) {
                        console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")));
                        e.printStackTrace();
                        runClansAutoSaveOne();
                        task2.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task2.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                    }
                    runClansAutoSaveOne();
                    task2.cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task2.toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                    }
                    return;
                }
                else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public static void runClanInviteClearOne(){
        task3 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            int time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClanInviteUtil.emptyInviteList();
                        if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-invite-wipe-message.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-complete")));
                        }
                        runClanInviteClearTwo();
                        task3.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task3.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                        return;
                    }catch (UnsupportedOperationException exception){
                        console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")));
                        runClanInviteClearTwo();
                        task3.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task3.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                    }
                    runClanInviteClearTwo();
                    task3.cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task3.toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                    }
                    return;
                }else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public static void runClanInviteClearTwo(){
        task4 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            int time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClanInviteUtil.emptyInviteList();
                        if (Clans.getPlugin().getConfig().getBoolean("general.show-auto-invite-wipe-message.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-complete")));
                        }
                        runClanInviteClearOne();
                        task4.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task4.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                        return;
                    }catch (UnsupportedOperationException exception){
                        console.sendMessage(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")));
                        runClanInviteClearOne();
                        task4.cancel();
                        if (config.getBoolean("general.developer-debug-mode.enabled")){
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task4.toString()));
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                        }
                    }
                    runClanInviteClearOne();
                    task4.cancel();
                    if (config.getBoolean("general.developer-debug-mode.enabled")){
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aWrapped task: " + task4.toString()));
                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aTimed task canceled successfully"));
                    }
                    return;
                }else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }
}
