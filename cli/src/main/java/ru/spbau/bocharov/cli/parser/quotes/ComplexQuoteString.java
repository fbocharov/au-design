package ru.spbau.bocharov.cli.parser.quotes;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;

import java.util.List;

/**
 * Class to represent complex quoted strings such as #{@code x"$x"'$x'}
 */
public class ComplexQuoteString implements QuoteString {

    private final List<QuoteString> parts;
    private String substitutedBody = null;

    public ComplexQuoteString(List<QuoteString> quotes) {
        parts = quotes;
    }

    /**
     * Does substitute values from context in each part and concatenate
     * results.
     *
     * @param context variables values
     * @return result of concatenation
     */
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
