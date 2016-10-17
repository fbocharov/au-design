package ru.spbau.bocharov.cli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Singleton for creating shell commands.
 * <p>
 * One who wants to add new command should add entry in #{@link #commandRegistry}.
 */

public class CommandFactory {

    private static final Logger log = LogManager.getLogger(CommandFactory.class);

    private static final CommandFactory instance = new CommandFactory();
    private static final Class<ExternalCommand> EXTERNAL_COMMAND_CLASS = ExternalCommand.class;

    public static CommandFactory getInstance() {
        return instance;
    }

    /**
     * Creates command (subclass of #{@link Command}) by name. If there is no command with such name,
     * creates #{@link ExternalCommand}.
     *
     * @param commandName name of command to create
     * @return object representing requested command
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public Command createCommand(String commandName) throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {

        log.info("Create command " + commandName);

        if (!commandRegistry.containsKey(commandName)) {
            return EXTERNAL_COMMAND_CLASS.getConstructor(String.class).newInstance(commandName);
        }

        return commandRegistry.get(commandName).getConstructor(String.class).newInstance(commandName);
    }

    private Map<String, Class<? extends Command>> commandRegistry = new HashMap<>();

    {
        commandRegistry.put("cat", CatCommand.class);
        commandRegistry.put("wc", WCCommand.class);
        commandRegistry.put("echo", EchoCommand.class);
        commandRegistry.put("pwd", PWDCommand.class);
        commandRegistry.put("grep", GrepCommand.class);
        commandRegistry.put("ls", LSCommand.class);
        commandRegistry.put("cd", CDCommand.class);
        commandRegistry.put("=", AssignmentCommand.class);
    }
}
