package ru.spbau.bocharov.cli.parser.quotes;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;

import java.util.List;

public class ComplexQuoteString implements QuoteString {

    private final List<QuoteString> parts;
    private String substitutedBody = null;

    public ComplexQuoteString(List<QuoteString> quotes) {
        parts = quotes;
    }

    @Override
    public String substitute(Context context) {
        if (substitutedBody == null) {
            StringBuilder result = new StringBuilder();
            for (QuoteString part : parts) {
                result.append(part.substitute(context));
            }
            substitutedBody = result.toString();

        }
        return substitutedBody;
    }
}
