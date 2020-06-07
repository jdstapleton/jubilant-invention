package com.jamesstapleton.com.bems.utils;

import java.util.stream.Stream;

public class StreamUtils {
    public static <T> T first(Stream<T> stream) {
        return stream.findFirst().orElse(null);
    }
}
