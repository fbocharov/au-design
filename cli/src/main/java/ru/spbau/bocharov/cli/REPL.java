package ru.spbau.bocharov.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.bocharov.cli.common.CommandWithArguments;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;
import ru.spbau.bocharov.cli.execution.Executor;
import ru.spbau.bocharov.cli.parser.Parser;

import java.util.List;
import java.util.Scanner;

public class REPL {

    private static final Logger log = LogManager.getLogger(REPL.class);

    public static void main(String[] args) {
        printGreeting();
        loop();
    }

    private static void loop() {
        Parser parser = new Parser();
        Executor executor = new Executor();
        Context context = new Context();
        IO io = new IO(System.in, System.out, System.err);

        log.info("Start REPL...");
        while (true) {
            String input = getUserInput();
            if (input.isEmpty()) {
                break;
            }

            try {
                List<CommandWithArguments> commands = parser.parse(input);
                executor.execute(io, context, commands);
            } catch (Exception e) {
                printError(e.getMessage());
                log.error("Exception in REPL:", e);
            }
        }
        log.info("End REPL");
    }

    private static String getUserInput() {
        Scanner sc = new Scanner(System.in);
        int emptyLineCount = 0;
        printPrompt();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                return input;
            }
            emptyLineCount++;
            if (emptyLineCount == 2) {
                break;
            }
            printPrompt();
        }
        return "";
    }

    private static void printPrompt() {
        System.out.print("> ");
    }

    private static void printError(String message) {
        System.err.println("error: " + message);
    }

    private static void printGreeting() {
        System.out.println("Welcome to CLI 1.0 by Fyodor Bocharov");
        System.out.println();
    }
}
