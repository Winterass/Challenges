package de.eternalcode.challenges.challenge.hs;

import org.bukkit.entity.Player;

/**
 * Utility class for managing player XP bars based on points.
 */
public final class XpUtil {
    private XpUtil() {}

    public static void applyPointsToXpBar(Player p, int points) {
        p.setExp(0.0f);
        p.setLevel(points);
    }


    public static void resetXpBar(Player p, int points) {
        p.setExp(0.0f);
        p.setTotalExperience(0);
        p.setLevel(Math.max(0, points));
    }
}
