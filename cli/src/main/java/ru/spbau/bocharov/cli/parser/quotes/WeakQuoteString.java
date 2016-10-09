package ru.spbau.bocharov.cli.parser.quotes;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;

/**
 * Class representing UNIX shell weak quote
 */
public class WeakQuoteString implements QuoteString {

    private static final char VARIABLE_MARKER = '$';

    private final String body;
    private String substitutedBody = null;

    public WeakQuoteString(String b) {
        body = b;
    }

    /**
     * Finds substrings in #{@link #body} starting with $ and existent in context
     * and replaces them by value from context
     *
     * @param context variables values
     * @return result of substitution variables from context into #{@link #body}
     */
    @Override
    public String substitute(Context context) {
        if (substitutedBody == null) {
            StringBuilder result = new StringBuilder("");
            for (int i = 0; i < body.length(); ++i) {
                char c = body.charAt(i);
                if (c == VARIABLE_MARKER && i + 1 < body.length() &&
                        Character.isLetterOrDigit(body.charAt(i + 1))) {
                    StringBuilder varName = new StringBuilder();
                    while (i + 1 < body.length() && Character.isLetterOrDigit(body.charAt(i + 1))) {
                        i++;
                        varName.append(body.charAt(i));
                    }

                    result.append(context.get(varName.toString()));
                } else {
                    result.append(c);
                }
            }
            substitutedBody = result.toString();
        }

        return substitutedBody;
    }
}
