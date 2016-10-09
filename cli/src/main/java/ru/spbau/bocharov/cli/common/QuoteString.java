package ru.spbau.bocharov.cli.common;

/**
 * Interface to represent shell quoted arguments.
 * See #{@link ru.spbau.bocharov.cli.parser.quotes.WeakQuoteString},
 * #{@link ru.spbau.bocharov.cli.parser.quotes.StrongQuoteString}
 */

public interface QuoteString {

    String substitute(Context context);
}
