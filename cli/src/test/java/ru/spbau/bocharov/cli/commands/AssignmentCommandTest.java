package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.Context;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class AssignmentCommandTest {

    @Test(expected=Exception.class)
    public void shouldThrowOnNullContext() throws Exception {
        createCommand().execute(null, null);
    }

    @Test(expected=Exception.class)
    public void shouldThrowOnWrongNumberOfArguments() throws Exception {
        Context c = createContext();
        createCommand().execute(null, c);
    }

    @Test
    public void shouldAddValueToContext() throws Exception {
        Context c = createContext();
        Command cmd = createCommand();
        cmd.addArguments("name", "value");

        cmd.execute(null, c);

        assertEquals(c.get("name"), "value");
    }

    @Test
    public void shouldUpdateValueInContext() throws Exception {
        Context c = createContext();
        c.put("name", "value1");
        Command cmd = createCommand();
        cmd.addArguments("name", "value2");

        cmd.execute(null, c);

        assertEquals(c.get("name"), "value2");
    }

    private Command createCommand() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CommandFactory.getInstance().createCommand("=");
    }

    private Context createContext() {
        return new Context();
    }
}