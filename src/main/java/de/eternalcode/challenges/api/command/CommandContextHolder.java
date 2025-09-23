package de.eternalcode.challenges.api.command;

import org.bukkit.command.CommandSender;

public class CommandContextHolder {
    private static final ThreadLocal<CommandSender> senderHolder = new ThreadLocal<>();

    public static void setSender(CommandSender sender) {
        senderHolder.set(sender);
    }

    public static CommandSender getSender() {
        return senderHolder.get();
    }

    public static void clear() {
        senderHolder.remove();
    }
}

