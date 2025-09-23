package de.eternalcode.challenges.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();

    String description() default "";

    String permission() default "";

    String subPermission() default "";

    String[] aliases() default {};

    String usage() default "";

    int minArgs() default 0;

    int maxArgs() default Integer.MAX_VALUE;

    SenderTypes senderTypes() default SenderTypes.ANY;

    boolean async() default false;
}
