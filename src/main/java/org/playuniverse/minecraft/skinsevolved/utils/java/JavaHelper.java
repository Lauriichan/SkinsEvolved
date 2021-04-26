package org.playuniverse.minecraft.skinsevolved.utils.java;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class JavaHelper {

    private static final String[] ESCAPE_CHARS;

    static {
        ESCAPE_CHARS = new String[128];
        ESCAPE_CHARS['\b'] = "\\b";
        ESCAPE_CHARS['\f'] = "\\f";
        ESCAPE_CHARS['\n'] = "\\n";
        ESCAPE_CHARS['\r'] = "\\r";
        ESCAPE_CHARS['\t'] = "\\t";
        ESCAPE_CHARS['\"'] = "\\\"";
        ESCAPE_CHARS['\\'] = "\\\\";
        for (int code = 0; code < 31; code++) {
            ESCAPE_CHARS[code] = String.format("\\u%04x", code);
        }
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

    private JavaHelper() {}

    public static java.lang.String formatDuration(java.time.Duration duration) {
        return FORMAT
            .format((TimeUnit.SECONDS.toMillis(duration.getSeconds()) + TimeUnit.NANOSECONDS.toMillis(duration.getNano())) / 1000.0f);
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> fromArray(T... array) {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    public static String jsonEscape(String string) {
        char[] array = string.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < array.length; index++) {
            char character = array[index];
            if (character < 128) {
                String escaped = ESCAPE_CHARS[character];
                if (escaped != null) {
                    builder.append(escaped);
                    continue;
                }
            }
            if (character == '\u2028') {
                builder.append("\\u2028");
                continue;
            }
            if (character == '\u2029') {
                builder.append("\\u2029");
                continue;
            }
            builder.append(character);
        }
        return builder.toString();
    }

}
