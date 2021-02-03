package tests;

import listeners.TestNGLogListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testdata.TestData;
import utils.AppProperties;
import utils.ZipUtils;

import java.io.File;
import java.util.Arrays;

@Log4j2
@Listeners(TestNGLogListener.class)
public class TaskTwoRunTest {

    @BeforeTest
    public void setUp() {
        File zipFile = new File(AppProperties.getProperty("zip_data_file"));
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
                     String[] ignoreNodes) {
        log.info(goldDataFileName);
        log.info(outputDataFileName);
        log.info(Arrays.toString(ignoreNodes));
    }

}
