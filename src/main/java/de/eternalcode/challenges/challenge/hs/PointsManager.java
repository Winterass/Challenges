package de.eternalcode.challenges.challenge.hs;

import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Manages points for players in a hide-and-seek challenge.
 */
public final class PointsManager {
    private final Map<UUID, Integer> points = new ConcurrentHashMap<>();

    public int getPoints(UUID id) {
        return points.getOrDefault(id, 0);
    }

    public void setPoints(UUID id, int value) {
        points.put(id, Math.max(0, value));
    }

    public void addPoints(UUID id, int delta) {
        points.merge(id, delta, Integer::sum);
    }

    public boolean trySpend(UUID id, int cost) {
        Logger.getLogger("PointsManager").info("Trying to spend " + cost + " points for player " + getPoints(id));
        int cur = getPoints(id);
        if (cur < cost) return false;
        setPoints(id, cur - cost);
        Logger.getLogger("PointsManager").info("Spent " + cost + " points for player, now has " + getPoints(id));
        XpUtil.applyPointsToXpBar(Bukkit.getPlayer(id), getPoints(id));
        return true;
    }
}