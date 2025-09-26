package de.eternalcode.challenges.challenge.services;

import de.eternalcode.challenges.api.Challenge;
import de.eternalcode.challenges.api.services.ChallengeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple implementation of the ChallengeService interface.
 * This class manages the registration, unregistration, and lifecycle of challenges.
 */
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
    }

    @Override
    public void stopCurrentChallenge(Challenge challenge) {
        if (currentChallenge != null) {
            currentChallenge.stop();
            currentChallenge = null;
        }
    }

    @Override
    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }
}
