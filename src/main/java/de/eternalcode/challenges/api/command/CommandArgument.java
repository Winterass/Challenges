package de.eternalcode.challenges.api.command;

import java.util.List;

public class CommandArgument<T> {
    private final String key;
    private final Class<T> type;
    private final boolean required;
    private final CommandElement element;

    public CommandArgument(String key, Class<T> type, boolean required, CommandElement element) {
        this.key = key;
        this.type = type;
        this.required = required;
        this.element = element;
    }

    public String getKey() {
        return key;
    }

    public boolean isRequired() {
        return required;
    }

    public List<String> getSuggestions() {
        // Vorschläge dynamisch über das CommandElement abrufen
        return element.getSuggestions();
    }

    public Class<T> getType() {
        return type;
    }
}