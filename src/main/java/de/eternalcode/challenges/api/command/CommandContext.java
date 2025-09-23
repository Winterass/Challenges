package de.eternalcode.challenges.api.command;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandContext {
    private final CommandSender sender;
    private final String[] args;
    private final Map<String, String> parsedArgs = new HashMap<>();

    public CommandContext(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return (index < args.length) ? args[index] : null;
    }

    public String getArg(String key) {
        return parsedArgs.get(key);
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() throws CommandException {
        if (sender instanceof Player) {
            return (Player) sender;
        }
        throw new CommandException("This command can only be executed by a player.");
    }

    // Neue Methode: Erstellt einen Sub-Context mit den verbleibenden Argumenten
    public CommandContext subContext() {
        if (args.length <= 1) {
            return new CommandContext(sender, new String[0]); // Keine weiteren Argumente
        }

        // Erstellt ein neues Array ohne das erste Argument
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        return new CommandContext(sender, subArgs);
    }

    /**
     * Parst die registrierten Argumente und speichert sie mit ihren Schlüsseln.
     */
    public void parseArguments(List<CommandArgument<?>> commandArguments) {
        for (int i = 0; i < commandArguments.size() && i < args.length; i++) {
            CommandArgument<?> arg = commandArguments.get(i);
            parsedArgs.put(arg.getKey(), args[i]);
        }
    }
}

