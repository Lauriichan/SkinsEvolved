package org.playuniverse.minecraft.skinsevolved.command;

@FunctionalInterface
public interface Command<S> {

    int execute(CommandContext<S> context);

}
