package com.otus.hw_04.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static String getSqlInsert(final String table, final String[] params) {
        final String columns = getInsertString(params, true);
        final String values = getInsertString(params, false);
        return String.format("INSERT INTO %s %s VALUES %s", table, columns, values);
    }

    public static String getSqlUpdate(final String table, final String[] params) {
        final String updateString = getUpdateString(params);
        return String.format("UPDATE %s SET %s WHERE id = :id", table, updateString);
    }

    private static String getUpdateString(final String[] params) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(params)
            .forEach(s -> {
                if (sb.length() > 1)
                    sb.append(" AND ");
                sb.append(s).append(" = :").append(s);
            });
        return sb.toString();
    }

    private static String getInsertString(final String[] params, final boolean isColumn) {
        final String delimiter = isColumn ? ", " : ", :";
        final String openBracket = isColumn ? "(" : "(:";
        return Arrays.stream(params)
            .collect(Collectors.joining(delimiter, openBracket, ")"));
    }

}
