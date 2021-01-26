import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;
import testdata.TestData;

import javax.xml.transform.Source;

@Log4j2
public class RunTest {

    @Test(dataProviderClass = TestData.class,
            dataProvider = "getXmlFiles")
    public void test(@NonNull final String goldData, @NonNull final String outputData) {

        Source control = Input.fromFile(goldData).build();
        Source test = Input.fromFile(outputData).build();

        DifferenceEngine diff = new DOMDifferenceEngine();
        diff.addDifferenceListener((comparison, outcome) -> log.info("found a difference: " + comparison));
        diff.compare(control, test);

        XmlAssert.assertThat(test)
                .and(control)
                .areIdentical();
    }

}
