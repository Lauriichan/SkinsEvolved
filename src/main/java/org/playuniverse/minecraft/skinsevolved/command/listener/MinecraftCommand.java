package org.playuniverse.minecraft.skinsevolved.command.listener;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.playuniverse.minecraft.skinsevolved.SkinsEvolvedApp;
import org.playuniverse.minecraft.skinsevolved.SkinsEvolvedCompat;
import org.playuniverse.minecraft.skinsevolved.command.CommandContext;
import org.playuniverse.minecraft.skinsevolved.command.nodes.Node;

public final class MinecraftCommand implements CommandExecutor, TabCompleter {

    private final AbstractRedirect redirect;

    private final SkinsEvolvedApp owner;
    private final String name;
    private final String[] aliases;

    private final String fallbackPrefix;

    private Consumer<MinecraftInfo> noCommand = (info) -> info.getSender()
        .sendMessage(info.getCompat().prefix("Please specify a command!"));
    private BiConsumer<MinecraftInfo, String> nonExistent = (info, name) -> info.getSender()
        .sendMessage(info.getCompat().prefix("The command " + name + " doesnt exist!"));

    private BiConsumer<MinecraftInfo, Integer> execution;

    private BiConsumer<MinecraftInfo, Throwable> failedComplete = (info, error) -> info.getCompat().getLogger().log(error);
    private BiConsumer<MinecraftInfo, Throwable> failedCommand = (info, error) -> info.getCompat().getLogger().log(error);

    public MinecraftCommand(String name) {
        this.fallbackPrefix = null;
        this.redirect = null;
        this.owner = null;
        this.name = name;
        this.aliases = null;
    }

    public MinecraftCommand(AbstractRedirect redirect, SkinsEvolvedApp owner, String name, String... aliases) {
        this.fallbackPrefix = owner.getPlugin().getDescription().getName();
        this.redirect = redirect;
        this.owner = owner;
        this.name = name;
        this.aliases = aliases;
    }

    public MinecraftCommand(AbstractRedirect redirect, String fallbackPrefix, SkinsEvolvedApp owner, String name, String... aliases) {
        this.fallbackPrefix = fallbackPrefix;
        this.redirect = redirect;
        this.owner = owner;
        this.name = name;
        this.aliases = aliases;
    }

    public boolean isValid() {
        return owner != null && (redirect != null && redirect.isValid()) && (name != null && !name.trim().isEmpty()) && aliases != null;
    }

    /*
     * Getter
     */

    public String getId() {
        return name;
    }

    public SkinsEvolvedApp getOwner() {
        return owner;
    }

    public String getFallbackPrefix() {
        return fallbackPrefix;
    }

    public String[] getAliases() {
        return aliases;
    }

    public AbstractRedirect getRedirect() {
        return redirect;
    }

    /*
     * Functions
     */

    public MinecraftCommand setNoCommand(Consumer<MinecraftInfo> noCommand) {
        this.noCommand = noCommand;
        return this;
    }

    public MinecraftCommand setNonExistent(BiConsumer<MinecraftInfo, String> nonExistent) {
        this.nonExistent = nonExistent;
        return this;
    }

    public MinecraftCommand setExecution(BiConsumer<MinecraftInfo, Integer> execution) {
        this.execution = execution;
        return this;
    }

    public MinecraftCommand setFailedCommand(BiConsumer<MinecraftInfo, Throwable> failedCommand) {
        this.failedCommand = failedCommand;
        return this;
    }

    public MinecraftCommand setFailedComplete(BiConsumer<MinecraftInfo, Throwable> failedComplete) {
        this.failedComplete = failedComplete;
        return this;
    }

    /*
     * Bukkit implementation
     */

    @Override
    public List<String> onTabComplete(CommandSender sender, Command ignore, String label, String[] args) {
        List<String> output = redirectComplete(sender, args);
        if (output == null) {
            return Collections.emptyList();
        }
        String arg = args[args.length - 1];
        if (!arg.trim().isEmpty()) {
            int size = output.size();
            for (int index = 0; index < size; index++) {
                if (StringUtils.startsWithIgnoreCase(arg, output.get(index))) {
                    continue;
                }
                output.remove(index--);
                size--;
            }
        }
        return output;
    }

    private List<String> redirectComplete(CommandSender sender, String[] args) {
        if (args.length == 0 && !redirect.hasGlobal()) {
            return redirect.handleNullComplete(this, sender, args);
        }
        Node<MinecraftInfo> node = args.length == 0 ? redirect.handleComplete(null) : redirect.handleComplete(args[0]);
        if (node == null) {
            return redirect.handleNullComplete(this, sender, args);
        }
        MinecraftInfo info = new MinecraftInfo(owner, (SkinsEvolvedCompat) owner.getCompat(), sender);
        try {
            return node.complete(new CommandContext<>(info, buildArgs(args)));
        } catch (Throwable throwable) {
            if (failedComplete != null) {
                failedComplete.accept(info, throwable);
            }
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ignore, String label, String[] args) {
        MinecraftInfo info = new MinecraftInfo(owner, (SkinsEvolvedCompat) owner.getCompat(), sender);
        Node<MinecraftInfo> node = args.length == 0 ? (redirect.hasGlobal() ? redirect.handleCommand(null) : null)
            : redirect.handleCommand(args[0]);
        if (node == null) {
            if (args.length == 0) {
                if (noCommand != null) {
                    noCommand.accept(info);
                }
                return false;
            }
            if (nonExistent != null) {
                nonExistent.accept(info, args[0]);
            }
            return false;
        }
        try {
            Integer state = node.execute(new CommandContext<>(info, buildArgs(args)));
            if (execution != null) {
                execution.accept(info, state);
            }
            return false;
        } catch (Throwable throwable) {
            if (failedCommand != null) {
                failedCommand.accept(info, throwable);
            }
            return false;
        }
    }

    /*
     * Helper
     */

    public String buildArgs(String[] args) {
        if (args.length <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int index = 1; index < args.length; index++) {
            builder.append(args[index]).append(" ");
        }
        return builder.substring(0, builder.length() - 1);
    }

}
