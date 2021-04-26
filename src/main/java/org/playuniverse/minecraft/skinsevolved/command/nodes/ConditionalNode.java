package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.List;
import java.util.function.Predicate;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class ConditionalNode<S> extends RootNode<S> {

    private final Predicate<CommandContext<S>> predicate;
    private final Node<S> node;

    public ConditionalNode(Node<S> node, Predicate<CommandContext<S>> predicate) {
        super(node.getName());
        this.node = node;
        this.predicate = predicate;
    }

    @Override
    public int execute(CommandContext<S> context) {
        if (!predicate.test(context)) {
            return -1;
        }
        return node.execute(context);
    }

    @Override
    public List<String> complete(CommandContext<S> context) {
        if (!predicate.test(context)) {
            return null;
        }
        return node.complete(context);
    }

}