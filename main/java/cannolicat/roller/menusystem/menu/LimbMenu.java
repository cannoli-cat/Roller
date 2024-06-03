package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.PaginatedMenu;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LimbMenu extends PaginatedMenu {
    public LimbMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getPlayer().getDisplayName() + "'s limb rolls";
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
        else if(e.getCurrentItem().getType().equals(Material.DIAMOND_SWORD) && !ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Limb Rolls")) {
            String lore = Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getLore()).get(1).split("limb")[0];
            lore = lore.replaceAll("[^0-9]", "");

            if(Integer.parseInt(lore) > 1) {
                int slotClicked = e.getSlot() - 10;
                new LimbBatchInfoMenu(playerMenuUtility, slotClicked).open();
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
            if(index >= Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).size()) break;

            int sum = Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getLimbs().size();

            ItemStack limb = new ItemStack(Material.DIAMOND_SWORD, Math.min(sum, 64));
            ItemMeta meta = limb.getItemMeta();
            assert meta != null;
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if(sum > 1) {
                meta.setDisplayName(ChatColor.GOLD + "Rolled: " + sum + (ChatColor.RED + " limbs"));
            }
            else
                meta.setDisplayName(ChatColor.GOLD + "Rolled: " + (ChatColor.RED + Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getLimbs().get(0)));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "At time: " + (ChatColor.RED + new SimpleDateFormat("HH:mm:ss").format(Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getDate())));
            lore.add(ChatColor.WHITE + "Batch Amount: " + (ChatColor.RED + ""
                    + Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getLimbs().size()));
            lore.add(ChatColor.WHITE + "For Player: " + (ChatColor.RED + Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(i).getPlayer().getName()));

            meta.setLore(lore);
            limb.setItemMeta(meta);
            inventory.addItem(limb);
        }
    }
}
