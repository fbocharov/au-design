package ru.spbau.bocharov.m2u;

import org.apache.commons.cli.*;
import ru.spbau.bocharov.m2u.core.Chat;
import ru.spbau.bocharov.m2u.network.impl.DefaultIOService;
import ru.spbau.bocharov.m2u.ui.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Bootstrap {

    private static final String PORT_ARG_NAME = "port";
    private static final String USERNAME_ARG_NAME = "username";
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(PORT_ARG_NAME, true, "local port to start server");
        OPTIONS.addOption(USERNAME_ARG_NAME, true, "username");
    }

    public static void main(String[] args) {
        try {
            CommandLine cmd = parseArgs(args);
            short port = Short.valueOf(cmd.getOptionValue(PORT_ARG_NAME));
            String username = cmd.getOptionValue(USERNAME_ARG_NAME);

            DefaultIOService io = new DefaultIOService(port);
            Chat chat = new Chat(io, username);

            chat.start();
            SwingUtilities.invokeLater(() -> {
                new MainWindow("m2u", chat).setVisible(true);
            });
        } catch (ParseException e) {
            System.err.println("failed to parse args: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(OPTIONS, args);

        if (!cmdLine.hasOption(PORT_ARG_NAME)) {
            throw new ParseException("you should specify port");
        }

        if (!cmdLine.hasOption(USERNAME_ARG_NAME)) {
            throw new ParseException("you should specify username");
        }

        return cmdLine;
    }}
