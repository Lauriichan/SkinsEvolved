package org.playuniverse.minecraft.skinsevolved;

import org.bukkit.plugin.java.JavaPlugin;

public final class SkinsEvolvedPlugin extends JavaPlugin {
    
    private SkinsEvolvedApp app;
    
    @Override
    public void onEnable() {
        (app = new SkinsEvolvedApp(this)).start();
    }
    
    @Override
    public void onDisable() {
        app.stop();
        app = null;
    }

}
