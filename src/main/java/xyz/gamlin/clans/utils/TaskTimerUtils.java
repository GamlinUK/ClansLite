package xyz.gamlin.clans.utils;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.WrappedTask;
import xyz.gamlin.clans.Clans;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TaskTimerUtils {

    static Logger logger = Clans.getPlugin().getLogger();
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
                            logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")));
                        }
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")));
                        e.printStackTrace();
                    }
                    runClansAutoSaveTwo();
                    task1.cancel();
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
                            logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")));
                        }
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")));
                        e.printStackTrace();
                    }
                    runClansAutoSaveOne();
                    task2.cancel();
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
                            logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-complete")));
                        }
                    }catch (UnsupportedOperationException exception){
                        logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")));
                    }
                    runClanInviteClearTwo();
                    task3.cancel();
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
                            logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-complete")));
                        }
                    }catch (UnsupportedOperationException exception){
                        logger.info(ColorUtils.translateColorCodes(Clans.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")));
                    }
                    runClanInviteClearOne();
                    task4.cancel();
                    return;
                }else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }
}
