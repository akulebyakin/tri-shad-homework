package utils;

import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class AppProperties {

    private static final String TEST_PROPERTIES_FILE;

    private static final Properties testProps;
    private static final Properties systemProps;
    private static final Map<String, String> envProperties;

    private static final Map<String, String> appProps;

    static {
        log.info("Setting up properties");

        TEST_PROPERTIES_FILE = System.getProperty("TEST_PROPERTIES_FILE",
                "src\\test\\resources\\test.properties");

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
        log.info("Get property '{}' with default value '{}'", key, defaultProperty);
        if (appProps.containsKey(key)) {
            log.info("Returning property from Application Properties");
            return appProps.get(key);
        } else if (envProperties.containsKey(key)) {
            log.info("Returning property from Environment Variables");
            return envProperties.get(key);
        } else if (systemProps.containsKey(key)) {
            log.info("Returning property from System Properties");
            return systemProps.getProperty(key);
        } else if (testProps.containsKey(key)) {
            log.info("Returning property from properties file '{}'", TEST_PROPERTIES_FILE);
            return testProps.getProperty(key);
        }

        log.info("No property '{}' were found. Returning default value.", key);
        return defaultProperty;
    }

    public static String getProperty(String key) {

        return getProperty(key, null);
    }

    public static void setProperty(String key, String value) {
        log.info("Setting property '{}' to Application Properties", key);
        appProps.put(key, value);
    }

}
