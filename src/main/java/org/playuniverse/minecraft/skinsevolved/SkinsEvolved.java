package org.playuniverse.minecraft.skinsevolved;

import org.bukkit.plugin.java.JavaPlugin;

public class SkinsEvolved extends JavaPlugin {

    private SkinsEvolvedApp app;

    public SkinsEvolved() {
        super();
    }

    @Override
    public void onEnable() {
        (app = new SkinsEvolvedApp(this)).start();
    }

    @Override
    public void onDisable() {
        app.stop();
    }

}
