package tests;

import listeners.TestNGLogListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import testdata.TestData;
import utils.AppProperties;
import utils.ZipUtils;
import utils.xml.XmlUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@Listeners(TestNGLogListener.class)
public class TaskTwoRunTest {

    @BeforeSuite
    public void setUp() {

        log.info("Setting up test suite: unzip data file archive");
        String zipDataFile = AppProperties.getProperty("zip_data_file", null);

        if (zipDataFile == null) {
            log.info("The property 'zip_data_file' is not set, so data file unzipping was skipped");
            return;
        }

        File zipFile = new File(zipDataFile);
        String goldData = AppProperties.getProperty("gold_data_folder");
        String outputDataFolder = AppProperties.getProperty("output_data_folder");
        String goldDataRegex = "A\\d?+.xml";
        String outputDataRegex = "B\\d?+.xml";

        ZipUtils.extractGoldDataAndOutputData(zipFile, goldData, outputDataFolder, goldDataRegex, outputDataRegex);
    }

    @Test(testName = "Compare Big XMLs",
            dataProviderClass = TestData.class,
            dataProvider = "getXmlFilesFromGoldDataAndOutputDataFolders")
    public void test(@NonNull final String goldDataFileName, @NonNull final String outputDataFileName,
                     String[] ignoreNodes) throws ParserConfigurationException, SAXException, IOException {

        Boolean hasDifferences = XmlUtils.compareTwoXmlFilesWithIgnoreNodesDefinitions(
                goldDataFileName,
                outputDataFileName,
                ignoreNodes);

        assertThat(hasDifferences)
                .describedAs("Check that these XML files have no differences: %s, %s",
                        goldDataFileName, outputDataFileName)
                .isFalse();
    }

}
