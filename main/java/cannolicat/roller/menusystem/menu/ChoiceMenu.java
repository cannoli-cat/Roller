package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.Menu;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ChoiceMenu extends Menu {
    public ChoiceMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Options";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {
            new PlayerMenu(playerMenuUtility).open();
        }
        else if(Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.GLOWSTONE_DUST) && ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Die Rolls")) {
            if(!Roller.getPlugin().dieRolls.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "This player has no current die rolls!");
                return;
            }
            new RollMenu(playerMenuUtility).open();
        }
        else if(Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.DIAMOND_SWORD) && ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName()).equalsIgnoreCase("Limb Rolls")) {
            if(!Roller.getPlugin().limbRolls.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "This player has no current limb rolls!");
                return;
            }
            new LimbMenu(playerMenuUtility).open();
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack roll = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta rollMeta = roll.getItemMeta();
        assert rollMeta != null;
        rollMeta.setDisplayName(ChatColor.GOLD + "Die Rolls");
        roll.setItemMeta(rollMeta);

        ItemStack limbs = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta limbsMeta = limbs.getItemMeta();
        assert limbsMeta != null;
        limbsMeta.setDisplayName(ChatColor.GOLD + "Limb Rolls");
        limbsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        limbs.setItemMeta(limbsMeta);

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        assert backMeta != null;
        backMeta.setDisplayName(ChatColor.RED + "Back");
        back.setItemMeta(backMeta);

        inventory.setItem(3, roll);
        inventory.setItem(5, limbs);
        inventory.setItem(8, back);

        setFillerGlass();
    }
}
