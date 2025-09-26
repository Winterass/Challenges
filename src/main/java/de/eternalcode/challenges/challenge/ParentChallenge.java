package de.eternalcode.challenges.challenge;

import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.api.Challenge;
import de.eternalcode.challenges.api.services.TimerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * An abstract class representing a abstract Parent Challenge for less Bloat code.
 */
public abstract class ParentChallenge implements Challenge {

    protected final Plugin plugin;
    private boolean running = false;

    public ParentChallenge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public TimerService getTimerService() {
        return Challenges.getSimpleTimerService();
    }

    @Override
    public void start() {
        running = true;

        getTimerService().start();

        Bukkit.broadcast(
                Component.text("§aDie Challenge §e")
                .append(getName())
                .append(Component.text(" §awurde gestartet!"))
        );
    }

    @Override
    public void stop() {
        running = false;

        // getTimerService().pause();
        getTimerService().reset();

        Bukkit.broadcast(
                Component.text("§aDie Challenge §e")
                .append(getName())
                .append(Component.text(" §cwurde gestoppt!"))
        );
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Material getIconMaterial() {
        return Material.BOOK; // default icon
    }
}
