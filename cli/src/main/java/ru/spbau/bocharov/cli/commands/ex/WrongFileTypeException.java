package ru.spbau.bocharov.cli.commands.ex;

import java.io.IOException;

public class WrongFileTypeException extends IOException {
  public WrongFileTypeException(String message) {
    super(message);
  }
}
