package pl.pg.dbclient.config;


import org.junit.jupiter.api.Test;
import pl.pg.dbclient.helper.ResourceHelper;
import pl.pg.dbclient.exception.DbConfigException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DbConfigFinderTest {

    @Test
    void shouldFindDbConfigForGivenName() {
        String configName = "company database2";
        String configFileName = "correct_dbConfigs.json";
        File resource = new ResourceHelper().getResource(configFileName);
        DbConfig dbConfig = new DbConfigFinder(resource).find(configName);
        assertTrue(dbConfig.getName().equals(configName));
        assertTrue(dbConfig.getUsername().equals("admin2"));
        assertTrue(dbConfig.getPassword().equals("postgres2"));
        assertTrue(dbConfig.getDriverClassName().equals("org.postgresql.Driver"));
        assertTrue(dbConfig.getUrl().equals("jdbc:postgresql://localhost:5432/company2"));
    }

    @Test()
    void shouldFindMoreThanOneConfigWithTheSameName() {
        String configName = "company database";
        DbConfigException dbConfigException = assertThrows(DbConfigException.class, () -> {
            String configFileName = "2_dbConfigs_with_the_same_name.json";
            File resource = new ResourceHelper().getResource(configFileName);
            new DbConfigFinder(resource).find(configName);
        });
        assertTrue(dbConfigException.getMessage()
                .equals("Found 2 configurations with name: [".concat(configName).concat("]")));
    }

    @Test
    void shouldNotFindConfigForGivenName() {
        String configName = "wrong_name";
        DbConfigException dbConfigException = assertThrows(DbConfigException.class, () -> {
            String configFileName = "correct_dbConfigs.json";
            File resource = new ResourceHelper().getResource(configFileName);
            new DbConfigFinder(resource).find(configName);
        });
        assertTrue(dbConfigException.getMessage()
                .equals("Cannot find configuration with name: [".concat(configName).concat("]")));
    }
}