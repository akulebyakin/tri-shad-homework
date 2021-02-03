package tests;

import listeners.TestNGLogListener;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testdata.TestData;
import utils.AppProperties;
import utils.ZipUtils;

import java.io.File;

@Log4j2
@Listeners(TestNGLogListener.class)
public class TaskTwoRunTest {

    @BeforeTest
    public void setUp() {
        File zipFile = new File(AppProperties.getProperty("zip_data_file"));
        String goldData = AppProperties.getProperty("gold_data_folder");
        String outputDataFolder = AppProperties.getProperty("output_data_folder");
        String goldDataRegex = AppProperties.getProperty("gold_data_regex");
        String outputDataRegex = AppProperties.getProperty("output_data_regex");

        ZipUtils.extractGoldDataAndOutputData(zipFile, goldData, outputDataFolder, goldDataRegex, outputDataRegex);
    }

    @Test(testName = "Compare Big XMLs",
            dataProviderClass = TestData.class,
            dataProvider = "getXmlFilesFromGoldDataAndOutputDataFolders")
    public void test() {

    }

//    @Test(testName = "Compare XMLs",
//            dataProviderClass = TestData.class,
//            dataProvider = "getXmlFiles")
//    public void testCompareXmls(@NonNull final String goldDataFileName, @NonNull final String outputDataFileName,
//                                String[] ignoreNodes) {
//
//        Boolean hasDifferences = XmlUtils.compareTwoXmlFiles(
//                new File(goldDataFileName),
//                new File(outputDataFileName),
//                ignoreNodes);
//        assertThat(hasDifferences)
//                .describedAs("Check that these XML files have no differences: %s, %s",
//                        goldDataFileName, outputDataFileName)
//                .isFalse();
//    }

}
