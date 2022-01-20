package org.playuniverse.minecraft.skinsevolved.command.listener;

import org.playuniverse.minecraft.skinsevolved.SkinsEvolved;

public abstract class MinecraftInfoAdapter {
    
    private final MinecraftInfo info;
    
    public MinecraftInfoAdapter(MinecraftInfo info) {
        this.info = info;
    }
    
    public SkinsEvolved getBase() {
        return info.getBase();
    }

}
