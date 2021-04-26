package org.playuniverse.minecraft.skinsevolved.command;

public class CommandContext<S> {

    private final S source;
    private final StringReader reader;

    public CommandContext(S source, String input) {
        this(source, new StringReader(input));
    }

    public CommandContext(S source, StringReader reader) {
        this.source = source;
        this.reader = reader;
    }

    public S getSource() {
        return source;
    }

    public StringReader getReader() {
        return reader;
    }

}
