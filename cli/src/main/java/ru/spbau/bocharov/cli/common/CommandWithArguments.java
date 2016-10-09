package ru.spbau.bocharov.cli.common;

import ru.spbau.bocharov.cli.commands.Command;

import java.util.List;


/**
 * Class representing command with arguments.
 * Used by #{@link ru.spbau.bocharov.cli.execution.Executor} to substitute
 * variables from context in arguments and execute provided command.
 */
public class CommandWithArguments {
    public final Command command;
    public final List<QuoteString> arguments;

    public CommandWithArguments(Command cmd, List<QuoteString> args) {
        command = cmd;
        arguments = args;
    }
}
