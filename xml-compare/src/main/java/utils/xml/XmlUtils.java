package utils.xml;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class XmlUtils {

    private static final Logger errorFileLog = LogManager.getLogger("Error File Log");

    /**
     * This method compares two XML files
     *
     * @param xmlControlFile File to compare with (Gold Data)
     * @param xmlTestFile    File to compare (Output Data)
     * @param ignoreNodes    XML node names to ignore during comparing. May be empty.
     * @return <b>true</b> - if xmls are similar, <b>false</b> - if xmls have differences.
     */
    public static Boolean compareTwoXmlFilesWithIgnoreNodes(@NonNull File xmlControlFile, @NonNull File xmlTestFile,
                                                            String... ignoreNodes) {

        log.info("Start comparing two XML files.");
        log.info("control file: {}", xmlControlFile);
        log.info("test file: {}", xmlTestFile);
        log.info("ignore nodes: {}", Arrays.toString(ignoreNodes));

        final Source control = getSourceFromFile(xmlControlFile);
        final Source test = getSourceFromFile(xmlTestFile);

        boolean hasDifferences = compareTwoXmls(control, test, ignoreNodes);
        if (hasDifferences) {
            log.info("Comparing finished. XMLs have differences.");
        } else {
            log.info("Comparing finished. No differences were found.");
        }

        return hasDifferences;
    }

    public static Boolean compareTwoXmlFilesWithIgnoreNodesDefinitions(@NonNull String xmlControlFilename,
                                                                       @NonNull String xmlTestFilename,
                                                                       String... ignoreNodeDefinitions)
            throws IOException, SAXException, ParserConfigurationException {

        log.info("Start comparing two XML files.");
        log.info("control file: {}", xmlControlFilename);
        log.info("test file: {}", xmlTestFilename);
        log.info("ignore nodes definitions: {}", Arrays.toString(ignoreNodeDefinitions));

        Document controlDocument = removeIgnoredNodeDefinition(getDocumentFromFile(xmlControlFilename), ignoreNodeDefinitions);
        Document testDocument = removeIgnoredNodeDefinition(getDocumentFromFile(xmlTestFilename), ignoreNodeDefinitions);

        final Source control = Input.fromDocument(controlDocument).build();
        final Source test = Input.fromDocument(testDocument).build();

//        final Source control = getSourceFromFile(new File(xmlControlFilename));
//        final Source test = getSourceFromFile(new File(xmlTestFilename));

        return compareTwoXmls(control, test);
    }

    private static Boolean compareTwoXmls(@NonNull Source control, @NonNull Source test, String... ignoreNodes) {

        Diff diff = DiffBuilder.compare(control).withTest(test)
                .withNodeFilter(node -> !Arrays.asList(ignoreNodes).contains(node.getNodeName()))
                .withDifferenceListeners((comparison, comparisonResult) -> log.info("Found a difference: " + comparison))
                .checkForIdentical()
                .ignoreWhitespace()
                .ignoreComments()
                .build();

        return diff.hasDifferences();
    }

    private static Source getSourceFromFile(@NonNull File file) {
        if (!file.exists()) log.error("File '{}' does not exist", file);

        return Input.fromFile(file).build();
    }

    private static Document getDocumentFromFile(@NonNull String filename) throws ParserConfigurationException, SAXException,
            IOException {

//        File file = new File(filename);
//
//        if (!file.exists()) log.error("File '{}' does not exist", filename);

        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

//            return factory.newDocumentBuilder().parse(file);
            return factory.newDocumentBuilder().parse(in);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error("Error while getting XML Document from file '{}'", filename, e);
            throw e;
        }
    }

    private static Document removeIgnoredNodeDefinition(@NonNull Document document, String... ignoreNodesDefinitions) {

        for (String ignoreNodeDefinition : ignoreNodesDefinitions) {
            NodeList nodeList = document.getElementsByTagName(ignoreNodeDefinition);
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println("parsing node from nodelist: " + i);
                Node ignoreNode = nodeList.item(0);
                Node ignoreNodeValue = ignoreNode.getFirstChild();
                if (ignoreNode.getParentNode() != null) {
                    if (ignoreNodeValue == null || ignoreNodeValue.getTextContent().isEmpty()) {
                        ignoreNode.getParentNode().removeChild(ignoreNode);
                    } else {
                        ignoreNode.getParentNode().replaceChild(ignoreNodeValue, ignoreNode);
                    }
                }
            }
        }

        return document;
    }

}
