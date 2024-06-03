package cannolicat.roller.Commands;

import cannolicat.roller.Roller;
import cannolicat.roller.menusystem.PlayerMenuUtility;
import cannolicat.roller.menusystem.menu.ChoiceMenu;
import cannolicat.roller.menusystem.menu.PlayerMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowRoll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("showrolls") && sender instanceof Player && args.length == 0) {
            Player player = (Player) sender;
            if (player.hasPermission("roll.show")) {
                new PlayerMenu(Roller.getPlayerMenuUtility(player)).open();
            }
        }
        else if(cmd.getName().equalsIgnoreCase("showrolls") && sender instanceof Player && args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            assert player != null;

            if (player.hasPermission("roll.show")) {
                if(Roller.getPlugin().dieRolls.containsKey(player.getUniqueId()) || Roller.getPlugin().limbRolls.containsKey(player.getUniqueId())) {
                    PlayerMenuUtility playerMenuUtility = Roller.getPlayerMenuUtility(player);
                    playerMenuUtility.setPlayer(player);
                    new ChoiceMenu(playerMenuUtility).open();
                }
                else {
                    Player p = (Player) sender;
                    p.sendMessage(ChatColor.RED+ "This player has no current rolls!");
                }
            }
        } else if(cmd.getName().equalsIgnoreCase("showrolls") && sender instanceof Server) Bukkit.getLogger().warning("Only players can execute this command.");
        return true;
    }
}
