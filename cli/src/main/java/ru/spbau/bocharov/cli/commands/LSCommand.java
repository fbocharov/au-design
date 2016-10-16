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
 * Class representing UNIX ls command
 *
 * @author Vitaliy.Bibaev
 */
public class LSCommand extends BaseDirectoryCommand {
    private static final String FILES_SEPARATOR = "  ";

    /**
     * Constructs a new {@link LSCommand} instance
     *
     * @param commandName name of command
     */
    public LSCommand(String commandName) {
        super(commandName);
    }

    /**
     * Print list of files of the directory(-ies) specified by parameters of this command
     * <p>
     * If the first argument is not specified, then files in the current directory will be printed
     *
     * @param io      stdin, stdout and stderr of command
     * @param context some variables defined earlie
     * @throws Exception throw exception if directories not found any any IO exceptions happened
     */
    @Override
    public void execute(IO io, Context context) throws Exception {
        PrintStream stdout = new PrintStream(io.STDOUT);
        final String currentDirectory = System.getProperty(Context.JVM_DIRECTORY_PROPERTY);

        Paths.get(currentDirectory);
        Path root = new File(currentDirectory).toPath().toAbsolutePath();
        if (arguments.isEmpty()) {
            printDirectoryContent(root, root, false, stdout);
        } else {
            List<Path> directories = new ArrayList<>();

            for (String relativePath : arguments) {
                final Path directory = root.resolve(relativePath);

                checkExists(directory);
                checkDirectory(directory);
                directories.add(directory);
            }

            for (Path directory : directories) {
                printDirectoryContent(root, directory, directories.size() > 1, stdout);
            }
        }
    }

    private static void printDirectoryContent(Path root, Path directory, boolean printDirectoryName, PrintStream writer)
            throws IOException {
        final File[] files = directory.toFile().listFiles();
        if (files != null && files.length > 0) {
            if (printDirectoryName) {
                writer.println(String.format("%s:", root.relativize(directory).toString()));
                writer.print("\t");
            }

            writer.println(String.join(FILES_SEPARATOR, Arrays.stream(files)
                    .map(LSCommand::nameOfFile).collect(Collectors.toList())));
        }
    }

    private static String nameOfFile(File file) {
        return String.format("%s%s", file.getName(), file.isDirectory() ? File.separator : "");
    }
}
