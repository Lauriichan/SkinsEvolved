package org.playuniverse.minecraft.skinsevolved;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.playuniverse.minecraft.skinsevolved.command.CommandContext;
import org.playuniverse.minecraft.skinsevolved.command.CommandManager;
import org.playuniverse.minecraft.skinsevolved.command.StringReader;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftCommand;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.skinsevolved.command.listener.redirect.ManagerRedirect;
import org.playuniverse.minecraft.skinsevolved.command.nodes.CommandNode;
import org.playuniverse.minecraft.skinsevolved.command.nodes.LiteralNode;
import org.playuniverse.minecraft.skinsevolved.utils.compat.ICompat;

import com.syntaxphoenix.syntaxapi.logging.ILogger;
import com.syntaxphoenix.syntaxapi.utils.java.UniCode;

import net.sourcewriters.minecraft.vcompat.listener.PlayerListener;
import net.sourcewriters.minecraft.vcompat.listener.handler.IPlayerHandler;
import net.sourcewriters.minecraft.vcompat.reflection.PlayerProvider;
import net.sourcewriters.minecraft.vcompat.reflection.VersionControl;
import net.sourcewriters.minecraft.vcompat.reflection.data.type.SkinDataType;
import net.sourcewriters.minecraft.vcompat.reflection.entity.NmsPlayer;
import net.sourcewriters.minecraft.vcompat.utils.bukkit.BukkitColor;
import net.sourcewriters.minecraft.vcompat.utils.logging.BukkitLogger;
import net.sourcewriters.minecraft.vcompat.utils.minecraft.MojangProfileServer;
import net.sourcewriters.minecraft.vcompat.utils.minecraft.SkinModel;

public class SkinsEvolvedCompat implements ICompat, IPlayerHandler {

    public static final Pattern UUID_PATTERN = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
    public static final Pattern SHORT_UUID_PATTERN = Pattern.compile("([a-f0-9]{32})");
    public static final Pattern NAME_PATTERN = Pattern.compile("\\w{3,16}");
    public static final Pattern MAIL_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
    public static final Pattern URL_PATTERN = Pattern
        .compile("^(https?:\\/\\/)?([\\w\\Q$-_+!*'(),%\\E]+\\.)+(\\w{2,63})(:\\d{1,4})?([\\w\\Q/$-_+!*'(),%\\E]+\\.?[\\w])*\\/?$");

    private final PlayerProvider<?> playerProvider = VersionControl.get().getPlayerProvider();
    private final CommandManager<MinecraftInfo> manager = new CommandManager<>();

    private final String prefix = "&bSkins&3Evolved &8" + UniCode.ARROWS_RIGHT + " &7";

    private MojangConfig config;
    private ILogger logger;

    @Override
    public void onStartup(SkinsEvolvedApp app) {
        logger = new BukkitLogger(app.getPlugin());
        setupCommands(app);
        config = new MojangConfig(logger, app.getPlugin().getDataFolder());
        config.reload();
        register(app, new MinecraftCommand(new ManagerRedirect(manager), app, "skinsevolved"));
        PlayerListener.register(app.getPlugin());
        logger.log("Started!");
    }
    
    public ILogger getLogger() {
        return logger;
    }

    @Override
    public void onShutdown(SkinsEvolvedApp app) {
        logger.log("Stopped!");
    }

    private void setupCommands(SkinsEvolvedApp app) {
        manager.register(new CommandNode<>("permissions", context -> commandPermissions(context, app)), "perms");
        manager.register(new CommandNode<>("reload", context -> commandReload(context)), "rl");
        manager.register(new CommandNode<>("update", context -> commandUpdate(context)), "refresh");
        LiteralNode<MinecraftInfo> node = new LiteralNode<>("use");
        node.putChild(new CommandNode<>("url", this::commandSkinUrl));
        node.putChild(new CommandNode<>("name", this::commandSkinName));
        node.putChild(new CommandNode<>("uuid", this::commandSkinUniqueId));
        node.setExecution("name");
        manager.register(node, "set");
    }

    private void register(SkinsEvolvedApp app, MinecraftCommand command) {
        PluginCommand plugin = app.getPlugin().getCommand(command.getId());
        plugin.setExecutor(command);
        plugin.setTabCompleter(command);
    }

