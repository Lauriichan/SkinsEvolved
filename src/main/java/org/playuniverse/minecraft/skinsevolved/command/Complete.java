package org.playuniverse.minecraft.skinsevolved.command;

import java.util.List;

@FunctionalInterface
public interface Complete<S> {

    static final Complete<?> DEFAULT = ignore -> null;

    @SuppressWarnings("unchecked")
    public static <E> Complete<E> nothing() {
        return (Complete<E>) DEFAULT;
    }

    List<String> complete(CommandContext<S> context);

}
