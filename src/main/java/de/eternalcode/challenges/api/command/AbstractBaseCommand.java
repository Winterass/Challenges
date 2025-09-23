package de.eternalcode.challenges.api.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractBaseCommand implements BaseCommand {
    private final Map<String, BaseCommand> subCommands = new HashMap<>();
    private final Map<String, String> subCommandPermissions = new HashMap<>();
    private final List<CommandArgument<?>> arguments = new ArrayList<>();

    @Override
    public Map<String, BaseCommand> getSubCommands() {
        return subCommands;
    }

    public String getSubCommandPermissions(String name) {
        return subCommandPermissions.get(name.toLowerCase());
    }

    @Override
    public BaseCommand child(String name, BaseCommand subCommand) {
        return child(name, subCommand, null); // Keine Permission, immer sichtbar
    }

    public BaseCommand child(String name, BaseCommand subCommand, String permission) {
        subCommands.put(name.toLowerCase(), subCommand);
        if (permission != null) {
            subCommandPermissions.put(name.toLowerCase(), permission);
        }
        return this; // Ermöglicht verkettete Aufrufe
    }

    // Registriere Argumente für diesen Command
    public AbstractBaseCommand arguments(CommandArgument<?>... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    // Konvertiert den usage-String in ein Component
    public Component getUsageComponent() {
        CommandInfo info = this.getClass().getAnnotation(CommandInfo.class);
        if (info == null || info.usage().isEmpty()) {
            return Component.text("Usage: /" + info.name());
        }

        // Konvertiert den usage-String zu einem Component mithilfe von MiniMessage
        return MiniMessage.miniMessage().deserialize(info.usage());
    }

    /*
    @Override
    public List<String> tabComplete(CommandContext context) {
        BaseCommand currentCommand = this;

        // Navigiere durch die Subcommands basierend auf den bisherigen Argumenten
        for (int i = 0; i < context.getArgs().length - 1; i++) {
            String arg = context.getArg(i);
            BaseCommand nextCommand = currentCommand.getSubCommand(arg);

            if (nextCommand == null) {
                return Collections.emptyList();
            }
            currentCommand = nextCommand;
        }

        String lastArg = context.getArg(context.getArgs().length - 1);

        // Tab-Completion für Spieler oder Subcommands
        if (currentCommand.getSubCommands().isEmpty()) {
            return getPlayerNameSuggestions(lastArg);
        }

        // Zeige nur Subcommands, die der Sender ausführen kann
        return currentCommand.getSubCommands().keySet().stream()
                .filter(cmd -> cmd.startsWith(lastArg))
                .filter(cmd -> {
                    String permission = getSubCommandPermissions(cmd);
                    return permission == null || context.getSender().hasPermission(permission);
                })
                .collect(Collectors.toList());
    }

     */

    /*
    @Override
    public List<String> tabComplete(CommandContext context) {
        BaseCommand currentCommand = this;

        // Navigiere durch die Subcommands basierend auf den bisherigen Argumenten
        for (int i = 0; i < context.getArgs().length - 1; i++) {
            String arg = context.getArg(i);
            BaseCommand nextCommand = currentCommand.getSubCommand(arg);

            if (nextCommand == null) {
                return Collections.emptyList();
            }
            currentCommand = nextCommand;
            context = context.subContext(); // Aktualisiere den Kontext für das nächste Sub-Command
        }

        String lastArg = context.getArg(context.getArgs().length - 1);
        System.out.println("Last arg: " + lastArg);

        // Zeige nur Subcommands, die der Sender ausführen kann
        if (!currentCommand.getSubCommands().isEmpty()) {
            System.out.println("Subcommands are not empty");
            CommandContext finalContext = context;
            return currentCommand.getSubCommands().keySet().stream()
                    .filter(cmd -> cmd.startsWith(lastArg))
                    .filter(cmd -> {
                        String permission = getSubCommandPermissions(cmd);
                        return permission == null || finalContext.getSender().hasPermission(permission);
                    })
                    .collect(Collectors.toList());
        }

        System.out.println("Context args: " + context.getArgs().length);
        System.out.println("Arguments size: " + arguments.size());

        // Falls keine weiteren Child-Commands mehr vorhanden sind, Argumente vorschlagen
        // int argIndex = context.getArgs().length - 1;
        int argIndex = context.getArgs().length - 1;
        System.out.println("Arg index: " + argIndex);

        if (argIndex >= arguments.size()) {
            System.out.println("No more arguments");
            return Collections.emptyList(); // Keine weiteren Argumente
        }

        CommandArgument<?> currentArgument = arguments.get(argIndex);
        System.out.println("Current argument: " + currentArgument.getKey());
        System.out.println("Current argument type: " + currentArgument.getSuggestions().stream()
                // .filter(suggestion -> suggestion.startsWith(lastArg))
                .toList());

        return currentArgument.getSuggestions().stream()
                .filter(suggestion -> suggestion.startsWith(lastArg))
                .collect(Collectors.toList());
    }

     */

    @Override
    public List<String> tabComplete(CommandContext context) {
        try {
            CommandContextHolder.setSender(context.getSender());

            BaseCommand currentCommand = this;

        /*
        // Navigiere durch die Subcommands basierend auf den bisherigen Argumenten
        for (int i = 0; i < context.getArgs().length - 1; i++) {
            String arg = context.getArg(i);
            BaseCommand nextCommand = currentCommand.getSubCommand(arg);

            if (nextCommand == null) {
                return Collections.emptyList();
            }
            currentCommand = nextCommand;
            // context = context.subContext(); // Aktualisiere den Kontext für das nächste Sub-Command
        }
         */

            String lastArg = context.getArg(context.getArgs().length - 1);
            System.out.println("Last arg: " + lastArg);


            // Zeige nur Subcommands, die der Sender ausführen kann
            if (!currentCommand.getSubCommands().isEmpty()) {
                System.out.println("Subcommands are not empty");
                // CommandContext finalContext = context;
                return currentCommand.getSubCommands().keySet().stream()
                        .filter(cmd -> cmd.startsWith(lastArg))
                        .filter(cmd -> {
                            String permission = getSubCommandPermissions(cmd);
                            return permission == null || context.getSender().hasPermission(permission);
                        })
                        .collect(Collectors.toList());
            }

            // Anzahl der bereits eingegebenen Argumente
            int enteredArgs = context.getArgs().length;
            System.out.println("Entered args: " + enteredArgs);

            // Wenn bereits alle registrierten Argumente eingegeben wurden, keine weiteren Vorschläge machen
            if (enteredArgs > arguments.size()) {
                System.out.println("No more arguments");
                return Collections.emptyList();
            }

            // Hole das aktuelle Argument basierend auf der Nutzereingabe
            CommandArgument<?> currentArgument = arguments.get(enteredArgs - 1);
            String lastArg2 = context.getArg(enteredArgs - 1);
            System.out.println("Current argument: " + currentArgument.getKey());
            System.out.println("lastArg2: " + lastArg2);
            // Gib Vorschläge für das aktuelle Argument zurück
            return currentArgument.getSuggestions().stream()
                    .filter(suggestion -> suggestion.startsWith(lastArg2))
                    .collect(Collectors.toList());
        } finally {
            CommandContextHolder.clear();
        }

    }



    // Hilfsmethode, um eine Liste der Spielernamen zu erhalten, die mit einem Präfix beginnen
    private List<String> getPlayerNameSuggestions(String prefix) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}
