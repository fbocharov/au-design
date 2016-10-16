package ru.spbau.bocharov.cli.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.bocharov.cli.commands.ex.WrongFileTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class BaseDirectoryCommandTest {
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldCheckThatExists() throws IOException {
    final File file = folder.newFile("file");
    BaseDirectoryCommand.checkExists(file.toPath());
  }

  @Test(expected = FileNotFoundException.class)
  public void shouldCheckThatNotExists() throws FileNotFoundException {
    BaseDirectoryCommand.checkExists(folder.getRoot().toPath().resolve("file"));
  }

  @Test
  public void shouldCheckThatIsDirectory() throws IOException {
    final File directory = folder.newFolder();
    assertTrue(directory.exists());
    assertTrue(directory.isDirectory());
    BaseDirectoryCommand.checkDirectory(directory.toPath());
  }

  @Test(expected = WrongFileTypeException.class)
  public void shouldCheckThatIsNotDirectory() throws IOException {
    final File file = folder.newFile("file");
    assertTrue(file.exists());
    assertFalse(file.isDirectory());
    BaseDirectoryCommand.checkDirectory(file.toPath());
  }
}
