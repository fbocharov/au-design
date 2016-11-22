package ru.spbau.bocharov.cli.common;

/**
 * Interface to represent shell quoted arguments.
 * See #{@link ru.spbau.bocharov.cli.parser.quotes.WeakQuoteString},
 * #{@link ru.spbau.bocharov.cli.parser.quotes.StrongQuoteString}
 */

public interface QuoteString {

    /**
     * Substitutes values from context and return result of substitution
     * See #{@link ru.spbau.bocharov.cli.parser.quotes.WeakQuoteString},
     * #{@link ru.spbau.bocharov.cli.parser.quotes.StrongQuoteString}
     *
     * @param context values for substitution
     * @return result of substitution
     */
    String substitute(Context context);
}
