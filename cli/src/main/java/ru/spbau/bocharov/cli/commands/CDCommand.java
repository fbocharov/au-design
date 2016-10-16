package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class representing UNIX cd command
 *
 * @author Vitaliy.Bibaev
 */
public class CDCommand extends BaseDirectoryCommand {
  /**
   * Constructs a new {@link CDCommand} instance
   *
   * @param commandName name of command
   */
  public CDCommand(String commandName) {
    super(commandName);
  }

  /**
   * Change working directory for first argument passed directory.
   * <p>
   * If the first argument is not specified, then working directory will changed to user home
   *
   * @param io      stdin, stdout and stderr of command
   * @param context some variables defined earlie
   * @throws Exception throw exception if directories not found any any IO exceptions happened
   */
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
