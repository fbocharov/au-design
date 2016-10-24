package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;
import org.apache.commons.cli.*;
import ru.spbau.bocharov.cli.utils.IOUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Class representing UNIX grep command
 */

public class GrepCommand extends BaseCommand {

    private CommandLineParser cmdParser = new DefaultParser();
    private Options cmdOptions = new Options();
    {
        cmdOptions.addOption("i", false, "ignore case");
        cmdOptions.addOption("w", false, "search whole words only");
        cmdOptions.addOption("A", true, "print only certain number of lines");
    }

    /**
     * @param commandName name of command
     */
    public GrepCommand(String commandName) {
        super(commandName);
    }

    /**
     * Tries to find files in arguments. If there is no files provided,
     * reads data from io.STDIN.
     * User should specify search pattern and also there is optional arguments:
     *   -i     to ignore case
     *   -w     to match whole word
     *   -A n   to print only firts n matched lines
     *
     * @param io stdin, stdout and stderr of command
     * @param context some variables defined earlier
     * @throws Exception if arguments parsing errors occurs
     */
    @Override
    public void execute(IO io, Context context) throws Exception {
        PrintStream stdout = new PrintStream(io.STDOUT);
        PrintStream stderr = new PrintStream(io.STDERR);

        if (arguments.isEmpty()) {
            stderr.println("can't execute grep without any argument");
            return;
        }

        CommandLine cmd = cmdParser.parse(cmdOptions, arguments.toArray(new String[arguments.size()]));
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            stderr.println("you should specify search pattern");
            return;
        }

        Integer maxPrintCount = cmd.hasOption('A') ?
                Integer.valueOf(cmd.getOptionValue('A').trim()) : 0;
        final Integer[] printCount = {maxPrintCount};

        Pattern pattern = createRegexPatter(args[0], cmd.hasOption('w'), cmd.hasOption('i'));
        if (args.length > 1) {
            Arrays.asList(args).subList(1, args.length).forEach(file -> {
                try (Stream<String> lines = Files.lines(Paths.get(file))) {
                    Iterator<String> it = lines.iterator();
                    while (it.hasNext()) {
                        String line = it.next();
                        if (pattern.matcher(line).find()) {
                            stdout.println(line);
                            printCount[0] = 0;
                        } else if (printCount[0] < maxPrintCount) {
                            stdout.println(line);
                            printCount[0]++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(stderr);
                }
            });
        } else if (io.STDIN != null) {
            IOUtils.interactive(io.STDIN, line -> {
                if (pattern.matcher(line).find()) {
                    stdout.println(line);
                    printCount[0] = 0;
                } else if (printCount[0] < maxPrintCount) {
                    stdout.println(line);
                    printCount[0]++;
                }
            });
        } else {
            stderr.println("you should specify files or input strings to match");
        }
    }

    private static Pattern createRegexPatter(String patternBase, boolean w, boolean i) {
        if (w) {
            patternBase = "\\b" + patternBase + "\\b";
        }
        int flags = i ? Pattern.CASE_INSENSITIVE : 0;
        return Pattern.compile(patternBase, flags);
    }
}
