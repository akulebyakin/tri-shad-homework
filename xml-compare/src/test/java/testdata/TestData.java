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

        Properties props = new Properties(System.getProperties());

        try {
            props.load(new FileInputStream(TEST_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error("Error while getting test properties from file: {}", TEST_PROPERTIES_FILE, e);
        }

        String goldData = props.getProperty("gold_data", null);
        String outputData = props.getProperty("output_data", null);
        String[] ignoreNodes = props.getProperty("ignore_nodes", "").split("([,;])");

        return new Object[][]{
                {goldData, outputData, ignoreNodes}
        };
    }

}
