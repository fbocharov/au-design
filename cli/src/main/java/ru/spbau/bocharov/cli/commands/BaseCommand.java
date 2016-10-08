package ru.spbau.bocharov.cli.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Base command class.
 * Implements Commands and defines some common methods such as {@link BaseCommand(String)}, {@link #getName()}
 * and {@link #addArguments(String...)}.
 * One should implement {@link Command} if needs to have specific behavior of those methods, otherwise
 * inheritance from this class is enough to add custom command.
 *
 * @author Fyodor Bocharov
 * @see CommandFactory
 */
public abstract class BaseCommand implements Command {

    protected List<String> arguments = new LinkedList<>();
    protected final String name;

    /**
     * @param commandName name of command
     */
    public BaseCommand(String commandName) {
        name = commandName;
    }


    @Override
    public void addArguments(String... args) {
        Collections.addAll(arguments, args);
    }

    @Override
    public String getName() {
        return name;
    }
}
