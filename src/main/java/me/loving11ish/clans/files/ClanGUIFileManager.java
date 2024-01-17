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

public class ClanGUIFileManager {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private Clans plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void ClanGUIFileManager(Clans plugin){
        this.plugin = plugin;
        saveDefaultClanGUIConfig();
    }

    public void reloadClanGUIConfig(){

        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "clangui.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("clangui.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getClanGUIConfig(){
        if (this.dataConfig == null){
            this.reloadClanGUIConfig();
        }
        return this.dataConfig;
    }

    public void saveClanGUIConfig() {
        if (this.dataConfig == null||this.configFile == null){
            return;
        }
        try {
            this.getClanGUIConfig().save(this.configFile);
        }catch (IOException e){
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save clangui.yml"));
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"));
            e.printStackTrace();
        }
    }

    public void saveDefaultClanGUIConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "clangui.yml");
        }
        if (!this.configFile.exists()){
            this.plugin.saveResource("clangui.yml", false);
        }
    }
}
