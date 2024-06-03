package cannolicat.roller;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Limb {
    private Date date;
    private final ArrayList<String> limbs;
    private Player player;

    public Limb() {
        limbs = new ArrayList<>();
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    public ArrayList<String> getLimbs() {
        return limbs;
    }

    public void rollLimb(int rollAmount, HashSet<Limbs> exclusions) {
        for (int i = 0; i < rollAmount; i++) {
            limbs.add(Limbs.randomLimb(exclusions).toString().replaceAll("_", " "));
        }
    }

    public enum Limbs {
        HEAD(0.01), TORSO(0.50),
        LEFT_ARM(0.30), LEFT_HAND(0.20),
        RIGHT_ARM(0.30), RIGHT_HAND(0.20),
        LEFT_LEG(0.30), LEFT_FOOT(0.20),
        RIGHT_LEG(0.30), RIGHT_FOOT(0.20),
        FINGER(0.15), TOE(0.15), BALLS(0.05);

        final double weight;

        Limbs(double weight) {
            this.weight = weight;
        }

        public static Limbs randomLimb(HashSet<Limbs> exclusions) {
            ArrayList<Limbs> limbsList = new ArrayList<>(EnumSet.allOf(Limbs.class));
            limbsList.removeIf(exclusions::contains);

            double totalWeight = 0.0;
            for(Limbs l : limbsList) totalWeight += l.weight;

            int i = 0;
            for (double r = Math.random() * totalWeight; i < limbsList.size() - 1; ++i) {
                r -= limbsList.get(i).weight;
                if (r <= 0.0) break;
            }
            return limbsList.get(i);
        }
    }

    public void sendMessage(Player player, Player playerToRoll, Limb limb, int rollCount) {
        StringBuilder message;

        if(player == playerToRoll)
            message = new StringBuilder(rollCount + " roll results for my own limb(s): ");
        else
            message = new StringBuilder(rollCount + " roll results for " + playerToRoll.getDisplayName() + "'s limb(s): ");

        for (int i = 0; i < limb.getLimbs().size(); i++) {
            message.append(limb.getLimbs().get(i)).append(", ");
        }

        message.deleteCharAt(message.length() - 1);
        message.deleteCharAt(message.length() - 1);

        player.sendMessage(message + ".");
    }
    public void sendBroadcast(Player player, Player playerToRoll, Limb limb, int rollCount) {
        StringBuilder message;

        if(player == playerToRoll)
            message = new StringBuilder(player.getDisplayName() + "'s " + rollCount + " roll results for their own limb(s): ");
        else
            message = new StringBuilder(player.getDisplayName() + "'s " + rollCount + " roll results for " + playerToRoll.getDisplayName() + "'s limb(s): ");

        for(int i = 0; i < limb.getLimbs().size(); i++) {
            message.append(limb.getLimbs().get(i)).append(", ");
        }

        message.deleteCharAt(message.length() - 1);
        message.deleteCharAt(message.length() - 1);

        Bukkit.getServer().broadcastMessage(message + ".");
    }
}
