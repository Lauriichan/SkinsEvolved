package org.playuniverse.minecraft.skinsevolved.command.listener;

import org.playuniverse.minecraft.skinsevolved.SkinsEvolvedApp;

public abstract class MinecraftInfoAdapter {
    
    private final MinecraftInfo info;
    
    public MinecraftInfoAdapter(MinecraftInfo info) {
        this.info = info;
    }
    
    public SkinsEvolvedApp getBase() {
        return info.getBase();
    }

}
