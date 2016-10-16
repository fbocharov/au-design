package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.commands.ex.WrongFileTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public abstract class BaseDirectoryCommand extends BaseCommand {
  /**
   * @param commandName name of command
   */
  BaseDirectoryCommand(String commandName) {
    super(commandName);
  }

  static void checkExists(Path file) throws FileNotFoundException {
    if (!file.toFile().exists()) {
      String errorMessage = String.format("directory not found: %s",
          file.toAbsolutePath().toString());
      throw new FileNotFoundException(errorMessage);
    }
  }

  static void checkDirectory(Path directory) throws WrongFileTypeException {
    if (!directory.toFile().isDirectory()) {
      String errorMessage = String.format("file should be a directory: %s",
          directory.toAbsolutePath().toString());
      throw new WrongFileTypeException(errorMessage);
    }
  }
}
