package tests;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import testdata.TestData;
import xml.compare.exceptions.TestDataException;
import xml.compare.services.xml.XmlCompare;
import xml.compare.services.zip.TestDataZipExtractor;
import xml.compare.utils.AppProperties;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class TaskTwoRunTest extends AbstractTest {

    private static final String NO_GOLD_DATA_PAIR_ERR_MSG = "Gold data file '%s' has no output data pair";
    private static final String NO_OUTPUT_DATA_PAIR_ERR_MSG = "Output data file '%s' has no gold data pair";

    @Autowired
    @Lazy
    private XmlCompare xmlCompare;

    @Autowired
    @Lazy
    private TestDataZipExtractor testDataZipExtractor;

    @BeforeSuite()
    public void setUp() {
        log.info("Setting up test suite: unzip data file archive");
        String zipDataFile = AppProperties.getProperty("task_two_zip_data_file", null);

        if (zipDataFile == null) {
            log.info("The property 'zip_data_file' is not set, so data file unzipping was skipped");
            return;
        }

        File zipFile = new File(zipDataFile);
        String goldData = AppProperties.getProperty("task_two_gold_data_folder");
        String outputDataFolder = AppProperties.getProperty("task_two_output_data_folder");
        String goldDataRegex = "A\\d*.xml";
        String outputDataRegex = "B\\d*.xml";

        testDataZipExtractor.extractGoldDataAndOutputData(zipFile, goldData, outputDataFolder, goldDataRegex, outputDataRegex);
    }

    @Test(testName = "Compare Big XMLs",
            dataProviderClass = TestData.class,
            dataProvider = "getXmlFilesFromGoldDataAndOutputDataFolders")
    public void testCompareBigXmls(final String goldDataFileName,
                                   final String outputDataFileName,
                                   String[] ignoreNodes) throws TestDataException {

        if (goldDataFileName != null && outputDataFileName == null) {
            String errorMessage = String.format(NO_GOLD_DATA_PAIR_ERR_MSG, goldDataFileName);
            throw new TestDataException(errorMessage);
        } else if (goldDataFileName == null && outputDataFileName != null) {
            String errorMessage = String.format(NO_OUTPUT_DATA_PAIR_ERR_MSG, outputDataFileName);
            throw new TestDataException(errorMessage);
        }

        Boolean hasDifferences = xmlCompare.compareTwoXmlFilesIgnoreNodesDefinitions(
                goldDataFileName,
                outputDataFileName,
                ignoreNodes);

        assertThat(hasDifferences)
                .describedAs("Check that these XML files have no differences: %s, %s",
                        goldDataFileName, outputDataFileName)
                .isFalse();
    }

}
