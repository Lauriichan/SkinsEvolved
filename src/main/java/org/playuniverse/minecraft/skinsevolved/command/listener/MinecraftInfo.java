package org.playuniverse.minecraft.skinsevolved.command.listener;

import org.bukkit.command.CommandSender;
import org.playuniverse.minecraft.skinsevolved.SkinsEvolved;

public class MinecraftInfo {

    private final SkinsEvolved base;
    private final CommandSender sender;

    public MinecraftInfo(SkinsEvolved base, CommandSender sender) {
        this.base = base;
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    public SkinsEvolved getBase() {
        return base;
    }

}
