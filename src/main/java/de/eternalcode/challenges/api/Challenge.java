package de.eternalcode.challenges.api;

import de.eternalcode.challenges.api.services.TimerService;
import de.eternalcode.challenges.timer.SimpleTimerServiceImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Basic-Interface for a Challenge.
 */
public interface Challenge {

    /**
     * Returns the TimerService used by this Challenge.
     *
     * @return the TimerService
     */
    TimerService getTimerService();

    /**
     * Returns the name of the Challenge as a Component.
     *
     * @return the name of the Challenge
     */
    Component getName();

    /**
     * Starts the Challenge.
     */
    void start();

    /**
     * Stops the Challenge.
     */
    void stop();

    /**
     * Returns whether the Challenge is currently running.
     *
     * @return true if the Challenge is running, false otherwise
     */
    boolean isRunning();

    /**
     * Returns a brief description of the Challenge as a Component.
     *
     * @return the description of the Challenge
     */
    Component getDescription();

    /**
     * Returns the Material used as an icon for the Challenge.
     *
     * @return the icon Material
     */
    Material getIconMaterial();

    /**
     * Called when a player joins the server.
     *
     * @param player the player who joined
     */
    default void onJoin(Player player) {}

    /**
     * Called when a player quits the server.
     *
     * @param player the player who quit
     */
    default void onQuit(Player player) {}
}
