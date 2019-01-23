package com.otus.hw_04.utils;

import java.util.Collections;
import java.util.Map;

public final class SqlCommon {

    public static Map<String, Object> getNamedParam(final String key, final Object value) {
        return Collections.singletonMap(key, value);
    }

    public static String getAnyMatchParam(final String param) {
        return "%" + param + "%";
    }

}
