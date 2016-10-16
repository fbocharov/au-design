package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vitaliy.Bibaev
 */
public class LSCommand extends BaseCommand {
  private static final String JVM_DIRECTORY_PROPERTY = "user.dir";
  private static final String FILES_SEPARATOR = "  ";

  /**
   * @param commandName name of command
   */
  public LSCommand(String commandName) {
    super(commandName);
  }

  @Override
  public void execute(IO io, Context context) throws Exception {
    PrintStream stdout = new PrintStream(io.STDOUT);
    final String currentDirectory = System.getProperty(JVM_DIRECTORY_PROPERTY);

    Path root = Paths.get(currentDirectory);
    if (arguments.isEmpty()) {
      printDirectoryContent(root, false, stdout);
    } else {
      List<Path> directories = new ArrayList<>();

      for (String relativePath : arguments) {
        final File directory = root.resolve(relativePath).toFile();

        checkExists(directory);
        checkDirectory(directory);
        directories.add(root.relativize(directory.toPath()));
      }

      for (Path directory : directories) {
        printDirectoryContent(directory, directories.size() > 1, stdout);
      }
    }
  }

  private void checkExists(File file) throws FileNotFoundException {
    if (!file.exists()) {
      String errorMessage = String.format("directory not found: %s",
          file.toPath().toAbsolutePath().toString());
      throw new FileNotFoundException(errorMessage);
    }
  }

  private void checkDirectory(File directory) throws FileNotFoundException {
    if (!directory.isDirectory()) {
      String errorMessage = String.format("Argument should be a directory: %s",
          directory.toPath().toAbsolutePath().toString());
      throw new FileNotFoundException(errorMessage);
    }
  }

  private static void printDirectoryContent(Path directory, boolean printDirectoryName, PrintStream writer)
      throws IOException {
    final File[] files = directory.toFile().listFiles();
    if (files != null && files.length > 0) {
      if (printDirectoryName) {
        writer.println(String.format("%s:", directory.toString()));
        writer.print("\t");
      }

      writer.println(String.join(FILES_SEPARATOR, Arrays.stream(files)
          .map(File::getName).collect(Collectors.toList())));
    }
  }
}
