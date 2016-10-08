package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.util.List;

/**
 * Base interface for all commands.
 * If you want to declare custom command, you need to implement this interface.
 * Also you need add your command to map inside of {@link CommandFactory}
 *
 * @author Fyodor Bocharov
 * @see CommandFactory
 */

public interface Command {


    /**
     * Runs command
     *
     * @param io stdin, stdout and stderr of command
     * @param context some variables defined earlie
     * @throws Exception if any errors occurs during execution
     */
    void execute(IO io, Context context) throws Exception;

    /**
     * Adds arguments to command
     *
     * @param args command arguments
     */
    void addArguments(String... args);

    /**
     * Returns command name
     *
     * @return string representing command name
     */
    String getName();
}
