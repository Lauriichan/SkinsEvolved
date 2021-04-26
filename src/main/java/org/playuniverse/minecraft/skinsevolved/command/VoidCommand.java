package org.playuniverse.minecraft.skinsevolved.command;

import java.util.function.Consumer;

@FunctionalInterface
public interface VoidCommand<S> extends Command<S> {

    public default int execute(CommandContext<S> context) {
        run(context);
        return 1;
    }

    void run(CommandContext<S> context);

    public static <S> VoidCommand<S> of(Consumer<CommandContext<S>> consumer) {
        return context -> consumer.accept(context);
    }

}
