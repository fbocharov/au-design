package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.util.List;

public interface ICommand {

    void execute(IO io, Context context) throws Exception;

    void addArguments(String... args);

    void addArguments(List<String> args);
}
