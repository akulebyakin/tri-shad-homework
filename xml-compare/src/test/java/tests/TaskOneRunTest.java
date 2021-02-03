package tests;

import listeners.TestNGLogListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testdata.TestData;
import utils.XmlUtils;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@Listeners(TestNGLogListener.class)
public class TaskOneRunTest {

    @Test(testName = "Compare XMLs",
            dataProviderClass = TestData.class,
            dataProvider = "getXmlFiles")
    public void testCompareXmls(@NonNull final String goldDataFileName, @NonNull final String outputDataFileName,
                                String[] ignoreNodes) {

        Boolean hasDifferences = XmlUtils.compareTwoXmlFiles(
                new File(goldDataFileName),
                new File(outputDataFileName),
                ignoreNodes);
        assertThat(hasDifferences)
                .describedAs("Check that these XML files have no differences: %s, %s",
                        goldDataFileName, outputDataFileName)
                .isFalse();
    }

}
