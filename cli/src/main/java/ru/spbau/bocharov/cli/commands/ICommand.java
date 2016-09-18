package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.util.List;

public interface ICommand {

    void execute(IO io) throws IOException;

    void addArguments(String... args);
}
