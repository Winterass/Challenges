package de.eternalcode.challenges.command;

import de.eternalcode.challenges.api.command.BaseCommand;
import de.eternalcode.challenges.api.command.CommandInfo;
import de.eternalcode.challenges.api.command.SenderTypes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PluginCommandImpl extends Command {
    private final BaseCommand baseCommand;
    private final CommandInfo commandInfo;
    private final CommandManager commandManager;

    public PluginCommandImpl(String name, String description, String permission, CommandInfo commandInfo, BaseCommand baseCommand, CommandManager commandManager) {
        super(name);
        this.baseCommand = baseCommand;
        this.commandInfo = commandInfo;
        this.setDescription(description);
        this.setPermission(permission);
        this.commandManager = commandManager;

        // Aliase aus CommandInfo laden und hinzufügen
        if (commandInfo.aliases().length > 0) {
            this.setAliases(Arrays.stream(commandInfo.aliases()).toList());
        }
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (commandInfo.permission() != null && !sender.hasPermission(commandInfo.permission())) {
            sender.sendMessage("Du hast keine Berechtigung, diesen Befehl auszuführen.");
            return true;
        }

        if (commandInfo.senderTypes() != null && !isValidSender(sender, commandInfo.senderTypes())) {
            sender.sendMessage("Du kannst diesen Befehl nicht ausführen.");
            return true;
        }

        if (args.length < commandInfo.minArgs()) {
            sender.sendMessage("Usage: " + (commandInfo.usage().isEmpty() ? "/" + commandInfo.name() : commandInfo.usage()));
            return true;
        }

        try {
            commandManager.executeCommand(sender, label, args);  // Verwendet den CommandManager
        } catch (CommandException e) {
            sender.sendMessage("Command execution error: " + e.getMessage());
        }
        return true;
    }

    // Tab-Completion wird an den CommandManager delegiert
    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return commandManager.tabCompleteCommand(sender, alias, args);
    }

    public boolean isValidSender(CommandSender sender, SenderTypes... senderTypes) {
        if (sender instanceof Player && containsType(senderTypes, SenderTypes.PLAYER)) {
            return true;
        } else if (sender instanceof org.bukkit.command.ConsoleCommandSender && containsType(senderTypes, SenderTypes.CONSOLE)) {
            return true;
        } else return containsType(senderTypes, SenderTypes.ANY);
    }

    private boolean containsType(SenderTypes[] types, SenderTypes senderTypes) {
        return Arrays.stream(types).anyMatch(type -> type.equals(senderTypes));
    }
}
