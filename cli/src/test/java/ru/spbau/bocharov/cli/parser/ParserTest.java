package ru.spbau.bocharov.cli.parser;

import org.junit.Test;
import ru.spbau.bocharov.cli.commands.CatCommand;
import ru.spbau.bocharov.cli.commands.ICommand;
import ru.spbau.bocharov.cli.commands.PWDCommand;
import ru.spbau.bocharov.cli.commands.WCCommand;
import ru.spbau.bocharov.cli.common.CommandWithArguments;
import ru.spbau.bocharov.cli.common.Context;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void shouldParseSimpleCommand() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<CommandWithArguments> result = createParser().parse("pwd");
        assertTrue(result.get(0).command instanceof PWDCommand);
        assertTrue(result.get(0).arguments.isEmpty());
    }

    @Test
    public void shouldParseSimpleCommandWithArgs() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<CommandWithArguments> result = createParser().parse("cat test1.txt test2.txt");

        assertTrue(result.get(0).command instanceof CatCommand);
    }

    @Test
    public void shouldRemoveBackslashesFromArguments() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<CommandWithArguments> result = createParser().parse("echo \\\"hello, world\\\"");
        Context context = new Context();

        assertEquals(
                result.get(0).arguments.get(0).substitute(context),
                "\"hello,");
        assertEquals(
                result.get(0).arguments.get(1).substitute(context),
                "world\"");
    }

    @Test
    public void shouldParsePipelineWithArgs() throws LexerException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        List<CommandWithArguments> result = createParser().parse("cat test1.txt | wc");

        assertTrue(result.size() == 2);
        assertTrue(result.get(0).command instanceof CatCommand);
        assertTrue(result.get(1).command instanceof WCCommand);
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