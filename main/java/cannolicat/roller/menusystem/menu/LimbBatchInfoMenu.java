package cannolicat.roller.menusystem.menu;

import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.Menu;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LimbBatchInfoMenu extends Menu {
    private final int slotClicked;

    public LimbBatchInfoMenu(PlayerMenuUtility playerMenuUtility, int slotClicked) {
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
            new LimbMenu(playerMenuUtility).open();
        }
    }

    @Override
    public void setMenuItems() {
        for(int i = 0; i < Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getLimbs().size(); i++) {
            ItemStack limb = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = limb.getItemMeta();
            NamespacedKey key = new NamespacedKey(Roller.getPlugin(), "" + new Random().nextInt(9999999));

            assert meta != null;
            meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.PI);
            meta.setDisplayName(ChatColor.GOLD + "Rolled: " + (ChatColor.RED + Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getLimbs().get(i)));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "At time: " + (ChatColor.RED + new SimpleDateFormat("HH:mm:ss").format(Roller.getPlugin().limbRolls.get(playerMenuUtility.getPlayer().getUniqueId()).get(slotClicked).getDate())));

            meta.setLore(lore);
            limb.setItemMeta(meta);
            inventory.addItem(limb);
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
