package me.loving11ish.clans.files;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UsermapFileManager {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private Clans plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void UsermapFileManager(Clans plugin){
        this.plugin = plugin;
        saveDefaultUsermapConfig();
    }

    public void reloadUsermapConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "usermap.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("usermap.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getUsermapConfig(){
        if (this.dataConfig == null){
            this.reloadUsermapConfig();
        }
        return this.dataConfig;
    }

    public void saveUsermapConfig() {
        if (this.dataConfig == null||this.configFile == null){
            return;
        }
        try {
            this.getUsermapConfig().save(this.configFile);
        }catch (IOException e){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save usermap.yml"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"));
            e.printStackTrace();
        }
    }

    public void saveDefaultUsermapConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "usermap.yml");
        }
        if (!this.configFile.exists()){
            this.plugin.saveResource("usermap.yml", false);
        }
    }
}
