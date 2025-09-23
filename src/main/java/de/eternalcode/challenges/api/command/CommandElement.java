package de.eternalcode.challenges.api.command;

import java.util.List;

@FunctionalInterface
public interface CommandElement {
    List<String> getSuggestions();
}
