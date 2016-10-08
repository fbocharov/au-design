package ru.spbau.bocharov.cli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Logger log = LogManager.getLogger(CommandFactory.class);

    private static final CommandFactory instance = new CommandFactory();
    private static final Class<ExternalCommand> EXTERNAL_COMMAND_CLASS = ExternalCommand.class;

    public static CommandFactory getInstance() {
        return instance;
    }

    public Command createCommand(String commandName) throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {

        log.info("Create command " + commandName);

        if (!commandRegistry.containsKey(commandName)) {
            return EXTERNAL_COMMAND_CLASS.getConstructor(String.class).newInstance(commandName);
        }

        return (Command) commandRegistry.get(commandName)
                .getConstructor(String.class)
                .newInstance(commandName);
    }

    private Map<String, Class> commandRegistry = new HashMap<>();

    private CommandFactory() {
        commandRegistry.put("cat",  CatCommand.class);
        commandRegistry.put("wc",   WCCommand.class);
        commandRegistry.put("echo", EchoCommand.class);
        commandRegistry.put("pwd",  PWDCommand.class);
        commandRegistry.put("grep", GrepCommand.class);
        commandRegistry.put("=",    AssignmentCommand.class);
    }
}
