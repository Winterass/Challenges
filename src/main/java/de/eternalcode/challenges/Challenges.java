package de.eternalcode.challenges;

import de.eternalcode.challenges.challenge.EnderDragonChallenge;
import de.eternalcode.challenges.challenge.command.ChallengeCommand;
import de.eternalcode.challenges.challenge.services.SimpleChallengeService;
import de.eternalcode.challenges.command.CommandManager;
import de.eternalcode.challenges.command.commands.timer.TimerCommand;
import de.eternalcode.challenges.timer.SimpleTimerServiceImpl;
import de.eternalcode.challenges.utils.InventoryClickListener;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new InventoryClickListener(), this);

        commandManager = new CommandManager(this);
        simpleTimerService = new SimpleTimerServiceImpl(this);
        simpleChallengeService = new SimpleChallengeService();

        simpleChallengeService.registerChallenge(new EnderDragonChallenge(this));

        commandManager.registerCommands(TimerCommand.class);
        commandManager.registerCommands(ChallengeCommand.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
