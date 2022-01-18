package xyz.gamlin.clans.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ColorUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ClansFileManager {

    private Clans plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    Logger logger = Clans.getPlugin().getLogger();

    public void ClansFileManager(Clans plugin){
        this.plugin = plugin;
        saveDefaultClansConfig();
    }

    public void reloadClansConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "clans.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("clans.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getClansConfig(){
        if (this.dataConfig == null){
            this.reloadClansConfig();
        }
        return this.dataConfig;
    }

    public void saveClansConfig() {
        if (this.dataConfig == null||this.configFile == null){
            return;
        }
        try {
            this.getClansConfig().save(this.configFile);
        }catch (IOException e){
            logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save clans.yml"));
            logger.severe(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"));
            e.printStackTrace();
        }
    }

    public void saveDefaultClansConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "clans.yml");
        }
        if (!this.configFile.exists()){
            this.plugin.saveResource("clans.yml", false);
        }
    }
}
