package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.List;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class CommandNode<S> extends RootNode<S> {

    private final Command<S> command;
    private final Complete<S> complete;

    public CommandNode(String name, Command<S> command) {
        super(name);
        this.command = command;
        this.complete = Complete.nothing();
    }

    public CommandNode(String name, VoidCommand<S> command) {
        super(name);
        this.command = command;
        this.complete = Complete.nothing();
    }

    public CommandNode(String name, Command<S> command, Complete<S> complete) {
        super(name);
        this.command = command;
        this.complete = complete;
    }

    public CommandNode(String name, VoidCommand<S> command, Complete<S> complete) {
        super(name);
        this.command = command;
        this.complete = complete;
    }

    public Command<S> getCommand() {
        return command;
    }

    public Complete<S> getComplete() {
        return complete;
    }
    
    @Override
    public int execute(CommandContext<S> context) {
        return command.execute(context);
    }

    @Override
    public List<String> complete(CommandContext<S> context) {
        return complete.complete(context);
    }

}