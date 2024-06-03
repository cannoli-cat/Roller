package cannolicat.roller;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Dice {
    private Sides sides;
    private Date date;
    private final ArrayList<Integer> rollArr;

    public Dice() {
        rollArr = new ArrayList<>();
        date = new Date();
    }

    public Sides getSides() { return sides; }

    public void setSides(Sides sides) { this.sides = sides; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public ArrayList<Integer> getRollArr() { return rollArr; }

    public int getRollSum() {
        int sum = 0;
        for(int i : rollArr) {
            sum += i;
        }
        return sum;
    }

    public void setRoll(int amountToRoll) {
        Random rand = new Random();
        for(int i = 0; i < amountToRoll; i++) {
            rollArr.add(rand.nextInt(sides.getValue()) + 1);
        }
    }

    public enum Sides {
        d100(100), d20(20),  d12(12), d10(10), d8(8), d6(6), d4(4), coin(2);
        private final int value;

        Sides(int value) {
            this.value = value;
        }
        public int getValue() { return value; }
    }

    public void sendMessage(Player player, Dice dice, int rollCount) {
        if(rollCount == 1) {
            String message = dice.getRollArr().get(0) + "";
            if(dice.getSides().equals(Sides.coin) && dice.getRollArr().get(0) == 1) {
                message = "Heads";
            } else if (dice.getSides().equals(Sides.coin) && dice.getRollArr().get(0) == 2) {
                message = "Tails";
            }
            String results = dice.getSides().equals(Sides.coin) ? "Coin flip results: " : "Roll results: ";
            player.sendMessage(results + message);

            if(!dice.getSides().equals(Sides.coin)) {
                if (dice.getRollArr().get(0) == 1) {
                    player.sendTitle(ChatColor.RED + "Nat 1!", "Critical Failure!", 10, 40, 10);
                } else if (dice.getRollArr().get(0) == 20) {
                    player.sendTitle(ChatColor.GREEN + "Nat 20!", "Critical Success!", 10, 40, 10);
                }
            }
        }
        else {
            StringBuilder message = new StringBuilder();
            int sum = 0;

            if (!dice.getSides().equals(Sides.coin)) {
                for (int i = 0; i < dice.getRollArr().size(); i++) {
                    message.append(dice.getRollArr().get(i)).append(", ");
                    sum += dice.getRollArr().get(i);
                }
            } else {
                for (int i = 0; i < dice.getRollArr().size(); i++) {
                    String result = dice.getRollArr().get(i) == 1 ? "Heads" : "Tails";
                    message.append(result).append(", ");
                }
            }

            message.deleteCharAt(message.length() - 1);
            message.deleteCharAt(message.length() - 1);
            String newMessage = dice.getSides().equals(Sides.coin) ? ("Coin flip results: " + message + ".") : ("Roll results: " + message + ". Sum: " + sum);
            player.sendMessage(newMessage);
        }
    }
    public void sendBroadcast(Player player, Dice dice, int rollCount) {
        if(rollCount == 1) {
            String message = dice.getRollArr().get(0) + "";
            if(dice.getSides().equals(Sides.coin) && dice.getRollArr().get(0) == 1) {
                message = "Heads";
            } else if (dice.getSides().equals(Sides.coin) && dice.getRollArr().get(0) == 2) {
                message = "Tails";
            }

            Bukkit.getServer().broadcastMessage(player.getDisplayName() + "'s 1" + dice.getSides() +  " results: " + message);

            if(!dice.getSides().equals(Sides.coin)) {
                if (dice.getRollArr().get(0) == 1) {
                    player.sendTitle(ChatColor.RED + "Nat 1!", "Critical Failure!", 10, 40, 10);
                    Location playerLocation = player.getLocation().add(0, 2, 0);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 3);

                    for (int i = 0; i < 10; i++) {
                        player.getWorld().spawnParticle(Particle.REDSTONE, playerLocation, 1, 0.5, 0.5, 0.5, 0,
                                dustOptions);
                    }
                } else if (dice.getRollArr().get(0) == 20) {
                    player.sendTitle(ChatColor.GREEN + "Nat 20!", "Critical Success!", 10, 40, 10);
                    Location playerLocation = player.getLocation().add(0, 2, 0);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GREEN, 3);

                    for (int i = 0; i < 10; i++) {
                        player.getWorld().spawnParticle(Particle.REDSTONE, playerLocation, 1, 0.5, 0.5, 0.5, 0,
                                dustOptions);
                    }
                }
            }
        }
        else {
            StringBuilder message = new StringBuilder();
            int sum = 0;

            if(!dice.getSides().equals(Sides.coin)) {
                for(int i = 0; i < dice.getRollArr().size(); i++) {
                    message.append(dice.getRollArr().get(i)).append(", ");
                    sum += dice.getRollArr().get(i);
                }
            }
            else {
                for (int i = 0; i < dice.getRollArr().size(); i++) {
                    String result = dice.getRollArr().get(i) == 1 ? "Heads" : "Tails";
                    message.append(result).append(", ");
                }
            }

            message.deleteCharAt(message.length() - 1);
            message.deleteCharAt(message.length() - 1);
            String newMessage = dice.getSides().equals(Sides.coin) ? (player.getDisplayName() + "'s " + dice.getRollArr().size() + " coin flip results: " + message + ".")
                    : (player.getDisplayName() + "'s " + dice.getRollArr().size() + dice.getSides() + " results: " + message + ". Sum: " + sum);

            Bukkit.getServer().broadcastMessage(newMessage);
        }
    }
}
