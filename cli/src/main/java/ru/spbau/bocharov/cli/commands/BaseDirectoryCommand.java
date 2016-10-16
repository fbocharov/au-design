package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.commands.ex.WrongFileTypeException;

import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * Base class for commands which are working with files and directories
 *
 * @author Vitaliy.Bibaev
 */
public abstract class BaseDirectoryCommand extends BaseCommand {
    BaseDirectoryCommand(String commandName) {
        super(commandName);
    }

    /**
     * Check that file exists, otherwise throws {@link FileNotFoundException}
     *
     * @param file the file for checking
     * @throws FileNotFoundException if {@code file} not exists
     */
    static void checkExists(Path file) throws FileNotFoundException {
        if (!file.toFile().exists()) {
            String errorMessage = String.format("directory not found: %s",
                    file.toAbsolutePath().toString());
            throw new FileNotFoundException(errorMessage);
        }
    }

    /**
     * Check that {@code file} represent directory, otherwise throws {@link WrongFileTypeException}
     *
     * @param file the file for checking
     * @throws WrongFileTypeException if {@code file} is not a file
     */
    static void checkDirectory(Path file) throws WrongFileTypeException {
        if (file.toFile().exists() && !file.toFile().isDirectory()) {
            String errorMessage = String.format("file should be a file: %s",
                    file.toAbsolutePath().toString());
            throw new WrongFileTypeException(errorMessage);
        }
    }
}
