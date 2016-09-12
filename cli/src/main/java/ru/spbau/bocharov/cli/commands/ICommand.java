package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.IO;

import java.util.List;

public interface ICommand {

    void execute(IO io);

    void addArguments(List<String> args);
}
