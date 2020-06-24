package pl.pg.dbclient.helper;

import java.io.File;

public class ResourceHelper {

    public File getResource(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(resourceName).getFile());
    }
}
