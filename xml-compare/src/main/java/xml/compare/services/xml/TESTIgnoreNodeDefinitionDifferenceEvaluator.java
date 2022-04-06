package xml.compare.services.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

import java.util.Arrays;

public class TESTIgnoreNodeDefinitionDifferenceEvaluator implements DifferenceEvaluator {

    private final String[] ignoreNodeNames;
    private final String NODE_REGEX_OPEN_TAG = "<%s[^>]*>";
    private final String NODE_REGEX_CLOSE_TAG = "</%s[^>]*>";

    public TESTIgnoreNodeDefinitionDifferenceEvaluator(String[] ignoreNodeName) {
        this.ignoreNodeNames = Arrays.stream(ignoreNodeName)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult comparisonResult) {

        if (comparisonResult.equals(ComparisonResult.EQUAL) || comparisonResult.equals(ComparisonResult.SIMILAR)) {

            return comparisonResult;
        }

        final Node testNode = comparison.getTestDetails().getTarget();
        final Node controlNode = comparison.getControlDetails().getTarget();

        final Node testParentNode = (testNode == null) ? null : testNode.getParentNode();
        final Node controlParentNode = (controlNode == null) ? null : controlNode.getParentNode();

//        // If one of nodes is child of 'cite.query' (text) just skip
//        if ((testParentNode != null && Arrays.asList(ignoreNodeNames).contains(testParentNode.getNodeName()))
//                || controlParentNode != null && Arrays.asList(ignoreNodeNames).contains(controlParentNode.getNodeName())) {
//
//            return ComparisonResult.EQUAL;
//        }

        // If one of nodes - child of 'paratext' node, then just skip it
        if ((testParentNode != null && isNodeContainsNodeWithName(testParentNode, ignoreNodeNames))
                || (controlParentNode != null && isNodeContainsNodeWithName(controlParentNode, ignoreNodeNames))) {

            return ComparisonResult.EQUAL;
        }

        // Else let's compare their text content
        String testNodeContent = testNode.getTextContent();
        String controlNodeContent = controlNode.getTextContent();

        testNodeContent = removeNodeDefinitions(testNodeContent);
        controlNodeContent = removeNodeDefinitions(controlNodeContent);

        return compareNodesTextContent(testNodeContent, controlNodeContent);
    }

    private boolean isNodeContainsNodeWithName(Node node, String[] childNodeName) {

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String nodeName = nodeList.item(i).getNodeName().toLowerCase();
            if (Arrays.asList(childNodeName).contains(nodeName)) {

                return true;
            }
        }

        return false;
    }

    private String removeNodeDefinitions(String nodeContent) {
        for (String ignoreNodeName : ignoreNodeNames) {las
            String regexOpenTag = String.format(NODE_REGEX_OPEN_TAG, ignoreNodeName);
            String regexCloseTag = String.format(NODE_REGEX_CLOSE_TAG, ignoreNodeName);

            nodeContent = nodeContent.replaceAll(regexOpenTag, "");
            nodeContent = nodeContent.replace(regexCloseTag, "");
        }
        return nodeContent;
    }

    private ComparisonResult compareNodesTextContent(String testNodeContent, String controlNodeContent) {

        if (testNodeContent.equals(controlNodeContent)) {

            return ComparisonResult.EQUAL;
        } else {

            return ComparisonResult.DIFFERENT;
        }
    }

}
