package de.eternalcode.challenges.challenge.hs.listener;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.challenge.hs.HideAndSeekChallenge;
import de.eternalcode.challenges.challenge.hs.XpUtil;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener to manage experience points (XP) and points system for players in the Hide and Seek challenge.
 * This listener ensures that players' XP bars are reset upon joining and prevents XP pickup during the challenge.
 */
public final class PointsXpListener implements Listener {

    public PointsXpListener() {}

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!Challenges.getSimpleChallengeService().isChallengeRunning() &&
                Challenges.getSimpleChallengeService().getCurrentChallenge() != null &&
                Challenges.getSimpleChallengeService().getCurrentChallenge() instanceof HideAndSeekChallenge) return;

        XpUtil.resetXpBar(e.getPlayer(), 0);
        Challenges.getPointsManager().setPoints(e.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onPickupXp(PlayerPickupExperienceEvent event) {
        if (!Challenges.getSimpleChallengeService().isChallengeRunning() &&
                Challenges.getSimpleChallengeService().getCurrentChallenge() != null &&
        Challenges.getSimpleChallengeService().getCurrentChallenge() instanceof HideAndSeekChallenge) return;

        event.setCancelled(true);

        ExperienceOrb orb = event.getExperienceOrb();
        orb.setExperience(0);
    }
}
