package cannolicat.roller.Commands;

import cannolicat.roller.Roller;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearRolls implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("clearrolls") && sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("roll.clear")) {
                Roller.getPlugin().dieRolls.clear();
                Roller.getPlugin().limbRolls.clear();
                player.sendMessage(ChatColor.GREEN + "Successfully cleared the list of rolls!");
            }
        }
        else if(sender instanceof Server) {
            Roller.getPlugin().dieRolls.clear();
            Roller.getPlugin().limbRolls.clear();
            Bukkit.getLogger().warning("Successfully cleared the list of rolls!");
        }
        return true;
    }
}
