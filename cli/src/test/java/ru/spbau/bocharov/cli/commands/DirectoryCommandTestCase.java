package ru.spbau.bocharov.cli.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

abstract class DirectoryCommandTestCase {
    static final String SRC_DIR = "src" + File.separator;
    static final String LIB_DIR = "lib" + File.separator;
    static final String ROOT_FILE_NAME = "Makefile";
    static final String SRC_FILE_NAME = "main.cpp";
    static final String LIB1 = "boost.lib";
    static final String LIB2 = "stdlib.lib";

    private static final int BUFFER_SIZE = 1024 * 512; // 0.5mb

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        folder.newFile(ROOT_FILE_NAME);
        Files.createFile(folder.newFolder(SRC_DIR).toPath().resolve(SRC_FILE_NAME));

        final Path libDirectory = folder.newFolder(LIB_DIR).toPath();
        Files.createFile(libDirectory.resolve(LIB1));
        Files.createFile(libDirectory.resolve(LIB2));

        System.setProperty(Context.JVM_DIRECTORY_PROPERTY, folder.getRoot().getAbsolutePath());
    }


    IO createIO() {
        return new IO(new ByteArrayInputStream(new byte[BUFFER_SIZE]), new ByteArrayOutputStream(), new ByteArrayOutputStream());
    }

    Command getCommand() throws Exception {
        return CommandFactory.getInstance().createCommand(getCommandName());
    }

    Context createEmptyContext() {
        return new Context();
    }

    Path getCurrentDirectory() {
        return Paths.get(System.getProperty(Context.JVM_DIRECTORY_PROPERTY));
    }

    protected abstract String getCommandName();
}
