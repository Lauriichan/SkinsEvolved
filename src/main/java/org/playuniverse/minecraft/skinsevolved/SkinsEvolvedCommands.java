package org.playuniverse.minecraft.skinsevolved;

import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.playuniverse.minecraft.skinsevolved.command.CommandContext;
import org.playuniverse.minecraft.skinsevolved.command.CommandManager;
import org.playuniverse.minecraft.skinsevolved.command.StringReader;
import org.playuniverse.minecraft.skinsevolved.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.skinsevolved.command.nodes.CommandNode;
import org.playuniverse.minecraft.skinsevolved.command.nodes.LiteralNode;

import net.sourcewriters.minecraft.vcompat.VersionCompatProvider;
import net.sourcewriters.minecraft.vcompat.provider.PlayerProvider;
import net.sourcewriters.minecraft.vcompat.provider.entity.NmsPlayer;
import net.sourcewriters.minecraft.vcompat.util.bukkit.BukkitColor;
import net.sourcewriters.minecraft.vcompat.util.minecraft.MojangProfileServer;
import net.sourcewriters.minecraft.vcompat.util.minecraft.SkinModel;
import net.sourcewriters.minecraft.vcompat.util.thread.PostAsync;

final class SkinsEvolvedCommands {

    private final SkinsEvolvedCompat compat;
    private final SkinsEvolvedApp app;

    private final PlayerProvider<?> playerProvider = VersionCompatProvider.get().getControl().getPlayerProvider();

    SkinsEvolvedCommands(SkinsEvolvedApp app, SkinsEvolvedCompat compat) {
        this.compat = compat;
        this.app = app;
        setup();
    }

    private void setup() {
        CommandManager<MinecraftInfo> manager = compat.getCommandManager();
        manager.register(new CommandNode<>("permissions", this::commandPermissions), "perms");
        manager.register(new CommandNode<>("reload", this::commandReload), "rl");
        manager.register(new CommandNode<>("update", this::commandUpdate), "refresh");
        LiteralNode<MinecraftInfo> node = new LiteralNode<>("use");
        node.putChild(new CommandNode<>("url", this::commandSkinUrl));
        node.putChild(new CommandNode<>("name", this::commandSkinName));
        node.putChild(new CommandNode<>("uuid", this::commandSkinUniqueId));
        node.setExecution("name");
        manager.register(node, "set");
    }

    private String prefix(String input){
        return compat.prefix(input);
    }

    private void commandSkinUrl(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!info.getSender().hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.skipWhitespace().hasNext()) {
            sender.sendMessage(prefix("Please specify an url"));
            return;
        }
        String url = reader.read();
        if (!reader.skipWhitespace().hasNext()) {
            sender.sendMessage(prefix("Please specify the skin model"));
            return;
        }
        SkinModel model = SkinModel.fromString(reader.read());
        Player target = getTarget(info, reader.skipWhitespace());
        if (target == null) {
            return;
        }
        sender.sendMessage(prefix("Trying to load skin for url '&e" + url + "&7'..."));
        PostAsync.forcePost(() -> {
            String targetName = sender == target ? "your" : target.getName() + "'s";
            NmsPlayer player = playerProvider.getPlayer(target);
            if (!compat.getMojangConfig().getMojang().request(player, url, model, 15000)) {
                sender.sendMessage(prefix("Unable to download skin for url '&c" + url + "&7'!"));
                return;
            }
            player.update();
            sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
        });
    }

    private void commandSkinName(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.skipWhitespace().hasNext()) {
            sender.sendMessage(prefix("Please specify a player name"));
            return;
        }
        String name = reader.read();
        int length = name.length();
        if (length < 3 || length > 16 || !SkinsEvolvedCompat.NAME_PATTERN.matcher(name).matches()) {
            sender.sendMessage(prefix("The name '" + name + "' is invalid!"));
            return;
        }
        Player target = getTarget(info, reader.skipWhitespace());
        if (target == null) {
            return;
        }
        sender.sendMessage(prefix("Trying to load skin for name '&e" + name + "&7'..."));
        PostAsync.forcePost(() -> {
            String targetName = sender == target ? "your" : target.getName() + "'s";
            NmsPlayer player = playerProvider.getPlayer(target);
            if (!compat.getMojangConfig().getMojang().request(player, name)) {
                sender.sendMessage(prefix("Unable to download skin for name '&c" + name + "&7'!"));
                return;
            }
            player.update();
            sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
        });
    }

    private void commandSkinUniqueId(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.skipWhitespace().hasNext()) {
            sender.sendMessage(prefix("Please specify a player uuid"));
            return;
        }
        String uuid = reader.read();
        int length = uuid.length();
        if (!(length == 36 || length == 32)) {
            sender.sendMessage(prefix("The uuid '&c" + uuid + "&7' is too short or too long (36 or 32 chars)"));
            return;
        }
        Matcher matcher = length == 32 ? SkinsEvolvedCompat.SHORT_UUID_PATTERN.matcher(uuid) : SkinsEvolvedCompat.UUID_PATTERN.matcher(uuid);
        if (!matcher.matches()) {
            sender.sendMessage(prefix("The uuid '&c" + uuid + "&7' is invalid!"));
            return;
        }
        UUID uniqueId = length == 32 ? MojangProfileServer.fromShortenId(uuid) : UUID.fromString(uuid);
        Player target = getTarget(info, reader.skipWhitespace());
        if (target == null) {
            return;
        }
        sender.sendMessage(prefix("Trying to load skin for uuid '&e" + uuid + "&7'..."));
        PostAsync.forcePost(() -> {
            String targetName = sender == target ? "your" : target.getName() + "'s";
            NmsPlayer player = playerProvider.getPlayer(target);
            if (!compat.getMojangConfig().getMojang().request(player, uniqueId)) {
                sender.sendMessage(prefix("Unable to download skin for uuid '&c" + uuid + "&7'!"));
                return;
            }
            player.update();
            sender.sendMessage(prefix("You updated &c" + targetName + " &7Skin."));
        });
    }

    private void commandPermissions(CommandContext<MinecraftInfo> context) {
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
        compat.getMojangConfig().reload();
        sender.sendMessage(prefix("Loaded &a" + compat.getMojangConfig().getProvider().getProfiles().size() + " &7Minecraft Profiles!"));
    }

    private void commandUpdate(CommandContext<MinecraftInfo> context) {
        MinecraftInfo info = context.getSource();
        CommandSender sender = info.getSender();
        if (!sender.hasPermission("skinsevolved.command.self")) {
            sender.sendMessage(prefix("You are lacking the permission '&cskinsevolved.command.self&7' to do this!"));
            return;
        }
        Player target = getTarget(info, context.getReader().skipWhitespace());
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

}
