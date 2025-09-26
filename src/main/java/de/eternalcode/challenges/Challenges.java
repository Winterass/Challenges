package de.eternalcode.challenges;

import de.eternalcode.challenges.challenge.EnderDragonChallenge;
import de.eternalcode.challenges.challenge.hs.AbilityExecutor;
import de.eternalcode.challenges.challenge.hs.HideAndSeekAbillityCommand;
import de.eternalcode.challenges.challenge.hs.HideAndSeekChallenge;
import de.eternalcode.challenges.challenge.ChallengeCommand;
import de.eternalcode.challenges.challenge.hs.listener.PointsXpListener;
import de.eternalcode.challenges.challenge.services.SimpleChallengeService;
import de.eternalcode.challenges.command.CommandManager;
import de.eternalcode.challenges.command.commands.timer.TimerCommand;
import de.eternalcode.challenges.timer.SimpleTimerServiceImpl;
import de.eternalcode.challenges.utils.InventoryClickListener;
import de.eternalcode.challenges.challenge.hs.PointsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Challenges extends JavaPlugin {

    @Getter
    private static CommandManager commandManager;

    @Getter
    private static SimpleTimerServiceImpl simpleTimerService;

    @Getter
    private static SimpleChallengeService simpleChallengeService;

    @Getter
    private static AbilityExecutor abilityExecutor;

    @Getter
    private static PointsManager pointsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new PointsXpListener(), this);

        commandManager = new CommandManager(this);
        simpleTimerService = new SimpleTimerServiceImpl(this);
        simpleChallengeService = new SimpleChallengeService();
        abilityExecutor = new AbilityExecutor(this);
        pointsManager = new PointsManager();

        simpleChallengeService.registerChallenge(new EnderDragonChallenge(this));
        simpleChallengeService.registerChallenge(new HideAndSeekChallenge(this));

        commandManager.registerCommands(TimerCommand.class);
        commandManager.registerCommands(ChallengeCommand.class);
        commandManager.registerCommands(HideAndSeekAbillityCommand.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
