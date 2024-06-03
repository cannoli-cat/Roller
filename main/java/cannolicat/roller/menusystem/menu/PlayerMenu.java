package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Roller;
import org.bukkit.Bukkit;
import cannolicat.roller.menusystem.PaginatedMenu;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class PlayerMenu extends PaginatedMenu {

    public PlayerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Player Rolls";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        HashSet<Player> players = new HashSet<>();
        for(UUID key : Roller.getPlugin().dieRolls.keySet()) {
            players.add(Bukkit.getPlayer(key));
        }

        for(UUID key : Roller.getPlugin().limbRolls.keySet()) {
            players.add(Bukkit.getPlayer(key));
        }

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.PLAYER_HEAD)) {
            PlayerMenuUtility playerMenuUtility = Roller.getPlayerMenuUtility(p);
            playerMenuUtility.setPlayer(Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Roller.getPlugin(), "uuid"), PersistentDataType.STRING)))));

            new ChoiceMenu(playerMenuUtility).open();
        }
        else if (e.getCurrentItem().getType().equals(Material.BARRIER)) p.closeInventory();
        else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)){
            if (ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) >= players.size())){
                    page = page + 1;
                    super.open();
                }
                else{
                    p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        HashSet<Player> playerSet = new HashSet<>();
        for(UUID key : Roller.getPlugin().dieRolls.keySet()) {
            playerSet.add(Bukkit.getPlayer(key));
        }

        for(UUID key : Roller.getPlugin().limbRolls.keySet()) {
            playerSet.add(Bukkit.getPlayer(key));
        }

        ArrayList<Player> players = new ArrayList<>(playerSet);

        if(!players.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){

                    ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta playerMeta = (SkullMeta) playerItem.getItemMeta();
                    assert playerMeta != null;
                    playerMeta.setDisplayName(ChatColor.RED + players.get(index).getDisplayName() + " ");
                    playerMeta.setOwningPlayer(players.get(index));

                    playerMeta.getPersistentDataContainer().set(new NamespacedKey(Roller.getPlugin(), "uuid"), PersistentDataType.STRING, players.get(index).getUniqueId().toString());
                    playerItem.setItemMeta(playerMeta);

                    inventory.addItem(playerItem);
                }
            }
        }
    }
}
