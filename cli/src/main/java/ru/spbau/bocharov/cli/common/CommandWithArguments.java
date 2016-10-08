package ru.spbau.bocharov.cli.common;

import ru.spbau.bocharov.cli.commands.Command;

import java.util.List;

public class CommandWithArguments {
    public final Command command;
    public final List<QuoteString> arguments;

    public CommandWithArguments(Command cmd, List<QuoteString> args) {
        command = cmd;
        arguments = args;
    }
}
