package org.playuniverse.minecraft.skinsevolved;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.playuniverse.minecraft.skinsevolved.utils.java.JavaHelper;
import org.playuniverse.minecraft.skinsevolved.utils.java.JavaLogger;

import net.sourcewriters.minecraft.vcompat.VersionCompatProvider;
import net.sourcewriters.minecraft.vcompat.provider.data.persistence.PersistentContainer;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.JsonArray;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.JsonObject;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.JsonValue;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.ValueType;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.io.JsonParser;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.io.JsonWriter;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.json.value.JsonString;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.utils.java.Files;
import net.sourcewriters.minecraft.vcompat.shaded.syntaxapi.utils.java.Streams;
import net.sourcewriters.minecraft.vcompat.skin.DefaultMojangProvider;
import net.sourcewriters.minecraft.vcompat.skin.Mojang;
import net.sourcewriters.minecraft.vcompat.skin.PersistentSkinStore;

public class MojangConfig {

    public static final UUID IDENTIFIER = UUID.fromString("98247806-f3af-4b8b-aa70-11ba408d2143");

    private final JsonParser parser = new JsonParser();
    private final JsonWriter writer = new JsonWriter().setPretty(true).setSpaces(true);

    private final DefaultMojangProvider provider = new DefaultMojangProvider(IDENTIFIER);
    private final Mojang mojang;

    private final File file;
    private final Logger logger;

    private final PersistentContainer<String> container;

    public MojangConfig(Logger logger, File dataFolder) {
        this.logger = logger;
        this.file = Files.createFile(new File(dataFolder, "mojang.json"));
        this.container = new PersistentContainer<String>("skins", new File(dataFolder, "skins.nbt"),
            VersionCompatProvider.get().getControl().getDataProvider().getRegistry());
        this.mojang = new Mojang(new JavaLogger(logger), provider, new PersistentSkinStore<>(container));
    }

    public PersistentContainer<String> getContainer() {
        return container;
    }

    public DefaultMojangProvider getProvider() {
        return provider;
    }

    public static UUID getIdentifier() {
        return IDENTIFIER;
    }

    public Mojang getMojang() {
        return mojang;
    }

    public void reload() {
        provider.clear();
        JsonArray array = new JsonArray();
        load(array);
        save(array);
    }

    private void load(JsonArray array) {
        try {
            String content = Streams.toString(new FileInputStream(file));
            if (content == null || content.trim().isEmpty()) {
                return;
            }
            JsonValue<?> value = parser.fromString(content);
            if (value.getType() != ValueType.OBJECT) {
                return;
            }
            JsonObject object = (JsonObject) value;
            if (!object.has("profiles", ValueType.ARRAY)) {
                return;
            }
            for (JsonValue<?> entry : (JsonArray) object.get("profiles")) {
                if (entry.getType() != ValueType.OBJECT) {
                    continue;
                }
                JsonObject jsonProfile = (JsonObject) entry;
                if (!jsonProfile.has("username") || !jsonProfile.has("password")) {
                    continue;
                }
                String password = jsonProfile.get("password").getValue().toString();
                if (password.length() < 8) {
                    continue;
                }
                String username = jsonProfile.get("username").getValue().toString();
                if (!SkinsEvolved.NAME_PATTERN.matcher(username).matches() && !SkinsEvolved.MAIL_PATTERN.matcher(username).matches()) {
                    continue;
                }
                provider.create(username, password);
                JsonObject current = new JsonObject();
                current.set("username", new JsonString(JavaHelper.jsonEscape(username)));
                current.set("password", new JsonString(JavaHelper.jsonEscape(password)));
                array.add(current);
            }
        } catch (IOException exp) {
            logger.log(Level.WARNING, "Failed to load profiles", exp);
        }
    }

    private void save(JsonArray array) {
        JsonObject object = new JsonObject();
        object.set("profiles", array);
        if (array.isEmpty()) {
            JsonObject example = new JsonObject();
            example.set("username", new JsonString("<username>"));
            example.set("password", new JsonString("<password>"));
            array.add(example);
        }
        try {
            writer.toFile(object, file);
        } catch (IOException exp) {
            logger.log(Level.WARNING, "Failed to save profiles", exp);
        }
    }

}
