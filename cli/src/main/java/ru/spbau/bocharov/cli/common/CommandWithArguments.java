package ru.spbau.bocharov.cli.common;

import ru.spbau.bocharov.cli.commands.ICommand;

import java.util.List;

public class CommandWithArguments {
    public final ICommand command;
    public final List<QuoteString> arguments;

    public CommandWithArguments(ICommand cmd, List<QuoteString> args) {
        command = cmd;
        arguments = args;
    }
}
