package ru.spbau.bocharov.cli.parser.quotes;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;

/**
 * Class representing UNIX shell strong quotes
 */
public class StrongQuoteString implements QuoteString {

    private final String body;

    public StrongQuoteString(String b) {
        body = b;
    }

    /**
     * Does nothing
     * @param context unused
     * @return #{@link body}
     */
    @Override
    public String substitute(Context context) {
        return body;
    }
}
