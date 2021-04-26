package org.playuniverse.minecraft.skinsevolved.command.listener;

import org.bukkit.command.CommandSender;
import org.playuniverse.minecraft.skinsevolved.SkinsEvolvedApp;
import org.playuniverse.minecraft.skinsevolved.SkinsEvolvedCompat;

public class MinecraftInfo {

    private final SkinsEvolvedApp base;
    private final SkinsEvolvedCompat compat;
    private final CommandSender sender;

    public MinecraftInfo(SkinsEvolvedApp base, SkinsEvolvedCompat compat, CommandSender sender) {
        this.base = base;
        this.compat = compat;
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }
    
    public SkinsEvolvedCompat getCompat() {
        return compat;
    }

    public SkinsEvolvedApp getBase() {
        return base;
    }

}
