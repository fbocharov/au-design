package ru.spbau.bocharov.cli.parser;

import org.junit.Test;
import ru.spbau.bocharov.cli.commands.CatCommand;
import ru.spbau.bocharov.cli.commands.ICommand;
import ru.spbau.bocharov.cli.commands.PWDCommand;
import ru.spbau.bocharov.cli.commands.WCCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void shouldParseSimpleCommand() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<ICommand> result = createParser().parse("pwd");
        assertTrue(result.get(0) instanceof PWDCommand);
    }

    @Test
    public void shouldParseSimpleCommandWithArgs() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<ICommand> result = createParser().parse("cat test1.txt test2.txt");
        assertTrue(result.get(0) instanceof CatCommand);
    }

    @Test
    public void shouldParsePipelineWithArgs() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<ICommand> result = createParser().parse("cat test1.txt | wc");
        assertTrue(result.size() == 2);
        assertTrue(result.get(0) instanceof CatCommand);
        assertTrue(result.get(1) instanceof WCCommand);
    }

    @Test(expected=LexerException.class)
    public void shouldThrowOnQuotesLeftOpen() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        createParser().parse("echo \"hello");
    }

    private Parser createParser() {
        return new Parser();
    }
}