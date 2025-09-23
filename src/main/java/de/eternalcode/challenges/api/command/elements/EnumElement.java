package de.eternalcode.challenges.api.command.elements;

import de.eternalcode.challenges.api.command.CommandElement;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumElement<T extends Enum<T>> implements CommandElement {
    private final Class<T> enumClass;
    private final Function<T, String> displayNameFunction;

    public EnumElement(Class<T> enumClass, Function<T, String> displayNameFunction) {
        this.enumClass = enumClass;
        this.displayNameFunction = displayNameFunction;
    }

    @Override
    public List<String> getSuggestions() {
        if (displayNameFunction == null) {
            // Rückgabe der nicht "lesbaren" Namen aller Enum-Werte
            return Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }
        // Rückgabe der "lesbaren" Namen aller Enum-Werte
        return Arrays.stream(enumClass.getEnumConstants())
                .map(displayNameFunction)
                .collect(Collectors.toList());
    }
}