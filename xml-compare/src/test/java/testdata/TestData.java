package testdata;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import utils.AppProperties;

@Log4j2
public class TestData {

    @DataProvider(name = "getXmlFiles")
    public static Object[][] getXmlFiles() {

        String goldData = AppProperties.getProperty("gold_data", null);
        String outputData = AppProperties.getProperty("output_data", null);
        String[] ignoreNodes = AppProperties.getProperty("ignore_nodes", "").split("([,;])");

        return new Object[][]{
                {goldData, outputData, ignoreNodes}
        };
    }

}
