package org.playuniverse.minecraft.skinsevolved.command.nodes;

import java.util.List;

import org.playuniverse.minecraft.skinsevolved.command.*;

public class RedirectNode<S> extends SubNode<S> {

    private final SubNode<S> redirect;

    public RedirectNode(String name, SubNode<S> redirect) {
        super(name);
        this.redirect = redirect;
    }

    public SubNode<S> getRedirect() {
        return redirect;
    }

    @Override
    public int execute(CommandContext<S> context) {
        return redirect.execute(context);
    }
    
    @Override
    public List<String> complete(CommandContext<S> context) {
        return redirect.complete(context);
    }

}
