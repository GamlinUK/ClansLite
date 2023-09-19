package me.loving11ish.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.loving11ish.clans.Clans;
import me.loving11ish.clans.api.ClanChatMessageSendEvent;
import me.loving11ish.clans.models.Clan;
import me.loving11ish.clans.models.ClanPlayer;
import me.loving11ish.clans.utils.ClansStorageUtil;
import me.loving11ish.clans.utils.ColorUtils;
import me.loving11ish.clans.utils.UsermapStorageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClanChatCommand implements CommandExecutor {
    
    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration clansConfig = Clans.getPlugin().getConfig();
    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    private static final String TIME_LEFT = "%TIMELEFT%";
    private static final String CLAN_PLACEHOLDER = "%CLAN%";

    HashMap<UUID, Long> chatCoolDownTimer = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (!(clansConfig.getBoolean("clan-chat.enabled"))){
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")));
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(ColorUtils.translateColorCodes(
                        "&6ClansLite clan chat usage:&3" +
                                "\n/clanchat <message>"
                ));
                return true;

            }else {
                ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                ArrayList<Player> playersWithSpyPerms = new ArrayList<>();
                for (Player p : onlinePlayers) {
                    ClanPlayer clanPlayer = UsermapStorageUtil.getClanPlayerByBukkitPlayer(p);
                    if (clanPlayer.getCanChatSpy() && p.hasPermission("clanslite.chat.spy")) {
                        playersWithSpyPerms.add(p);
                    }
                }

                Clan clanByMember = ClansStorageUtil.findClanByPlayer(player);
                Clan clanByOwner = ClansStorageUtil.findClanByOwner(player);

                String chatSpyPrefix = clansConfig.getString("clan-chat.chat-spy.chat-spy-prefix");
                StringBuilder messageString = new StringBuilder();
                messageString.append(clansConfig.getString("clan-chat.chat-prefix")).append(" ");
                messageString.append("&d").append(player.getName()).append(":&r").append(" ");
                for (String arg : args) {
                    messageString.append(arg).append(" ");
                }

                if (clansConfig.getBoolean("clan-chat.cool-down.enabled")) {
                    if (chatCoolDownTimer.containsKey(uuid)) {
                        if (!(player.hasPermission("clanslite.bypass.chatcooldown") || player.hasPermission("clanslite.bypass.*")
                                || player.hasPermission("clanslite.bypass") || player.hasPermission("clanslite.*") || player.isOp())) {
                            if (chatCoolDownTimer.get(uuid) > System.currentTimeMillis()) {

                                //If player still has time left on cool down
                                Long timeLeft = (chatCoolDownTimer.get(uuid) - System.currentTimeMillis()) / 1000;
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("home-cool-down-timer-wait")
                                        .replace(TIME_LEFT, timeLeft.toString())));
                            } else {

                                //Add player to cool down and run message
                                chatCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-chat.cool-down.time") * 1000));
                                if (clanByMember != null) {
                                    ArrayList<String> playerClanMembers = clanByMember.getClanMembers();
                                    fireClanChatMessageSendEvent(player, clanByMember, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), playerClanMembers);
                                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                                    }
                                    for (String playerClanMember : playerClanMembers) {
                                        if (playerClanMember != null) {
                                            UUID memberUUID = UUID.fromString(playerClanMember);
                                            UUID ownerUUID = UUID.fromString(clanByMember.getClanOwner());
                                            Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                            Player playerClanOwner = Bukkit.getPlayer(ownerUUID);
                                            if (playerClanPlayer != null) {
                                                if (playerClanOwner != null) {
                                                    playerClanOwner.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                                }
                                                playerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                                if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                                    for (Player p : playersWithSpyPerms) {
                                                        p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                }
                                if (clanByOwner != null) {
                                    ArrayList<String> ownerClanMembers = clanByOwner.getClanMembers();
                                    fireClanChatMessageSendEvent(player, clanByOwner, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), ownerClanMembers);
                                    if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                                    }
                                    for (String ownerClanMember : ownerClanMembers) {
                                        if (ownerClanMember != null) {
                                            UUID memberUUID = UUID.fromString(ownerClanMember);
                                            Player ownerClanPlayer = Bukkit.getPlayer(memberUUID);
                                            if (ownerClanPlayer != null) {
                                                ownerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                                player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                                if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                                    for (Player p : playersWithSpyPerms) {
                                                        p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                    player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                } else {
                                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-clan")));
                                }
                            }
                        } else {

                            //If player has cool down bypass
                            if (clanByMember != null) {
                                ArrayList<String> playerClanMembers = clanByMember.getClanMembers();
                                fireClanChatMessageSendEvent(player, clanByMember, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), playerClanMembers);
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                                }
                                for (String playerClanMember : playerClanMembers) {
                                    if (playerClanMember != null) {
                                        UUID memberUUID = UUID.fromString(playerClanMember);
                                        UUID ownerUUID = UUID.fromString(clanByMember.getClanOwner());
                                        Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                        Player playerClanOwner = Bukkit.getPlayer(ownerUUID);
                                        if (playerClanPlayer != null) {
                                            if (playerClanOwner != null) {
                                                playerClanOwner.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                            }
                                            playerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                            if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                                for (Player p : playersWithSpyPerms) {
                                                    p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                                }
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                            if (clanByOwner != null) {
                                ArrayList<String> ownerClanMembers = clanByOwner.getClanMembers();
                                fireClanChatMessageSendEvent(player, clanByOwner, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), ownerClanMembers);
                                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                                }
                                for (String ownerClanMember : ownerClanMembers) {
                                    if (ownerClanMember != null) {
                                        UUID memberUUID = UUID.fromString(ownerClanMember);
                                        Player ownerClanPlayer = Bukkit.getPlayer(memberUUID);
                                        if (ownerClanPlayer != null) {
                                            ownerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                            player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                            if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                                for (Player p : playersWithSpyPerms) {
                                                    p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                                }
                                            }
                                            return true;
                                        }
                                    }
                                }
                                player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                            } else {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-clan")));
                            }
                        }
                    } else {

                        //If player not in cool down hashmap
                        chatCoolDownTimer.put(uuid, System.currentTimeMillis() + (clansConfig.getLong("clan-chat.cool-down.time") * 1000));
                        if (clanByMember != null) {
                            ArrayList<String> playerClanMembers = clanByMember.getClanMembers();
                            fireClanChatMessageSendEvent(player, clanByMember, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), playerClanMembers);
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                            }
                            for (String playerClanMember : playerClanMembers) {
                                if (playerClanMember != null) {
                                    UUID memberUUID = UUID.fromString(playerClanMember);
                                    UUID ownerUUID = UUID.fromString(clanByMember.getClanOwner());
                                    Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                    Player playerClanOwner = Bukkit.getPlayer(ownerUUID);
                                    if (playerClanPlayer != null) {
                                        if (playerClanOwner != null) {
                                            playerClanOwner.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                        }
                                        playerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                        if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                            for (Player p : playersWithSpyPerms) {
                                                p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                        if (clanByOwner != null) {
                            ArrayList<String> ownerClanMembers = clanByOwner.getClanMembers();
                            fireClanChatMessageSendEvent(player, clanByOwner, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), ownerClanMembers);
                            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                            }
                            for (String ownerClanMember : ownerClanMembers) {
                                if (ownerClanMember != null) {
                                    UUID memberUUID = UUID.fromString(ownerClanMember);
                                    Player ownerClanPlayer = Bukkit.getPlayer(memberUUID);
                                    if (ownerClanPlayer != null) {
                                        ownerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                        player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                        if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                            for (Player p : playersWithSpyPerms) {
                                                p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }
                            player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-clan")));
                        }
                    }
                } else {

                    //If cool down disabled
                    if (clanByMember != null) {
                        ArrayList<String> playerClanMembers = clanByMember.getClanMembers();
                        fireClanChatMessageSendEvent(player, clanByMember, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), playerClanMembers);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                        }
                        for (String playerClanMember : playerClanMembers) {
                            if (playerClanMember != null) {
                                UUID memberUUID = UUID.fromString(playerClanMember);
                                UUID ownerUUID = UUID.fromString(clanByMember.getClanOwner());
                                Player playerClanPlayer = Bukkit.getPlayer(memberUUID);
                                Player playerClanOwner = Bukkit.getPlayer(ownerUUID);
                                if (playerClanPlayer != null) {
                                    if (playerClanOwner != null) {
                                        playerClanOwner.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                    }
                                    playerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                    if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                        for (Player p : playersWithSpyPerms) {
                                            p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByMember.getClanPrefix())));
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                    if (clanByOwner != null) {
                        ArrayList<String> ownerClanMembers = clanByOwner.getClanMembers();
                        fireClanChatMessageSendEvent(player, clanByOwner, clansConfig.getString("clan-chat.chat-prefix"), messageString.toString(), ownerClanMembers);
                        if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired ClanChatMessageSendEvent"));
                        }
                        for (String ownerClanMember : ownerClanMembers) {
                            if (ownerClanMember != null) {
                                UUID memberUUID = UUID.fromString(ownerClanMember);
                                Player ownerClanPlayer = Bukkit.getPlayer(memberUUID);
                                if (ownerClanPlayer != null) {
                                    ownerClanPlayer.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                    player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                    if (clansConfig.getBoolean("clan-chat.chat-spy.enabled")) {
                                        for (Player p : playersWithSpyPerms) {
                                            p.sendMessage(ColorUtils.translateColorCodes(chatSpyPrefix + " " + messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                        player.sendMessage(ColorUtils.translateColorCodes(messageString.toString()).replace(CLAN_PLACEHOLDER, ColorUtils.translateColorCodes(clanByOwner.getClanPrefix())));
                    } else {
                        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-clan")));
                    }
                }
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")));
        }
        return true;
    }

    private static void fireClanChatMessageSendEvent(Player player, Clan clan, String prefix, String message, ArrayList<String> recipients) {
        ClanChatMessageSendEvent clanChatMessageSendEvent = new ClanChatMessageSendEvent(player, clan, prefix, message, recipients);
        Bukkit.getPluginManager().callEvent(clanChatMessageSendEvent);
    }
}
