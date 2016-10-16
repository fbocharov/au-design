package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CDCommand extends BaseDirectoryCommand {
  /**
   * @param commandName name of command
   */
  public CDCommand(String commandName) {
    super(commandName);
  }

  @Override
  public void execute(IO io, Context context) throws Exception {
    String currentDirectory = System.getProperty(Context.JVM_DIRECTORY_PROPERTY);
    Path path = Paths.get(currentDirectory);
    if (arguments.isEmpty()) {
      System.setProperty(Context.JVM_DIRECTORY_PROPERTY, System.getProperty(Context.JVM_USER_HOME_PROPERTY));
    } else {
      final Path newCurrentDirectory = path.resolve(arguments.get(0));

      checkExists(newCurrentDirectory);
      checkDirectory(newCurrentDirectory);

      System.setProperty(Context.JVM_DIRECTORY_PROPERTY, newCurrentDirectory.toAbsolutePath().normalize().toString());
    }
  }
}
