package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.List;
import java.util.function.Function;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class MapNode<OS, NS> extends RootNode<OS> {

    private final Function<OS, NS> function;
    private final Node<NS> node;

    public MapNode(Function<OS, NS> function, Node<NS> node) {
        super(node.getName());
        this.function = function;
        this.node = node;
    }

    @Override
    public int execute(CommandContext<OS> context) {
        return node.execute(new CommandContext<>(function.apply(context.getSource()), context.getReader()));
    }

    @Override
    public List<String> complete(CommandContext<OS> context) {
        return node.complete(new CommandContext<>(function.apply(context.getSource()), context.getReader()));
    }

}
