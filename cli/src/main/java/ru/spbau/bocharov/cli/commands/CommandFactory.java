package ru.spbau.bocharov.cli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Logger log = LogManager.getLogger(CommandFactory.class);
    private static final CommandFactory instance = new CommandFactory();

    public static CommandFactory getInstance() {
        return instance;
    }

    public ICommand createCommand(String commandName) throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        log.info("Create command " + commandName);
        return (ICommand) commandRegistry.get(nameToCommandType(commandName))
                .getConstructor(String.class)
                .newInstance(commandName);
    }

    private Map<CommandType, Class> commandRegistry = new HashMap<>();

    private CommandFactory() {
        commandRegistry.put(CommandType.CAT,        CatCommand.class);
        commandRegistry.put(CommandType.WC,         WCCommand.class);
        commandRegistry.put(CommandType.ECHO,       EchoCommand.class);
        commandRegistry.put(CommandType.PWD,        PWDCommand.class);
        commandRegistry.put(CommandType.EXTERNAL,   ExternalCommand.class);
        commandRegistry.put(CommandType.ASSIGNMENT, AssignmentCommand.class);
    }

    private CommandType nameToCommandType(String name) {
        switch (name) {
            case "cat":  return CommandType.CAT;
            case "wc":   return CommandType.WC;
            case "echo": return CommandType.ECHO;
            case "pwd":  return CommandType.PWD;
            case "=":    return CommandType.ASSIGNMENT;
            default:     return CommandType.EXTERNAL;
        }
    }

    private enum CommandType {
        EXTERNAL,

        CAT,
        WC,
        ECHO,
        PWD,
        ASSIGNMENT
    }
}
