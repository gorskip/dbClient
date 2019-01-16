package pl.pg.dbclient;

import org.apache.commons.cli.CommandLine;
import pl.pg.dbclient.cmd.Cmd;
import pl.pg.dbclient.config.DbConfig;
import pl.pg.dbclient.mapper.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        CommandLine cmd = Cmd.build(args);

        if(cmd.hasOption("p")) {
            JsonMapper.prettify();
        }

        File configFile = new File(cmd.getOptionValue("c"));
        String sql = cmd.getOptionValue("s");

        DbConfig dbConfig = JsonMapper.readValue(configFile, DbConfig.class);
        String output = JsonMapper.toString(new DbClient(dbConfig).query(sql));

        System.out.println(output);
    }
}
