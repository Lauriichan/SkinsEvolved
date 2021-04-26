package org.playuniverse.minecraft.skinsevolved.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.playuniverse.minecraft.skinsevolved.command.nodes.*;

import com.syntaxphoenix.syntaxapi.utils.java.Arrays;

public class CommandManager<S> {

    private final ConcurrentHashMap<String, RootNode<S>> commands = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ArrayList<String>> aliases = new ConcurrentHashMap<>();
    
    private String global = null;

    public String[] getAliases(IPlugin plugin) {
        ArrayList<String> list = aliases.get(plugin.getId());
        return list == null ? new String[0] : list.toArray(new String[list.size()]);
    }

    public HashMap<RootNode<S>, String[]> getCommands() {
        HashMap<RootNode<S>, String[]> map = new HashMap<>();
        for (RootNode<S> node : commands.values()) {
            fillCommand(node, map);
        }
        return map;
    }

    private void fillCommand(RootNode<S> node, HashMap<RootNode<S>, String[]> map) {
        if (node instanceof ForkNode) {
            fillForkNode((ForkNode<S>) node, map);
            return;
        }
        if (node instanceof PluginNode) {
            fillCommand(((PluginNode<S>) node).getRoot(), map);
            return;
        }
        fillNode(node, map);
    }

    private void fillForkNode(ForkNode<S> node, HashMap<RootNode<S>, String[]> map) {
        RootNode<S> root = findNonFork(node);
        if (!map.containsKey(root)) {
            map.put(root, new String[] {
                node.getName()
            });
            return;
        }
        String[] aliases = map.get(root);
        Arrays.merge(String[]::new, aliases, node.getName());
        map.put(root, aliases);
    }

    private RootNode<S> findNonFork(ForkNode<S> node) {
        if (node.getFork() instanceof ForkNode) {
            return findNonFork((ForkNode<S>) node.getFork());
        }
        if (node.getFork() instanceof PluginNode) {
            PluginNode<S> plugin = (PluginNode<S>) node.getFork();
            if (plugin.getRoot() instanceof ForkNode) {
                return findNonFork((ForkNode<S>) plugin.getRoot());
            }
            return plugin.getRoot();
        }
        return node.getFork();
    }

    private void fillNode(RootNode<S> node, HashMap<RootNode<S>, String[]> map) {
        if (map.containsKey(node)) {
            String[] aliases = map.get(node);
            Arrays.merge(String[]::new, aliases, node.getName());
            map.put(node, aliases);
            return;
        }
        map.put(node, new String[] {
            node.getName()
        });
    }

    public CommandState register(RootNode<S> node, String... aliases) {
        return node instanceof PluginNode ? registerPluginNode((PluginNode<S>) node, aliases) : registerNode(node, aliases);
    }

    private CommandState registerNode(RootNode<S> node, String[] aliases) {
        if (commands.containsKey(node.getName())) {
            return CommandState.FAILED;
        }
        commands.put(node.getName(), node);
        ArrayList<String> conflicts = new ArrayList<>();
        for (String alias : aliases) {
            if (!commands.containsKey(alias)) {
                commands.put(alias, new ForkNode<>(alias, node));
                continue;
            }
            conflicts.add(alias);
        }
        return conflicts.isEmpty() ? CommandState.SUCCESS : CommandState.PARTIAL.setAliases(conflicts.toArray(new String[conflicts.size()]));
    }

    private CommandState registerPluginNode(PluginNode<S> node, String[] aliases) {
        CommandState state;
        if ((state = registerNode(node, aliases)) == CommandState.FAILED) {
            return state;
        }
        RootNode<S> root = node.getRoot();
        if (!commands.containsKey(root.getName())) {
            commands.put(root.getName(), new ForkNode<>(root.getName(), node));
        }
        IPlugin plugin = node.getPlugin();
        String[] conflicts = state.hasConflicts() ? state.getAliases() : new String[0];
        ArrayList<String> globalAliases = this.aliases.computeIfAbsent(plugin.getId(), ignore -> new ArrayList<>());
        ArrayList<String> globalConflicts = new ArrayList<>();
        for (String alias : aliases) {
            PluginNode<S> pluginNode;
            if (!Arrays.contains(conflicts, alias)) {
                globalAliases.add(alias);
                pluginNode = new PluginNode<>(plugin, commands.get(alias));
            } else {
                pluginNode = new PluginNode<>(plugin, new ForkNode<>(alias, node));
            }
            if (commands.containsKey(pluginNode.getName())) {
                globalConflicts.add(pluginNode.getName());
                continue;
            }
            globalAliases.add(alias);
            commands.put(pluginNode.getName(), pluginNode);
        }
        Collections.addAll(globalConflicts, conflicts);
        return globalConflicts.isEmpty() ? CommandState.SUCCESS : CommandState.PARTIAL.setAliases(globalConflicts.toArray(new String[globalConflicts.size()]));
    }

    public boolean unregisterCommand(String name) {
        if (!commands.containsKey(name)) {
            return false;
        }
        Optional<Entry<String, ArrayList<String>>> optional = aliases.entrySet().stream().filter(entry -> entry.getValue().contains(name))
            .findFirst();
        if (optional.isPresent()) {
            Entry<String, ArrayList<String>> entry = optional.get();
            entry.getValue().remove(name);
            if (entry.getValue().isEmpty()) {
                aliases.remove(entry.getKey());
            }
        }
        commands.remove(name);
        return true;
    }

    public CommandManager<S> setGlobal(String global) {
        this.global = global;
        return this;
    }

    public boolean hasGlobal() {
        return global != null;
    }

    public RootNode<S> getGlobal() {
        return global != null ? getCommand(global) : null;
    }

    public RootNode<S> getCommandOrGlobal(String name) {
        RootNode<S> node = getCommand(name);
        if (node == null && hasGlobal()) {
            return getGlobal();
        }
        return node;
    }

    public RootNode<S> getCommand(String name) {
        return commands.get(name);
    }

    public boolean hasCommand(String name) {
        return commands.containsKey(name);
    }

}
