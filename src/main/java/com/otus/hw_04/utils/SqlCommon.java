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

    public static String getSqlFindAll(final String table) {
        return String.format("SELECT * FROM %s", table);
    }

    public static String getSqlFindById(final String table) {
        return String.format("SELECT * FROM %s WHERE id = :id", table);
    }

    public static String getSqlDelete(final String table) {
        return String.format("DELETE FROM %s WHERE id = :id", table);
    }
}
