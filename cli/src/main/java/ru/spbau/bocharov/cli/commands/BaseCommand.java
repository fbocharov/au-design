package ru.spbau.bocharov.cli.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseCommand implements ICommand {

    protected List<String> arguments = new LinkedList<>();
    protected final String name;

    public BaseCommand(String commandName) {
        name = commandName;
    }

    public void addArguments(String... args) {
        Collections.addAll(arguments, args);
    }
}
