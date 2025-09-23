package de.eternalcode.challenges.api.command;

import org.bukkit.command.CommandException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface BaseCommand {
    void execute(CommandContext context) throws CommandException;

    default List<String> tabComplete(CommandContext context) {
        return Collections.emptyList();
    }

    Map<String, BaseCommand> getSubCommands();

    default BaseCommand child(String name, BaseCommand subCommand) {
        getSubCommands().put(name.toLowerCase(), subCommand);
        return this;  // Ermöglicht verkettete Aufrufe
    }

    default BaseCommand getSubCommand(String name) {
        return getSubCommands().get(name.toLowerCase());
    }
}
