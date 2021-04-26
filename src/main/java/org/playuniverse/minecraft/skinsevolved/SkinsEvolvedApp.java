package org.playuniverse.minecraft.skinsevolved;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.playuniverse.minecraft.skinsevolved.utils.compat.ICompat;

import net.sourcewriters.minecraft.vcompat.updater.CompatApp;
import net.sourcewriters.minecraft.vcompat.updater.Reason;

public class SkinsEvolvedApp extends CompatApp {

    private final Logger bukkitLogger;
    private final SkinsEvolved plugin;

    private ICompat compat;

    SkinsEvolvedApp(SkinsEvolved plugin) {
        super(plugin.getDescription().getName(), 2);
        this.plugin = plugin;
        this.bukkitLogger = plugin.getLogger();
    }

    public SkinsEvolved getPlugin() {
        return plugin;
    }
    
    public ICompat getCompat() {
        return compat;
    }

    public Logger getBukkitLogger() {
        return bukkitLogger;
    }

    @Override
    protected void onFailed(Reason reason, String message) {
        switch (reason) {
        case ALREADY_REGISTERED:
            bukkitLogger.log(Level.SEVERE, "Failed to enable SkinsEvolved; There is a version of SkinsEvolved already installed!");
            break;
        case INCOMPATIBLE:
            bukkitLogger.log(Level.SEVERE, "Another plugin uses a incompatible version of vCompat; SkinsEvolved needs vCompat v2.X");
            break;
        case NO_CONNECTION:
            bukkitLogger.log(Level.SEVERE, "Unable to download a copy of vCompat -> No Connection. SkinsEvolved needs vCompat v2.X");
            bukkitLogger.log(Level.SEVERE, "You can manually donwload vCompat at https://github.com/SourceWriters/vCompat/releases");
            bukkitLogger.log(Level.SEVERE,
                "After downloading it simply put it into plugins/vCompat and be sure that the name is \"vCompat.jar\"");
            break;
        default:
            bukkitLogger.log(Level.SEVERE, "Something went wrong while loading vCompat; Therefore SkinsEvolved cant startup!");
            break;
        }
        bukkitLogger.log(Level.SEVERE, "-----");
        bukkitLogger.log(Level.SEVERE, "Reason: " + reason.name() + " / Message: " + message);
    }

    @Override
    protected void onReady() {
        (compat = new SkinsEvolvedCompat()).onStartup(this);
    }

    @Override
    protected void onShutdown() {
        if (compat != null) {
            compat.onShutdown(this);
        }
    }

}
