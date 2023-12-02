package me.loving11ish.clans.commands.commandTabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClanAdminTabCompleter implements TabCompleter {

    private final List<String> arguments = new ArrayList<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (arguments.isEmpty()){
            arguments.add("save");
            arguments.add("reload");
            arguments.add("disband");
            arguments.add("about");

        }

        List<String> result = new ArrayList<>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        }
        return null;
    }
}
