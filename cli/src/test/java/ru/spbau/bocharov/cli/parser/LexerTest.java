package ru.spbau.bocharov.cli.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void shouldTokenizeSingleCommand() throws LexerException {
        assertEquals(
                Collections.singletonList("cmd"),
                createLexer().tokenize("cmd", Parser.PIPE));
    }

    @Test
    public void shouldTokenizeSingleCommandWithArguments() throws LexerException {
        assertEquals(
                Arrays.asList("cmd", "arg1", "arg2"),
                createLexer().tokenize("cmd  arg1 arg2", Parser.SPACE));
    }

    @Test
    public void shouldTokenizeSingleCommandWithQuotedArguments() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "arg1", "\"arg2   \"", "'arg3'"),
                createLexer().tokenize("cmd1 arg1 \"arg2   \" 'arg3'", Parser.SPACE));
    }

    @Test
    public void shouldTokenizeSingleCommandWithEscapedQuotes() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "arg1", "\"arg2", "\"", "'arg3'"),
                createLexer().tokenize("cmd1 arg1 \\\"arg2   \\\" 'arg3'", Parser.SPACE));
    }

    @Test
    public void shouldTokenizePipelineWithPipeInStrongQuote() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "arg1", "'arg2  |  '", "arg3"),
                createLexer().tokenize("cmd1 arg1 'arg2  |  ' arg3", Parser.SPACE));
    }

    @Test
    public void shouldTokenizeSimplePipeline() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "cmd2", "cmd3"),
                createLexer().tokenize("cmd1 | cmd2 | cmd3", Parser.PIPE));
    }

    @Test
    public void shouldParsePipelineWithSimpleArguments() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1 arg1 arg2   arg3", "cmd2 arg1", "cmd3  arg1  arg2"),
                createLexer().tokenize("cmd1 arg1 arg2   arg3 | cmd2 arg1 | cmd3  arg1  arg2", Parser.PIPE));
    }

    @Test
    public void shouldKeepEscapedBackslash() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "\\"),
                createLexer().tokenize("cmd1 \\\\", Parser.SPACE));
    }

    @Test
    public void shouldNotConsiderSpacesInsideEscapedQuotes() throws LexerException {
        assertEquals(
                Arrays.asList("cmd1", "\"", "\""),
                createLexer().tokenize("cmd1 \\\"    \\\"", Parser.SPACE));
    }

    @Test
    public void shouldNotSplitBySeparatorInsideStrongQuotes() throws LexerException {
        assertEquals(
                Collections.singletonList("cmd1 '|' cmd2"),
                createLexer().tokenize("cmd1 '|' cmd2", Parser.PIPE));
    }

    @Test(expected=LexerException.class)
    public void shouldThrowOnQuotesLeftOpen() throws LexerException {
        createLexer().tokenize("cmd1 \"", Parser.SPACE);
    }

    @Test(expected=LexerException.class)
    public void shouldThrowOnQuotesLeftOpenWithBackslashBefore() throws LexerException {
        createLexer().tokenize("cmd1 \\\\\"", Parser.SPACE);
    }

    @Test(expected=LexerException.class)
    public void shouldThrowOnUnmatchedQuotes() throws LexerException {
        createLexer().tokenize("cmd1 \"arg1'", Parser.SPACE);
    }

    @Test
    public void shouldTokenizeAssignment() throws LexerException {
        assertEquals(
                Arrays.asList("name", "value"),
                createLexer().tokenize("name=value", Parser.ASSIGNMENT));
    }

    private Lexer createLexer() {
        return new Lexer();
    }
}