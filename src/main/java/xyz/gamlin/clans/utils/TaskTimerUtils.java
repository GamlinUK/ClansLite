package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.gamlin.clans.Clans;

import java.io.IOException;
import java.util.logging.Logger;

public class TaskTimerUtils {

    static Logger logger = Clans.getPlugin().getLogger();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    public static Integer taskID1;
    public static Integer taskID2;

    public static void runClansAutoSaveOne(){
        taskID1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Clans.getPlugin(Clans.class), new Runnable() {
            Integer time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClansStorageUtil.saveClans();
                        logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("auto-save-complete")));
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("auto-save-failed")));
                        e.printStackTrace();
                    }
                    runClansAutoSaveTwo();
                    Bukkit.getScheduler().cancelTask(taskID1);
                    return;
                }
                else {
                    time --;
                }
            }
        },0, 20);
    }

    public static void runClansAutoSaveTwo(){
        taskID2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Clans.getPlugin(Clans.class), new Runnable() {
            Integer time = 900;
            @Override
            public void run() {
                if (time == 1){
                    try {
                        ClansStorageUtil.saveClans();
                        logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("auto-save-complete")));
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("auto-save-failed")));
                        e.printStackTrace();
                    }
                    runClansAutoSaveOne();
                    Bukkit.getScheduler().cancelTask(taskID2);
                    return;
                }
                else {
                    time --;
                }
            }
        },0, 20);
    }
}
