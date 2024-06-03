package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Dice;
import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import cannolicat.roller.menusystem.PlayerMenuUtility;

import java.text.SimpleDateFormat;
import java.util.*;

public class RollMenu extends PaginatedMenu {
    public RollMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getPlayer().getDisplayName() + "'s Rolls";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {
            new ChoiceMenu(playerMenuUtility).open();
        }
        else if(e.getCurrentItem().getType().equals(Material.GLOWSTONE_DUST) && !ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Die Rolls")) {
            String lore = Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getLore()).get(1).split("d")[0];
            lore = lore.replaceAll("[^0-9]", "");

            if(Integer.parseInt(lore) > 1) {
                int slotClicked = e.getSlot() - 10;
                new DieBatchInfoMenu(playerMenuUtility, slotClicked).open();
            }
        }
        else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
            if (ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) >= Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).size())){
                    page = page + 1;
                    super.open();
                }else{
                    p.sendMessage(ChatColor.GRAY + "You are on the last page.");
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        for(int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if(index >= Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).size()) break;

            int sum = Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getRollSum();

            ItemStack roll = new ItemStack(Material.GLOWSTONE_DUST, Math.min(sum, 64));
            ItemMeta meta = roll.getItemMeta();
            assert meta != null;
            if(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getSides().equals(Dice.Sides.coin)) {
                String result = "Coins";
                if(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getRollArr().size() == 1)
                    result = Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getRollArr().get(0) == 1 ? "Heads" : "Tails";

                meta.setDisplayName(ChatColor.GOLD + result);
            }
            else
                meta.setDisplayName(ChatColor.GOLD + "Rolled: " + sum);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "At time: " + (ChatColor.RED + new SimpleDateFormat("HH:mm:ss").format(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getDate())));
            lore.add(ChatColor.WHITE + "Batch Amount: " + (ChatColor.RED + ""
                    + Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getRollArr().size()
                    + Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getSides()));

            meta.setLore(lore);
            roll.setItemMeta(meta);
            inventory.addItem(roll);
        }
    }
}
