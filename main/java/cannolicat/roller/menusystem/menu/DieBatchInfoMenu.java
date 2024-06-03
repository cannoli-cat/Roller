package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Dice;
import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.Menu;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DieBatchInfoMenu extends Menu {
    private final int slotClicked;

    public DieBatchInfoMenu(PlayerMenuUtility playerMenuUtility, int slotClicked) {
        super(playerMenuUtility);
        this.slotClicked = slotClicked;
    }

    @Override
    public String getMenuName() {
        return "Batch Info";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {
            new RollMenu(playerMenuUtility).open();
        }
    }

    @Override
    public void setMenuItems() {
        for(int i = 0; i < Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getRollArr().size(); i++) {
            ItemStack roll = new ItemStack(Material.GLOWSTONE_DUST, Math.min(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getRollArr().get(i), 64));
            ItemMeta meta = roll.getItemMeta();
            NamespacedKey key = new NamespacedKey(Roller.getPlugin(), "" + new Random().nextInt(9999999));
            assert meta != null;
            meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.PI);
            if(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getSides().equals(Dice.Sides.coin)) {
                String results = Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getRollArr().get(i) == 1 ? "Heads" : "Tails";
                meta.setDisplayName(ChatColor.GOLD + results);
            }
            else
                meta.setDisplayName(ChatColor.GOLD + "Rolled: " + Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getRollArr().get(i));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "At time: " + (ChatColor.RED + new SimpleDateFormat("HH:mm:ss").format(Roller.getPlugin().dieRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getDate())));

            meta.setLore(lore);
            roll.setItemMeta(meta);
            inventory.addItem(roll);
        }

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta meta = back.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Back");
        back.setItemMeta(meta);
        inventory.setItem(17, back);

        setFillerGlass();
    }
}
