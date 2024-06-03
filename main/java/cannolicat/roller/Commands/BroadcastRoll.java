package cannolicat.roller.Commands;

import cannolicat.roller.Dice;
import cannolicat.roller.Limb;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class BroadcastRoll implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("broll") && sender instanceof Player) {
            Player player = (Player) sender;
            Dice dice = new Dice();
            if (args.length >= 2) {
                int rollCount;
                try {
                    rollCount = Integer.parseInt(args[0]) <= 0 ? 1 : Integer.parseInt(args[0]);
                }catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Please enter a valid number.");
                    return true;
                }

                if(rollCount > 10) {
                    player.sendMessage(ChatColor.RED + "You cannot roll more than 10 at a time!");
                    return true;
                }

                if(!args[1].equalsIgnoreCase("limb")) {
                    Dice.Sides sideCount = Dice.Sides.valueOf(args[1]);

                    String message = sideCount.equals(Dice.Sides.coin) ? ("Flipping " + rollCount + " coin(s)...") : ("Rolling " + rollCount + sideCount + "...");
                    player.sendMessage(message);
                    dice.setSides(sideCount);
                    dice.setRoll(rollCount);

                    dice.sendBroadcast(player, dice, rollCount);
                }
                else {
                    Player playerToRoll;

                    if(args.length == 3)
                        playerToRoll = Bukkit.getPlayer(args[2]);
                    else
                        playerToRoll = (Player) sender;

                    Limb limb = new Limb();

                    HashSet<Limb.Limbs> exclusions  = new HashSet<>();
                    if(args.length >= 4) {
                        for(int i = 3; i < args.length; i++) {
                            exclusions.add(Limb.Limbs.valueOf(args[i]));
                        }
                    }

                    assert playerToRoll != null;
                    String message = playerToRoll.getUniqueId() == ((Player) sender).getUniqueId() ? "Rolling " + rollCount + " of my own limb(s)..." : "Rolling " + rollCount + " of " + playerToRoll.getDisplayName() +"'s limb(s)...";

                    player.sendMessage(message);
                    limb.rollLimb(rollCount, exclusions);
                    limb.setPlayer(playerToRoll);
                    limb.sendBroadcast(player, playerToRoll, limb, rollCount);
                }
            }
            else if (args.length == 1) {
                int rollCount;
                try {
                    rollCount = Integer.parseInt(args[0]) <= 0 ? 1 : Integer.parseInt(args[0]);
                }catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Please enter a valid number.");
                    return true;
                }

                if (rollCount > 10) {
                    player.sendMessage(ChatColor.RED + "You cannot roll more than 10 die at a time!");
                    return true;
                }

                player.sendMessage("Rolling " + rollCount + Dice.Sides.d20 + "...");
                dice.setSides(Dice.Sides.d20);
                dice.setRoll(rollCount);

                dice.sendBroadcast(player, dice, rollCount);
            } else {
                player.sendMessage("Rolling 1d20...");
                dice.setSides(Dice.Sides.d20);
                dice.setRoll(1);

                dice.sendBroadcast(player, dice, 1);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1) {
            List<String> list = new ArrayList<>();
            for(int i = 1; i < 11; i++)
                list.add(i + "");
            return list;
        }
        else if(strings.length == 2) {
            List<String> list = new ArrayList<>();
            for(Dice.Sides side : EnumSet.allOf(Dice.Sides.class)) {
                list.add(side.toString());
            }
            list.add("limb");
            return list;
        }
        else if (strings.length >= 4) {
            List<String> limbs = new ArrayList<>();
            for(Limb.Limbs limb : EnumSet.allOf(Limb.Limbs.class)) {
                limbs.add(limb.toString());
            }
            return limbs;
        }
        return null;
    }
}
