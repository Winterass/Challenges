package de.eternalcode.challenges.command;

import de.eternalcode.challenges.api.command.AbstractBaseCommand;
import de.eternalcode.challenges.api.command.BaseCommand;
import de.eternalcode.challenges.api.command.CommandContext;
import de.eternalcode.challenges.api.command.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager {
    private final Plugin plugin;
    private final Map<String, BaseCommand> commands = new HashMap<>();

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @SafeVarargs
    public final void registerCommands(Class<? extends BaseCommand>... commandClasses) {
        for (Class<? extends BaseCommand> commandClass : commandClasses) {
            if (!commandClass.isAnnotationPresent(CommandInfo.class)) {
                plugin.getLogger().warning("Command class " + commandClass.getName() + " missing @CommandInfo annotation");
                continue;
            }

            CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);
            try {
                Constructor<? extends BaseCommand> constructor = commandClass.getConstructor();
                BaseCommand command = constructor.newInstance();
                registerCommand(commandInfo, command);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to register command " + commandInfo.name(), e);
            }
        }
    }

    private void registerCommand(CommandInfo info, BaseCommand command) {
        commands.put(info.name().toLowerCase(), command);
        SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit.getServer().getCommandMap();

        PluginCommandImpl pluginCommand = new PluginCommandImpl(info.name(), info.description(), info.permission(), info, command, this);

        // Registrieren des Hauptbefehls und aller Aliase
        commandMap.register(plugin.getName(), pluginCommand);
        for (String alias : info.aliases()) {
            Logger.getLogger("Command Register").info("Registering alias: " + alias + " for command: " + info.name());
            commandMap.register(plugin.getName(), new PluginCommandImpl(alias, info.description(), info.permission(), info, command, this));
            commands.put(alias.toLowerCase(), command);
        }
    }

    public boolean executeCommand(CommandSender sender, String name, String[] args) {
        BaseCommand command = commands.get(name.toLowerCase());
        if (command == null) {
            sender.sendMessage("Unknown command: " + name);
            return true;
        }

        CommandContext context = new CommandContext(sender, args);
        BaseCommand currentCommand = command;

        // Verarbeite Subcommands rekursiv
        while (context.getArgs().length > 0) {
            String subCommandName = context.getArg(0).toLowerCase();
            BaseCommand subCommand = currentCommand.getSubCommand(subCommandName);

            if (subCommand == null) break;

            String permission = ((AbstractBaseCommand) currentCommand).getSubCommandPermissions(subCommandName);
            if (permission != null && !sender.hasPermission(permission)) {
                sender.sendMessage("You don't have permission to execute this subcommand.");
                return true;
            }

            currentCommand = subCommand;
            context = context.subContext();
        }

        try {
            currentCommand.execute(context); // Letztes gültiges Subcommand ausführen
        } catch (CommandException e) {
            sender.sendMessage("Error executing command: " + e.getMessage());
        }
        return true;
    }

    public List<String> tabCompleteCommand(CommandSender sender, String name, String[] args) {
        BaseCommand command = commands.get(name.toLowerCase());
        if (command == null) {
            return Collections.emptyList();
        }

        CommandContext context = new CommandContext(sender, args);
        BaseCommand subCommand = command;

        // Rekursives Navigieren durch Subcommands für Tab-Completion
        for (int i = 0; i < args.length - 1; i++) {
            BaseCommand nextSubCommand = subCommand.getSubCommand(args[i]);
            if (nextSubCommand == null) break;
            subCommand = nextSubCommand;
            context = context.subContext();
        }

        // Rückgabe der Tab-Vervollständigung des gefundenen Subcommands
        return subCommand.tabComplete(context);
    }
}

