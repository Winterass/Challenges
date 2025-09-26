package de.eternalcode.challenges.command.commands.timer;

import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.api.command.AbstractBaseCommand;
import de.eternalcode.challenges.api.command.CommandContext;
import de.eternalcode.challenges.api.command.CommandInfo;
import de.eternalcode.challenges.api.services.TimerService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandException;

/**
 * Command to manage the challenge timer.
 */
@CommandInfo(
        name = "timer",
        description = "Manage the challenge timer",
        minArgs = 0,
        maxArgs = 1,
        usage = "<resume | pause | reset>",
        aliases = {"challenge-timer", "ctimer"},
        permission = "eternal.timer"
)
public class TimerCommand extends AbstractBaseCommand {

    private final TimerService timerService;

    public TimerCommand() {
        this.timerService = Challenges.getSimpleTimerService();

        this.child("resume", TimerResume(), "eternal.timer.resume");
        this.child("pause", TimerPause(), "eternal.timer.pause");
        this.child("reset", TimerReset(), "eternal.timer.reset");
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        context.getPlayer().sendMessage(Component.text("Please specify a subcommand: resume, pause, or reset."));
    }

    private AbstractBaseCommand TimerResume() {
        return new AbstractBaseCommand() {
            @Override
            public void execute(CommandContext context) throws CommandException {
                if (timerService.isRunning()) {
                    context.getPlayer().sendMessage(Component.text("The timer is already running."));
                } else {
                    timerService.start();
                    context.getPlayer().sendMessage(Component.text("The timer has been resumed."));
                }
            }
        };
    }

    private AbstractBaseCommand TimerPause() {
        return new AbstractBaseCommand() {
            @Override
            public void execute(CommandContext context) throws CommandException {
                if (!timerService.isRunning()) {
                    context.getPlayer().sendMessage(Component.text("The timer is already paused."));
                } else {
                    timerService.pause();
                    context.getPlayer().sendMessage(Component.text("The timer has been paused."));
                }
            }
        };
    }

    private AbstractBaseCommand TimerReset() {
        return new AbstractBaseCommand() {
            @Override
            public void execute(CommandContext context) throws CommandException {
                timerService.reset();
                context.getPlayer().sendMessage(Component.text("The timer has been reset."));
            }
        };
    }

    /*
    private AbstractBaseCommand child(String name, CommandExecutor executor, String description) {
        return new AbstractBaseCommand() {
            {
                this.setName(name);
                this.setDescription(description);
                this.setMinArgs(0);
                this.setMaxArgs(0);
                this.setUsage("");
                this.setPermission(description);
            }

            @Override
            public void execute(CommandContext context) throws CommandException {
                executor.onCommand(context.getSender(), null, name, context.getArgs());
            }
        };
    }

     */
}
