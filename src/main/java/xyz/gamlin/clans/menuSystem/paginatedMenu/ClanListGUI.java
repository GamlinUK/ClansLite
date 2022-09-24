package xyz.gamlin.clans.menuSystem.paginatedMenu;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.menuSystem.PaginatedMenu;
import xyz.gamlin.clans.menuSystem.PlayerMenuUtility;
import xyz.gamlin.clans.menuSystem.menu.ClanJoinRequestMenu;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;
import xyz.gamlin.clans.utils.ColorUtils;

import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ClanListGUI extends PaginatedMenu {

    public static Integer taskID5;

    FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();
    FileConfiguration guiConfig = Clans.getPlugin().clanGUIFileManager.getClanGUIConfig();

    public ClanListGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ColorUtils.translateColorCodes(guiConfig.getString("clan-list.name"));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ArrayList<Clan> clans = new ArrayList<>(ClansStorageUtil.getClanList());
        if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
            PlayerMenuUtility playerMenuUtility = Clans.getPlayerMenuUtility(player);
            playerMenuUtility.setOfflineClanOwner(Bukkit.getOfflinePlayer(UUID.fromString(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Clans.getPlugin(), "uuid"), PersistentDataType.STRING))));
            new ClanJoinRequestMenu(Clans.getPlayerMenuUtility(player)).open();
            Bukkit.getScheduler().cancelTask(taskID5);
        }else if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
            player.closeInventory();
            Bukkit.getScheduler().cancelTask(taskID5);
        }else if(event.getCurrentItem().getType().equals(Material.STONE_BUTTON)){
            if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")){
                if (page == 0){
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-list.GUI-first-page")));
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")){
                if (!((index + 1) >= clans.size())){
                    page = page + 1;
                    super.open();
                }else{
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-list.GUI-last-page")));
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuControls();
        taskID5 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Clans.getPlugin(), new Runnable() {
            @Override
            public void run() {
                //The thing you will be looping through to place items
                ArrayList<Clan> clans = new ArrayList<>(ClansStorageUtil.getClanList());

                //Pagination loop template
                if (clans != null && !clans.isEmpty()){
                    for (int i = 0; i < getMaxItemsPerPage(); i++){
                        index = getMaxItemsPerPage() * page + i;
                        if (index >= clans.size()) break;
                        if (clans.get(index) != null) {

                            //Create an item from our collection and place it into the inventory
                            String clanOwnerUUIDString = clans.get(i).getClanOwner();
                            UUID ownerUUID = UUID.fromString(clanOwnerUUIDString);
                            OfflinePlayer clanOwnerPlayer = Bukkit.getOfflinePlayer(ownerUUID);
                            Clan clan = ClansStorageUtil.findClanByOfflineOwner(clanOwnerPlayer);

                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
                            skull.setOwningPlayer(getServer().getOfflinePlayer(ownerUUID));
                            playerHead.setItemMeta(skull);

                            ItemMeta meta = playerHead.getItemMeta();
                            String displayName = ColorUtils.translateColorCodes(clan.getClanFinalName());
                            meta.setDisplayName(displayName);

                            ArrayList<String> lore = new ArrayList<>();
                            ArrayList<String> clanMembersList = clan.getClanMembers();
                            ArrayList<String> clanAlliesList = clan.getClanAllies();
                            ArrayList<String> clanEnemiesList = clan.getClanEnemies();
                            lore.add(ColorUtils.translateColorCodes("&7----------"));
                            lore.add(ColorUtils.translateColorCodes("&3Clan Prefix:" + clan.getClanPrefix()));
                            lore.add(ColorUtils.translateColorCodes("&3Clan Owner: &d" + clanOwnerPlayer.getName()));
                            lore.add(ColorUtils.translateColorCodes("&3Clan Members:"));
                            for (String string : clanMembersList){
                                UUID memberUUID = UUID.fromString(string);
                                OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
                                String offlineMemberName = member.getName();
                                lore.add(ColorUtils.translateColorCodes(" &7- &3" + offlineMemberName));
                                if (clanMembersList.size() >= 10){
                                    int membersSize = clanMembersList.size() - 10;
                                    lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l" + membersSize + "&r &3&omore!"));
                                    break;
                                }
                            }
                            lore.add(ColorUtils.translateColorCodes("&3Clan Allies:"));
                            for (String string : clanAlliesList){
                                UUID allyUUID = UUID.fromString(string);
                                OfflinePlayer ally = Bukkit.getOfflinePlayer(allyUUID);
                                String offlineAllyName = ally.getName();
                                lore.add(ColorUtils.translateColorCodes(" &7- &3" + offlineAllyName));
                                if (clanAlliesList.size() >= 10){
                                    int allySize = clanAlliesList.size() - 10;
                                    lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l" + allySize + "&r &3&omore!"));
                                    break;
                                }
                            }
                            lore.add(ColorUtils.translateColorCodes("&3Clan Enemies:"));
                            for (String string : clanEnemiesList){
                                UUID enemyUUID = UUID.fromString(string);
                                OfflinePlayer enemy = Bukkit.getOfflinePlayer(enemyUUID);
                                String offlineEnemyName = enemy.getName();
                                lore.add(ColorUtils.translateColorCodes(" &7- &3" + offlineEnemyName));
                                if (clanEnemiesList.size() >= 10){
                                    int enemySize = clanEnemiesList.size() - 10;
                                    lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l" + enemySize + "&r &3&omore!"));
                                    break;
                                }
                            }
                            lore.add(ColorUtils.translateColorCodes("&7----------"));
                            lore.add(ColorUtils.translateColorCodes("&d&oClick to send an invite request to this clan owner"));
                            lore.add(ColorUtils.translateColorCodes("&7----------"));

                            meta.setLore(lore);
                            meta.getPersistentDataContainer().set(new NamespacedKey(Clans.getPlugin(), "uuid"), PersistentDataType.STRING, clan.getClanOwner());

                            playerHead.setItemMeta(meta);

                            inventory.setItem(index, playerHead);

                        }
                    }
                }
            }
        }, 0, 40);
    }
}
