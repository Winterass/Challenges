package de.eternalcode.challenges.api.services;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Service for managing a challenge timer.
 */
public interface TimerService {

    /**
     * Startet den Timer (zählt hoch).
     */
    void start();

    /**
     * Pausiert den Timer.
     */
    void pause();

    /**
     * Setzt den Timer zurück (auf 0).
     */
    void reset();

    /**
     * @return Ob der Timer aktuell läuft.
     */
    boolean isRunning();

    /**
     * @return Vergangene Zeit in Sekunden.
     */
    int getElapsedTime();

    /**
     * Setzt die vergangene Zeit (in Sekunden).
     *
     * @param timeInSeconds Zeit in Sekunden.
     */
    void setElapsedTime(int timeInSeconds);

    /**
     * @return Formatierte Zeit (z. B. 00:12:34).
     */
    String getFormattedTime();

    /**
     * Aktualisiert das Interface für alle Spieler
     * (z. B. Scoreboard oder ActionBar).
     */
    void updateDisplay();

    /**
     * Zeigt die Zeit in der ActionBar an.
     */
    void showActionBar(@NotNull Player player);
}
