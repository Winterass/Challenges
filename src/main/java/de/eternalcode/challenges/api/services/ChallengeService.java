package de.eternalcode.challenges.api.services;

import de.eternalcode.challenges.api.Challenge;

import java.util.Collection;

/**
 * Service for managing challenge-related functionalities.
 */
public interface ChallengeService {

    void registerChallenge(Challenge challenge);

    void unregisterChallenge(Challenge challenge);

    Collection<Challenge> getChallenges();

    void startChallenge(Challenge challenge);

    void stopCurrentChallenge(Challenge challenge);

    Challenge getCurrentChallenge();

    default boolean isChallengeRunning() {
        return getCurrentChallenge() != null && getCurrentChallenge().isRunning();
    }

}