    private void commandSkinUrl(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!info.getSender().hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.hasNext()) {
            sender.sendMessage(prefix("Please specify an url"));
            return;
        }
        String url = reader.read();
        if (!URL_PATTERN.matcher(url).matches()) {
            sender.sendMessage(prefix("The url '" + url + "' is invalid."));
            return;
        }
        if (!reader.hasNext()) {
            sender.sendMessage(prefix("Please specify the skin model"));
            return;
        }
        SkinModel model = SkinModel.fromString(reader.read());
        Player target = getTarget(info, reader);
        if (target == null) {
            return;
        }
        String targetName = sender == target ? "your" : target.getName() + "'s";
        NmsPlayer player = playerProvider.getPlayer(target);
        if (!config.getMojang().request(player, url, model)) {
            sender.sendMessage(prefix("Unable to download skin for url '&c" + url + "&7'!"));
            return;
        }
        player.update();
        sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
    }

    private void commandSkinName(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.hasNext()) {
            sender.sendMessage(prefix("Please specify a player name"));
            return;
        }
        String name = reader.read();
        int length = name.length();
        if (length < 3 || length > 16 || !NAME_PATTERN.matcher(name).matches()) {
            sender.sendMessage(prefix("The name '" + name + "' is invalid!"));
            return;
        }
        Player target = getTarget(info, reader);
        if (target == null) {
            return;
        }
        String targetName = sender == target ? "your" : target.getName() + "'s";
        NmsPlayer player = playerProvider.getPlayer(target);
        if (!config.getMojang().request(player, name)) {
            sender.sendMessage(prefix("Unable to download skin for name '&c" + name + "&7'!"));
            return;
        }
        player.update();
        sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
    }

    private void commandSkinUniqueId(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.hasNext()) {
            sender.sendMessage(prefix("Please specify a player name"));
            return;
        }
        String uuid = reader.read();
        int length = uuid.length();
        if (length != 36 || length != 32) {
            sender.sendMessage(prefix("The uuid '&c" + uuid + "&7' is too short or too long (36 or 32 chars)"));
            return;
        }
        Matcher matcher = length == 32 ? SHORT_UUID_PATTERN.matcher(uuid) : UUID_PATTERN.matcher(uuid);
        if (!matcher.matches()) {
            sender.sendMessage(prefix("The uuid '&c" + uuid + "&7' is invalid!"));
            return;
        }
        UUID uniqueId = length == 32 ? MojangProfileServer.fromShortenId(uuid) : UUID.fromString(uuid);
        Player target = getTarget(info, reader);
        if (target == null) {
            return;
        }
        String targetName = sender == target ? "your" : target.getName() + "'s";
        NmsPlayer player = playerProvider.getPlayer(target);
        if (!config.getMojang().request(player, uniqueId)) {
            sender.sendMessage(prefix("Unable to download skin for uuid '&c" + uuid + "&7'!"));
            return;
        }
        player.update();
        sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
    }

    private void commandPermissions(CommandContext<MinecraftInfo> context, SkinsEvolvedApp app) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!info.getSender().hasPermission("skinsevolved.permissions")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.permissions&7' to do this!"));
            return;
        }
        List<Permission> permissions = app.getPlugin().getDescription().getPermissions();
        StringBuilder builder = new StringBuilder("&bSkins&3Evolved &8<-=-> &7Permissions\n");
        for (Permission permission : permissions) {
            builder.append(permission.getName()).append(" => ").append(permission.getDescription());
            if (permission.getChildren().isEmpty()) {
                builder.append('\n');
                continue;
            }
            for (Entry<String, Boolean> entry : permission.getChildren().entrySet()) {
                if (!entry.getValue()) {
                    continue;
                }
                builder.append("\n  + ").append(entry.getKey());
            }
            builder.append('\n');
        }
        builder.append("&bSkins&3Evolved &8<-=-> &7Permissions");
        info.getSender().sendMessage(BukkitColor.apply(builder.toString()));
    }

    private void commandReload(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.reload")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.reload&7' to do this!"));
            return;
        }
        sender.sendMessage(prefix("Reloading minecraft profiles..."));
        config.reload();
        sender.sendMessage(prefix("Loaded &a" + config.getProvider().getProfiles().size() + " &7Minecraft Profiles!"));
    }

    private void commandUpdate(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        Player target = getTarget(info, context.getReader());
        if (target == null) {
            return;
        }
        String targetName = sender == target ? "your" : target.getName() + "'s";
        NmsPlayer player = playerProvider.getPlayer(target);
        player.setSkin(MojangProfileServer.getSkin(player.getUniqueId()));
        player.update();
        sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
    }

    private Player getTarget(MinecraftInfo info, StringReader reader) {
        CommandSender sender = info.getSender();
        if (sender instanceof Player) {
            if (!(sender.hasPermission("skinsevolved.command.other") && !reader.hasNext())) {
                return (Player) sender;
            }
            String name = reader.read();
            Player player = Bukkit.getPlayer(name);
            if (player == null) {
                sender.sendMessage(prefix("The target '&c" + name + "&7' doesn't exist!"));
            }
            return player;
        }
        if (!reader.hasNext()) {
            sender.sendMessage(prefix("Please specify a target!"));
            return null;
        }
        String name = reader.read();
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            sender.sendMessage(prefix("The target '&c" + name + "&7' doesn't exist!"));
        }
        return player;
    }

    public String prefix(String message) {
        return BukkitColor.apply(prefix + message);
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
