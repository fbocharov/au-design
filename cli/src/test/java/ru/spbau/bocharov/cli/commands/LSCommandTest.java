package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class LSCommandTest extends DirectoryCommandTestCase {

    @Test
    public void shouldPrintCurrentDirectoryFilesWithoutArguments() throws Exception {
        final Command command = getCommand();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        command.execute(new IO(null, out, new ByteArrayOutputStream()), createEmptyContext());
        checkAllContains(out.toString("UTF-8"), ROOT_FILE_NAME, SRC_DIR, LIB_DIR);
    }

    @Test
    public void shouldPrintNestedDirectoryContent() throws Exception {
        final Command command = getCommand();
        command.addArguments(SRC_DIR);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        command.execute(new IO(null, out, new ByteArrayOutputStream()), createEmptyContext());
        checkAllContains(out.toString("UTF-8"), SRC_FILE_NAME);
    }

    @Test
    public void shouldAllFilesFromFewDirectories() throws Exception {
        final Command command = getCommand();
        command.addArguments(SRC_DIR, LIB_DIR);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        command.execute(new IO(null, out, new ByteArrayOutputStream()), createEmptyContext());
        checkAllContains(out.toString("UTF-8"), SRC_FILE_NAME, LIB1, LIB2);
    }

    @Test
    public void shouldUpperLevelContainsCurrent() throws Exception {
        final Command command = getCommand();
        command.addArguments("..");

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        command.execute(new IO(null, out, new ByteArrayOutputStream()), createEmptyContext());
        checkAllContains(out.toString("UTF-8"), getCurrentDirectory().getFileName().toString() + File.separator);
    }

    @Override
    protected String getCommandName() {
        return "ls";
    }

    private void checkAllContains(String output, String... files) {
        final HashSet<String> strings = new HashSet<>(Arrays.asList(output.trim().split("\\s+")));
        for (String file : files) {
            assertTrue(strings.contains(file));
        }
    }
}