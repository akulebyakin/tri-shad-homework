package utils;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.transform.Source;
import java.io.File;
import java.util.Arrays;

@Log4j2
public class XmlUtils {

    /**
     * This method compares two XML files
     *
     * @param xmlControlFile File to compare with (Gold Data)
     * @param xmlTestFile    File to compare (Output Data)
     * @param ignoreNodes    XML node names to ignore while comparing. Can be empty.
     * @return <b>true</b> - if xmls are similar, <b>false</b> - if xmls have differences.
     */
    public static Boolean compareTwoXmlFiles(@NonNull File xmlControlFile, @NonNull File xmlTestFile,
                                             String... ignoreNodes) {

        log.info("Start comparing two XML files.");
        log.info("control file: {}", xmlControlFile);
        log.info("test file: {}", xmlTestFile);
        log.info("ignore nodes: {}", Arrays.toString(ignoreNodes));

        if (!xmlControlFile.exists()) log.error("File \"{}\" does not exist", xmlControlFile);
        if (!xmlTestFile.exists()) log.error("File \"{}\" does not exist", xmlTestFile);

        final Source control = Input.fromFile(xmlControlFile).build();
        final Source test = Input.fromFile(xmlTestFile).build();

        Diff diff = DiffBuilder.compare(control).withTest(test)
                .withNodeFilter(node -> !Arrays.asList(ignoreNodes).contains(node.getNodeName()))
                .withDifferenceListeners((comparison, comparisonResult) -> log.info("Found a difference: " + comparison))
                .checkForSimilar()
                .ignoreWhitespace()
                .ignoreComments()
                .build();

        boolean hasDifferences = diff.hasDifferences();
        if (hasDifferences) {
            log.info("Comparing finished. XMLs have differences.");
        } else {
            log.info("Comparing finished. No differences were found.");
        }

        return hasDifferences;
    }

}
