package me.loving11ish.clans.updatesystem;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.utils.ColorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private int resourceId;

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        FoliaLib foliaLib = Clans.getFoliaLib();
        foliaLib.getImpl().runAsync((task) -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-check-failure") + exception.getMessage()));
            }
        });
    }
}
