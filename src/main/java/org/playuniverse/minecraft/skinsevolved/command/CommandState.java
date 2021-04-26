package org.playuniverse.minecraft.skinsevolved.command;

public enum CommandState {

    SUCCESS,
    PARTIAL,
    FAILED;

    private String[] aliases;

    protected CommandState setAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean hasConflicts() {
        return aliases != null;
    }

}
