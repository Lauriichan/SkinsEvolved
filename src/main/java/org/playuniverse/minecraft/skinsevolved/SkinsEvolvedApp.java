package org.playuniverse.minecraft.skinsevolved;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourcewriters.minecraft.vcompat.updater.CompatApp;
import net.sourcewriters.minecraft.vcompat.updater.Reason;
import net.sourcewriters.minecraft.vcompat.updater.shaded.syntaxapi.utils.java.Exceptions;

public final class SkinsEvolvedApp extends CompatApp {

    private final Logger bukkitLogger;
    private final SkinsEvolvedPlugin plugin;
    
    private SkinsEvolved evolved;
    
    public SkinsEvolvedApp(SkinsEvolvedPlugin plugin) {
        super(plugin.getName(), 3);
        this.plugin = plugin;
        this.bukkitLogger = plugin.getLogger();
    }

    @Override
    protected void onFailed(Reason reason, String message, Throwable throwable) {
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
        bukkitLogger.log(Level.SEVERE, "Reason: " + reason.name() + (throwable == null ? " / Message: " + message : ""));
        if(throwable != null) {
            bukkitLogger.log(Level.SEVERE, "\n" + Exceptions.stackTraceToString(throwable));
        }
    }
    
    @Override
    protected void onReady() {
        (evolved = new SkinsEvolved(plugin)).onEnable();
    }
    
    @Override
    protected void onShutdown() {
        evolved.onDisable();
        evolved = null;
    }

}
