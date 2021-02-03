package utils;

import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class AppProperties {

    private static final String TEST_PROPERTIES_FILE = "src\\test\\resources\\test.properties";

    private static Properties testProps;
    private static Properties systemProps;
    private static Map<String, String> envProperties;

    private static Map<String, String> appProps;

    static {

        log.info("Setting up properties");

        appProps = new HashMap<>();
        envProperties = System.getenv();
        systemProps = System.getProperties();
        testProps = new Properties();
        try {
            testProps.load(new FileInputStream(TEST_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error("Error while getting test properties from file: {}", TEST_PROPERTIES_FILE, e);
        }
    }

    public static String getProperty(String key, String defaultProperty) {

        if (appProps.containsKey(key)) {

            return appProps.get(key);
        } else if (envProperties.containsKey(key)) {

            return envProperties.get(key);
        } else if (systemProps.containsKey(key)) {

            return systemProps.getProperty(key);
        } else if (testProps.containsKey(key)) {

            return testProps.getProperty(key);
        }

        return defaultProperty;
    }

    public static String getProperty(String key) {

        return getProperty(key, null);
    }

    public static void setProperty(String key, String value) {

        appProps.put(key, value);
    }

}
