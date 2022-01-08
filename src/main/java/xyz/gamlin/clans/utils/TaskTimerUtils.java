package xyz.gamlin.clans.utils;

import org.bukkit.Bukkit;
import xyz.gamlin.clans.Clans;

import java.io.IOException;
import java.util.logging.Logger;

public class TaskTimerUtils {

    static Logger logger = Clans.getPlugin().getLogger();

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
                        logger.info("&6ClansLite: &aSaved all Clans to file!");
                    } catch (IOException e) {
                        logger.severe("&6ClansLite: &4Failed to save clans.json to file!");
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
                        logger.info("&6ClansLite: &aSaved all Clans to file!");
                    } catch (IOException e) {
                        logger.severe("&6ClansLite: &4Failed to save clans.json to file!");
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
