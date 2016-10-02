package ru.spbau.bocharov.cli.parser.quotes;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;

public class StrongQuoteString implements QuoteString {

    private final String body;

    public StrongQuoteString(String b) {
        body = b;
    }

    @Override
    public String substitute(Context context) {
        return body;
    }
}
