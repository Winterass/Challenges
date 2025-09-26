package de.eternalcode.challenges.challenge.hs;

import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.challenge.ParentChallenge;
import de.eternalcode.challenges.utils.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Hide and Seek Challenge
 * One Player is the seeker, all the others are Hiders!
 */
public class HideAndSeekChallenge extends ParentChallenge implements Listener {

    private HashSet<UUID> seeker = new HashSet<>();
    @Getter
    private static final HashSet<UUID> hiders = new HashSet<>();

    private BossBar bossBar;


    public HideAndSeekChallenge(Plugin plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Component getName() {
        return Component.text("Hide and Seek Challenge", NamedTextColor.GOLD);
    }

    @Override
    public Component getDescription() {
        return Component.text("One Player is the seeker, all the others are Hiders!", NamedTextColor.YELLOW);
    }

    @Override
    public Material getIconMaterial() {
        return Material.ENDER_EYE;
    }

    @Override
    public void start() {
        super.start();

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.size() < 2) {
            Bukkit.broadcast(Component.text("❌ Es müssen mindestens 2 Spieler online sein!", NamedTextColor.RED));
            stop();
            return;
        }

        // Random Seeker wählen
        Player randomSeeker = players.get(new Random().nextInt(players.size()));
        this.seeker.add(randomSeeker.getUniqueId());

        giveSeekerItems(randomSeeker);

        for (Player p : players) {
            if (seeker.stream().noneMatch(s -> s.equals(p.getUniqueId()))) {
                hiders.add(p.getUniqueId());
            }
        }

        Bukkit.broadcast(Component.text("🔍 " + randomSeeker.getName() + " ist der Sucher!", NamedTextColor.GOLD));

        bossBar = BossBar.bossBar(
                Component.text("Hider verbleibend: " + hiders.size(), NamedTextColor.RED),
                BossBar.MAX_PROGRESS,
                BossBar.Color.BLUE,
                BossBar.Overlay.PROGRESS
        );

        for (Player p : players) {
            p.showBossBar(bossBar);
        }
    }

    @Override
    public void stop() {
        super.stop();
        seeker = null;
        hiders.clear();
    }

    public boolean isSeeker(Player player) {
        return seeker != null && seeker.stream().anyMatch(p -> p.equals(player.getUniqueId()));
    }

    public boolean isHider(Player player) {
        return hiders.contains(player.getUniqueId());
    }

    private void giveSeekerItems(Player seeker) {
        seeker.getInventory().clear();

        XpUtil.applyPointsToXpBar(seeker, 5);
        Challenges.getPointsManager().setPoints(seeker.getUniqueId(), 5);

        ItemStack seekerSword = new ItemBuilder(Material.IRON_SWORD).build();
        ItemMeta meta = seekerSword.getItemMeta();
        meta.displayName(Component.text("Sucher Schwert", NamedTextColor.RED));
        seekerSword.setItemMeta(meta);

        seeker.getInventory().addItem(seekerSword);
    }

    private Component seekerText() {
        return Component.text("Dein Ziel ist es alle verbleibenden Hider zu töten!", NamedTextColor.RED);
    }

    private void updateBossBar() {
        if (bossBar != null) {
            bossBar.name(Component.text("Hider verbleibend: " + hiders.size(), NamedTextColor.RED));

            float progress = hiders.isEmpty() ? 0.0f : (float) hiders.size() / (hiders.size() + 1);
            bossBar.progress(progress);
        } else if (hiders.isEmpty()) {
            /**
             * It's not null at that Point in the Game.
             */
            bossBar = null;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Player k = event.getPlayer().getKiller();
        if (!isRunning()) return;
        event.setCancelled(true);
        if(k == null) return;

        if (isHider(p)) {
            hiders.remove(p.getUniqueId());
            Bukkit.broadcast(Component.text("❗ " + p.getName() + " wurde gefunden!", NamedTextColor.RED));

            Logger.getLogger("HideAndSeek").info("Hiders left: " + hiders.size());
            XpUtil.applyPointsToXpBar(k , Challenges.getPointsManager().getPoints(k.getUniqueId()) + 1);
            Challenges.getPointsManager().addPoints(k.getUniqueId(), 1);
            updateBossBar();
            if (hiders.isEmpty()) {
                Bukkit.broadcast(Component.text("🎉 Der Sucher hat alle Verstecker gefunden! Challenge geschafft!", NamedTextColor.GREEN));
                stop();
            } else {
                seeker.add(p.getUniqueId());
                Bukkit.broadcast(Component.text("❗ " + p.getName() + " ist nun auch ein Seeker!", NamedTextColor.YELLOW));
                giveSeekerItems(p);
                p.sendMessage(seekerText());
            }
        }
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (!isRunning()) return;

        if (isHider(p)) {
            hiders.remove(p.getUniqueId());
            Bukkit.broadcast(Component.text("❗ " + p.getName() + " hat das Spiel verlassen und wurde entfernt!", NamedTextColor.RED));

            Logger.getLogger("HideAndSeek").info("Hiders left: " + hiders.size());
            if (hiders.isEmpty()) {
                Bukkit.broadcast(Component.text("🎉 Der Sucher hat alle Verstecker gefunden! Challenge geschafft!", NamedTextColor.GREEN));
                stop();
            }
        } else if (isSeeker(p)) {
            seeker.remove(p.getUniqueId());
            Bukkit.broadcast(Component.text("❗ Der Sucher " + p.getName() + " hat das Spiel verlassen!", NamedTextColor.RED));

            if (seeker.isEmpty()) {
                if (hiders.isEmpty()) {
                    Bukkit.broadcast(Component.text("🎉 Alle Spieler haben das Spiel verlassen! Challenge gestoppt!", NamedTextColor.GREEN));
                    stop();
                } else {
                    Player newSeeker = Bukkit.getPlayer(hiders.iterator().next());
                    if (newSeeker != null) {
                        hiders.remove(newSeeker.getUniqueId());
                        seeker.add(newSeeker.getUniqueId());
                        giveSeekerItems(newSeeker);
                        newSeeker.sendMessage(seekerText());
                        Bukkit.broadcast(Component.text("🔍 " + newSeeker.getName() + " ist nun der neue Sucher!", NamedTextColor.GOLD));
                    }
                }
            }
        }
    }

}
