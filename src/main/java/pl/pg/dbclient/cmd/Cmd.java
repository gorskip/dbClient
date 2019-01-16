package pl.pg.dbclient.cmd;

import lombok.experimental.UtilityClass;
import org.apache.commons.cli.*;
import pl.pg.dbclient.exception.CannotParseArgsException;

@UtilityClass
public class Cmd {

    public static CommandLine build(String[] args) {
        Options options = prepareOptions(args);
        CommandLineParser parser = new BasicParser();
        try {
            return parser.parse( options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ant", options );
            throw new CannotParseArgsException(e.getMessage());
        }
    }

    private static Options prepareOptions(String[] args) {
        Options options = new Options();

        Option config = OptionBuilder
                .hasArgs(1)
                .isRequired()
                .withDescription("Database configuration JSON file path")
                .create("c");

        Option sql = OptionBuilder
                .hasArgs(1)
                .isRequired()
                .withDescription("Sql query")
                .create("s");

        Option prettify = OptionBuilder
                .hasArg(false)
                .isRequired(false)
                .withDescription("Prettify output")
                .create("p");

        options
                .addOption(config)
                .addOption(sql)
                .addOption(prettify);

        return options;
    }
}
