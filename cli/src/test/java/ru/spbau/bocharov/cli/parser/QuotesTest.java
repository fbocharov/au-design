package ru.spbau.bocharov.cli.parser;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.QuoteString;
import ru.spbau.bocharov.cli.parser.quotes.ComplexQuoteString;
import ru.spbau.bocharov.cli.parser.quotes.StrongQuoteString;
import ru.spbau.bocharov.cli.parser.quotes.WeakQuoteString;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class QuotesTest {

    @Test
    public void shouldNotSubstituteInStrongQuote() {
        String body = "$x";
        QuoteString quoteString = new StrongQuoteString(body);
        Context context = new Context();
        context.put("x", "5");

        assertEquals(quoteString.substitute(context), body);
    }

    @Test
    public void shouldNotSubstituteInWeakQuote() {
        String body = "$,";
        QuoteString quoteString = new WeakQuoteString(body);
        Context context = new Context();

        assertEquals(quoteString.substitute(context), body);
    }

    @Test
    public void shouldDoSimpleSubstitutionInWeakQuote() {
        String body = "$var";
        QuoteString quoteString = new WeakQuoteString(body);
        Context context = new Context();
        context.put("var", "5");

        assertEquals("5", quoteString.substitute(context));
    }

    @Test
    public void shouldNotSubstituteInPartOfWeakQuote() {
        String body = "$var67";
        QuoteString quoteString = new WeakQuoteString(body);
        Context context = new Context();
        context.put("var", "5");

        assertEquals("", quoteString.substitute(context));
    }

    @Test
    public void shouldSubstituteInComplexQuote() {
        QuoteString weak = new WeakQuoteString("$var1");
        QuoteString strong = new StrongQuoteString("$var2");
        QuoteString quoteString = new ComplexQuoteString(Arrays.asList(weak, strong));
        Context context = new Context();
        context.put("var1", "5");
        context.put("var2", "5");

        assertEquals("5$var2", quoteString.substitute(context));
    }
}
