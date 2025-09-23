package de.eternalcode.challenges.challenge.services;

import de.eternalcode.challenges.api.Challenge;
import de.eternalcode.challenges.api.services.ChallengeService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleChallengeService implements ChallengeService {

    private final List<Challenge> challenges = new ArrayList<>();
    private Challenge currentChallenge;

    @Override
    public void registerChallenge(Challenge challenge) {
        challenges.add(challenge);
    }

    @Override
    public void unregisterChallenge(Challenge challenge) {
        challenges.remove(challenge);
    }

    @Override
    public Collection<Challenge> getChallenges() {
        return challenges;
    }

    @Override
    public void startChallenge(Challenge challenge) {
        if (currentChallenge != null) {
            currentChallenge.stop();
        }
        currentChallenge = challenge;
        currentChallenge.start();
        // Bukkit.broadcastMessage("§aDie Challenge §e" + challenge.getName() + " §awurde gestartet!");
    }

    @Override
    public void stopCurrentChallenge(Challenge challenge) {
        if (currentChallenge != null) {
            currentChallenge.stop();
            // Bukkit.broadcast(Component.text("§cDie Challenge §e" + currentChallenge.getName() + " §cwurde gestoppt!"));
            currentChallenge = null;
        }
    }

    @Override
    public Challenge getCurrentChallenge() {
        return null;
    }
}
