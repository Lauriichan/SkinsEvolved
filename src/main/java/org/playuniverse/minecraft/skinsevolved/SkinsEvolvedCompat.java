package org.playuniverse.minecraft.skinsevolved;

import java.util.regex.Pattern;

import org.bukkit.command.PluginCommand;
import org.playuniverse.minecraft.skinsevolved.command.CommandManager;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftCommand;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.skinsevolved.command.listener.redirect.ManagerRedirect;
import org.playuniverse.minecraft.skinsevolved.utils.compat.ICompat;

import net.sourcewriters.minecraft.vcompat.listener.PlayerListener;
import net.sourcewriters.minecraft.vcompat.listener.handler.IPlayerHandler;
import net.sourcewriters.minecraft.vcompat.provider.data.type.SkinDataType;
import net.sourcewriters.minecraft.vcompat.provider.entity.NmsPlayer;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.ILogger;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.utils.java.UniCode;
import net.sourcewriters.minecraft.vcompat.util.bukkit.BukkitColor;
import net.sourcewriters.minecraft.vcompat.util.logging.BukkitLogger;

public class SkinsEvolvedCompat implements ICompat, IPlayerHandler {

    public static final Pattern UUID_PATTERN = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
    public static final Pattern SHORT_UUID_PATTERN = Pattern.compile("([a-f0-9]{32})");
    public static final Pattern NAME_PATTERN = Pattern.compile("\\w{3,16}");
    public static final Pattern MAIL_PATTERN = Pattern.compile(
        "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
        Pattern.CASE_INSENSITIVE);

    private final CommandManager<MinecraftInfo> manager = new CommandManager<>();

    private final String prefix = "&bSkins&3Evolved &8" + UniCode.ARROWS_RIGHT + " &7";

    private MojangConfig config;
    private ILogger logger;

    @Override
    public void onStartup(SkinsEvolvedApp app) {
        logger = new BukkitLogger(app.getPlugin());
        config = new MojangConfig(logger, app.getPlugin().getDataFolder());
        config.reload();
        register(app, new MinecraftCommand(new ManagerRedirect(manager), app, "skinsevolved"));
        PlayerListener.register(app.getPlugin());
        PlayerListener.registerHandler(this);
        config.getContainer().forceLoad();
        new SkinsEvolvedCommands(app, this);
        logger.log("Started!");
    }

    public String prefix(String message) {
        return BukkitColor.apply(prefix + message);
    }

    public ILogger getLogger() {
        return logger;
    }

    public CommandManager<MinecraftInfo> getCommandManager() {
        return manager;
    }

    public MojangConfig getMojangConfig() {
        return config;
    }

    public String getPrefix() {
        return prefix;
    }

    private void register(SkinsEvolvedApp app, MinecraftCommand command) {
        PluginCommand plugin = app.getPlugin().getCommand(command.getId());
        plugin.setExecutor(command);
        plugin.setTabCompleter(command);
    }

    @Override
    public void onShutdown(SkinsEvolvedApp app) {
        config.getContainer().delete();
        logger.log("Stopped!");
    }

    @Override
    public void onJoin(NmsPlayer player) {
        if (!player.getBukkitPlayer().hasPermission("skinsevolved.permanent")) {
            return;
        }
        if (!player.getDataAdapter().has("skin", SkinDataType.WRAPPED_INSTANCE)) {
            player.setSkin(player.getRealSkin());
        }
        player.update();
    }

}
