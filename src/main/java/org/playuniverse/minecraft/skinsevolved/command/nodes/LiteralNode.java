package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class LiteralNode<S> extends RootNode<S> {

    protected final LinkedHashMap<String, Node<S>> children = new LinkedHashMap<>();
    protected String execution;

    public LiteralNode(String name) {
        super(name);
    }

    public Node<S> getChild(String name) {
        return children.get(name);
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    public boolean putChild(Node<S> node) {
        if (children.containsKey(node.getName())) {
            return false;
        }
        children.put(node.getName(), node);
        return true;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    @Override
    public int execute(CommandContext<S> context) {
        String name = context.getReader().skipWhitespace().readUnquoted();
        if (!hasChild(name) && !hasChild(name = execution)) {
            return 0;
        }
        return getChild(name).execute(context);
    }
    
    @Override
    public List<String> complete(CommandContext<S> context) {
        String name = context.getReader().skipWhitespace().readUnquoted();
        if (!hasChild(name) && !hasChild(name = execution)) {
            return new ArrayList<>(children.keySet());
        }
        return getChild(name).complete(context);
    }

}
