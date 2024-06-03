package cannolicat.roller;

import cannolicat.roller.Commands.BroadcastRoll;
import cannolicat.roller.Commands.ClearRolls;
import cannolicat.roller.Commands.Roll;
import cannolicat.roller.Commands.ShowRoll;
import cannolicat.roller.listeners.MenuListener;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Roller extends JavaPlugin {
    public HashMap<UUID, ArrayList<Dice>> dieRolls = new HashMap<>();
    public HashMap<UUID, ArrayList<Limb>> limbRolls = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static Roller plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getCommand("roll").setExecutor(new Roll());
        getCommand("showrolls").setExecutor(new ShowRoll());
        getCommand("clearrolls").setExecutor(new ClearRolls());
        getCommand("broll").setExecutor(new BroadcastRoll());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().warning("Shutting down... Clearing roll maps...");
        dieRolls.clear();
        limbRolls.clear();
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }

    public static Roller getPlugin() {
        return plugin;
    }
}
