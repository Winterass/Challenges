package de.eternalcode.challenges.challenge.hs;

import de.eternalcode.challenges.Challenges;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages player abilities for the Hide and Seek challenge.
 */
public class AbilityExecutor {

    private final Plugin plugin;

    public AbilityExecutor(Plugin plugin) {
        this.plugin = plugin;
    }

    public void teleportNearRandomPlayer(Player seeker) {
        int radius = 30;

        Player target = pickTargetPlayer(seeker);
        if (target == null) {
            seeker.sendMessage(Component.text("Kein Zielspieler gefunden.", NamedTextColor.RED));
            return;
        }

        Location dest = findRandomSafeAround(target.getLocation(), radius, 24);
        if (dest == null) {
            seeker.sendMessage(Component.text("Kein sicherer Teleportpunkt gefunden.", NamedTextColor.RED));
            return;
        }

        if(Challenges.getPointsManager().trySpend(seeker.getUniqueId(), 3)) {
            seeker.sendMessage(Component.text("3 Punkte für Teleport ausgegeben.", NamedTextColor.YELLOW));
        } else {
            seeker.sendMessage(Component.text("Du hast nicht genug Punkte für einen Teleport (3 benötigt).", NamedTextColor.RED));
            return;
        }

        seeker.teleport(dest);
        World w = dest.getWorld();
        if (w != null) {
            w.playSound(dest, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            w.spawnParticle(Particle.PORTAL, dest, 40, 0.3, 0.8, 0.3, 0.02);
        }
        seeker.sendMessage(Component.text("Teleport nahe " + target.getName() + " (±" + radius + " Blöcke).", NamedTextColor.GREEN));
    }

    public void applySpeed(Player p) {

        if(Challenges.getPointsManager().trySpend(p.getUniqueId(), 2)) {
            p.sendMessage(Component.text("2 Punkte für Speed II ausgegeben.", NamedTextColor.YELLOW));
        } else {
            p.sendMessage(Component.text("Du hast nicht genug Punkte für einen Speed-Boost (2 benötigt).", NamedTextColor.RED));
            return;
        }

        int secs = 30;
        int amp = 2;
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, secs * 20, amp, false, true, true));
        p.sendMessage(Component.text("Speed II aktiv für " + secs + "s.", NamedTextColor.GREEN));
    }

    public void applyFly(Player p) {

        if(Challenges.getPointsManager().trySpend(p.getUniqueId(), 5)) {
            p.sendMessage(Component.text("5 Punkte für Fliegen ausgegeben.", NamedTextColor.YELLOW));
        } else {
            p.sendMessage(Component.text("Du hast nicht genug Punkte für's Fliegen (5 benötigt).", NamedTextColor.RED));
            return;
        }

        int secs = 10;
        if (!p.getAllowFlight()) p.setAllowFlight(true);
        p.setFlying(true);
        p.sendMessage(Component.text("Flug aktiv für " + secs + "s.", NamedTextColor.GREEN));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (p.isOnline() && p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
                p.setFlying(false);
                p.setAllowFlight(false);
                p.sendMessage(Component.text("Flug beendet.", NamedTextColor.YELLOW));
            }
        }, secs * 20L);
    }

    /* ===== Hilfsmethoden ===== */

    private static Player pickTargetPlayer(Player seeker) {
        // Bevorzugt: lebende Hider
        List<Player> hidersAlive = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(HideAndSeekChallenge.getHiders())) continue;
            hidersAlive.add(p);
        }
        List<? extends Player> candidates = !hidersAlive.isEmpty() ? hidersAlive : Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(seeker))
                .toList();

        if (candidates.isEmpty()) return null;
        return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
    }

    private static Location findRandomSafeAround(Location center, int radius, int attempts) {
        World world = center.getWorld();
        if (world == null) return null;
        WorldBorder border = world.getWorldBorder();

        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < attempts; i++) {
            double angle = rnd.nextDouble(0, Math.PI * 2);
            double dist = rnd.nextDouble(3, radius + 0.0001);
            double dx = Math.cos(angle) * dist;
            double dz = Math.sin(angle) * dist;

            int x = center.getBlockX() + (int) Math.round(dx);
            int z = center.getBlockZ() + (int) Math.round(dz);

            int yStart = center.getBlockY();

            Location feet = null;
            for (int dy = 8; dy >= -8; dy--) {
                Location test = new Location(world, x + 0.5, yStart + dy, z + 0.5);
                if (border != null && !border.isInside(test)) continue;
                if (isSafe(test)) {
                    feet = test;
                    break;
                }
            }

            if (feet == null) {
                int highestY = world.getHighestBlockYAt(x, z);
                Location test = new Location(world, x + 0.5, highestY, z + 0.5);
                if (border == null || border.isInside(test)) {
                    if (isSafe(test)) feet = test;
                }
            }

            if (feet != null) {
                return feet;
            }
        }
        return null;
    }

    private static boolean isSafe(Location feet) {
        if (feet.getWorld() == null) return false;
        Location head = feet.clone().add(0, 1, 0);
        Location below = feet.clone().add(0, -1, 0);
        Material belowMat = below.getBlock().getType();

        return belowMat.isSolid()
                && feet.getBlock().isPassable()
                && head.getBlock().isPassable();
    }
}