package pl.pg.dbclient.config;

import pl.pg.dbclient.exception.DbConfigException;
import pl.pg.dbclient.mapper.JsonMapper;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DbConfigFinder {

    private final File configFile;

    public DbConfigFinder(File configFile) {
        this.configFile = configFile;
    }

    public DbConfig find(String name) {
        List<DbConfig> dbConfigs = JsonMapper.readToList(configFile, DbConfig.class);
        List<DbConfig> foundDbConfigs = dbConfigs.stream()
                .filter(dbConfig -> name.equals(dbConfig.getName()))
                .collect(Collectors.toList());
        if(foundDbConfigs.isEmpty()) {
            throw new DbConfigException("Cannot find configuration with name: [".concat(name).concat("]"));
        }
        if(foundDbConfigs.size() > 1) {
            throw new DbConfigException("Found " + dbConfigs.size() + " configurations with name: [".concat(name).concat("]"));
        }
        return foundDbConfigs.get(0);
    }
}
