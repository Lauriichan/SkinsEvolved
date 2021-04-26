package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.List;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class ForkNode<S> extends RootNode<S> {

    private final RootNode<S> fork;

    public ForkNode(String name, RootNode<S> fork) {
        super(name);
        this.fork = fork;
    }

    public RootNode<S> getFork() {
        return fork;
    }

    @Override
    public int execute(CommandContext<S> context) {
        return fork.execute(context);
    }
    
    @Override
    public List<String> complete(CommandContext<S> context) {
        return fork.complete(context);
    }

}
