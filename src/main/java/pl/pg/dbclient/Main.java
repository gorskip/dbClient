package pl.pg.dbclient;

import org.apache.commons.cli.CommandLine;
import pl.pg.dbclient.cmd.Cmd;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.config.DbConfigFinder;
import pl.pg.dbclient.mapper.Mapper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        CommandLine cmd = Cmd.build(args);

        if(cmd.hasOption("p")) {
            Mapper.prettify();
        }

        File configFile = new File(cmd.getOptionValue("c"));
        String configName = cmd.getOptionValue("n");
        String sql = cmd.getOptionValue("s");


        DbConfig dbConfig = new DbConfigFinder(configFile).find(configName);

        String output = new DbClient(dbConfig).query(sql).asString();

        System.out.println(output);
    }
}
