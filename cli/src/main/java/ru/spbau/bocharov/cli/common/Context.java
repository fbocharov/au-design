package ru.spbau.bocharov.cli.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing shell context.
 * Stores variables providing access to them by variable name.
 */
public class Context {

    private final Map<String, String> vars = new HashMap<>();

    public void put(String name, String value) {
        vars.put(name, value);
    }

    public String get(String name) {
        return vars.containsKey(name) ? vars.get(name) : "";
    }
}
