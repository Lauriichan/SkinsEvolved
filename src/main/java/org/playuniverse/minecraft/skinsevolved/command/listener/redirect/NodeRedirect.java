package org.playuniverse.minecraft.skinsevolved.command.listener.redirect;

import org.playuniverse.minecraft.skinsevolved.command.listener.AbstractRedirect;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.skinsevolved.command.nodes.Node;

public class NodeRedirect extends AbstractRedirect {

    private final Node<MinecraftInfo> node;

    public NodeRedirect(Node<MinecraftInfo> node) {
        this.node = node;
    }

    @Override
    protected boolean isValid() {
        return node != null;
    }

    @Override
    protected Node<MinecraftInfo> handleComplete(String command) {
        return node;
    }

    @Override
    protected Node<MinecraftInfo> handleCommand(String command) {
        return node;
    }

    @Override
    protected boolean hasGlobal() {
        return true;
    }

}
