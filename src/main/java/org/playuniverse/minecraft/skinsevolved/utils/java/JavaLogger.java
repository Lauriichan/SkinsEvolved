package org.playuniverse.minecraft.skinsevolved.utils.java;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.ILogger;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.LogTypeId;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.LoggerState;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.color.LogType;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.logging.color.LogTypeMap;

public final class JavaLogger implements ILogger {

    private final LogTypeMap map = new LogTypeMap();

    private LoggerState state = LoggerState.STREAM;

    private final Logger delegate;

    public JavaLogger(final Logger delegate) {
        this.delegate = delegate;
    }

    @Override
    public ILogger close() {
        return this;
    }

    @Override
    public BiConsumer<Boolean, String> getCustom() {
        return null;
    }

    @Override
    public LoggerState getState() {
        return state;
    }

    @Override
    public String getThreadName() {
        return Thread.currentThread().getName();
    }

    @Override
    public LogType getType(String id) {
        return map.getById(id);
    }

    @Override
    public LogTypeMap getTypeMap() {
        return map;
    }

    @Override
    public boolean isColored() {
        return false;
    }

    @Override
    public ILogger log(String paramString) {
        delegate.log(Level.FINE, paramString);
        return this;
    }

    @Override
    public ILogger log(String... paramVarArgs) {
        for (String string : paramVarArgs) {
            log(string);
        }
        return this;
    }

    @Override
    public ILogger log(Throwable paramThrowable) {
        delegate.log(Level.SEVERE, paramThrowable.getLocalizedMessage(), paramThrowable);
        return this;
    }

    @Override
    public ILogger log(LogTypeId ignore, String paramString) {
        return log(paramString);
    }

    @Override
    public ILogger log(String ignore, String paramString2) {
        return log(paramString2);
    }

    @Override
    public ILogger log(LogTypeId ignore, String... paramVarArgs) {
        return log(paramVarArgs);
    }

    @Override
    public ILogger log(String ignore, String... paramVarArgs) {
        return log(paramVarArgs);
    }

    @Override
    public ILogger log(LogTypeId ignore, Throwable paramThrowable) {
        delegate.log(Level.SEVERE, paramThrowable.getLocalizedMessage(), paramThrowable);
        return this;
    }

    @Override
    public ILogger log(String ignore, Throwable paramThrowable) {
        delegate.log(Level.SEVERE, paramThrowable.getLocalizedMessage(), paramThrowable);
        return this;
    }

    @Override
    public ILogger setColored(boolean paramBoolean) {
        return this;
    }

    @Override
    public ILogger setCustom(BiConsumer<Boolean, String> paramBiConsumer) {
        return this;
    }

    @Override
    public ILogger setState(LoggerState paramLoggerState) {
        this.state = Objects.requireNonNull(paramLoggerState);
        return null;
    }

    @Override
    public ILogger setThreadName(String paramString) {
        return this;
    }

    @Override
    public ILogger setType(LogType paramLogType) {
        return this;
    }

}
