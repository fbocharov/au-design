package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.commands.ex.WrongFileTypeException;
import ru.spbau.bocharov.cli.common.Context;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CDCommandTest extends DirectoryCommandTestCase {

  @Test
  public void shouldDoNothingIfArgIsCurrentDirectory() throws Exception {
    final Command command = getCommand();
    command.addArguments(".");

    String oldValue = System.getProperty(Context.JVM_DIRECTORY_PROPERTY);
    command.execute(createIO(), createEmptyContext());
    assertEquals(oldValue, System.getProperty(Context.JVM_DIRECTORY_PROPERTY));
  }

  @Test
  public void shouldMoveToHomeDirectory() throws Exception {
    final Command command = getCommand();

    assertNotEquals(System.getProperty(Context.JVM_USER_HOME_PROPERTY),
        getCurrentDirectory().toAbsolutePath().toString());

    command.execute(createIO(), createEmptyContext());

    assertEquals(System.getProperty(Context.JVM_USER_HOME_PROPERTY),
        getCurrentDirectory().toAbsolutePath().toString());
  }

  @Test
  public void shouldMoveToNestedDirectory() throws Exception {
    final Command command = getCommand();
    final Path currentDirectory = getCurrentDirectory();
    command.addArguments(SRC_DIR);
    command.execute(createIO(), createEmptyContext());
    assertEquals(getCurrentDirectory(), currentDirectory.resolve(SRC_DIR));
  }

  @Test
  public void shouldMoveIfPathIsNonNormalized() throws Exception {
    final Command command = getCommand();
    final Path before = getCurrentDirectory();
    command.addArguments(String.format("./././././%s/../%s/..", LIB_DIR, SRC_DIR));
    command.execute(createIO(), createEmptyContext());
    assertEquals(getCurrentDirectory(), before);
  }

  @Test(expected = WrongFileTypeException.class)
  public void shouldThrowIfFileIsNotDirectory() throws Exception {
    final Command command = getCommand();
    command.addArguments(ROOT_FILE_NAME);
    command.execute(createIO(), createEmptyContext());
  }

  @Test(expected = FileNotFoundException.class)
  public void shouldThrowIfFileIsNotExists() throws Exception {
    final Command command = getCommand();
    assertTrue(getCurrentDirectory().resolve(ROOT_FILE_NAME).toFile().exists());
    command.addArguments("asd");
    command.execute(createIO(), createEmptyContext());
  }

  @Override
  protected String getCommandName() {
    return "cd";
  }
}
