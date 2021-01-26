package testdata;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public class TestData {

    private static final String TEST_PROPERTIES_FILE = "src\\test\\resources\\test.properties";

    @DataProvider(name = "getXmlFiles")
    public static Object[][] getXmlFiles() {

        Properties props = new Properties();

        try {
            props.load(new FileInputStream(TEST_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error("Error while getting file names from file: \"{}\"", TEST_PROPERTIES_FILE, e);
        }

        return new Object[][]{
                {props.getProperty("gold_data"), props.getProperty("output_data")}
        };
    }
}
