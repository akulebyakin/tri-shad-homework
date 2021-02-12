package services.xml;

import lombok.NonNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

import java.util.Arrays;

public class IgnoreNodeDefinitionDifferenceEvaluator implements DifferenceEvaluator {

    private final String[] ignoreNodeName;

    public IgnoreNodeDefinitionDifferenceEvaluator(String[] ignoreNodeName) {
        this.ignoreNodeName = Arrays.stream(ignoreNodeName)
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

        // If one of nodes - child of 'paratext' node, then just skip it
        if ((testParentNode != null && isNodeContainsNodeWithName(testParentNode, ignoreNodeName))
                || (controlParentNode != null && isNodeContainsNodeWithName(controlParentNode, ignoreNodeName))) {

            return ComparisonResult.EQUAL;
        }

        // If both nodes is 'paratext' - change all child 'cite.query' nodes to theirs content and compare
        if (controlNode != null && isNodeContainsNodeWithName(controlNode, ignoreNodeName)
                || testNode != null && isNodeContainsNodeWithName(testNode, ignoreNodeName)) {
            Node newControlNode = removeIgnoredNodeDefinition(controlNode, ignoreNodeName);
            Node newTestNode = removeIgnoredNodeDefinition(testNode, ignoreNodeName);

            return compareNodesTextContent(newControlNode, newTestNode);
        }

        // Else just return Comparison Result
        return comparisonResult;
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

    private ComparisonResult compareNodesTextContent(@NonNull Node controlNode, @NonNull Node testNode) {

        String testText = testNode.getTextContent().replaceAll("\\s", "");
        String controlText = controlNode.getTextContent().replaceAll("\\s", "");

        if (testText.equals(controlText)) {

            return ComparisonResult.EQUAL;
        } else {

            return ComparisonResult.DIFFERENT;
        }
    }

    private Node removeIgnoredNodeDefinition(Node parent, String... ignoreNodesDefinitions) {

        for (String ignoreNodeDefinition : ignoreNodesDefinitions) {
            NodeList nodeList = parent.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node ignoreNode = nodeList.item(0);
                if (ignoreNode.getNodeName().equals(ignoreNodeDefinition)) {
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
        }

        return parent;
    }

}
