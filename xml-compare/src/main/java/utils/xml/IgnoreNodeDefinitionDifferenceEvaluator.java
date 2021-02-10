package utils.xml;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

@AllArgsConstructor
public class IgnoreNodeDefinitionDifferenceEvaluator implements DifferenceEvaluator {

    private final String ignoreNodeParentName;
    private final String ignoreNodeName;

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
        if ((testParentNode != null && testParentNode.getNodeName().equals(ignoreNodeParentName))
                || (controlParentNode != null && controlParentNode.getNodeName().equals(ignoreNodeParentName))) {

            return ComparisonResult.EQUAL;
        }

        // If both nodes is 'paratext' - change all child 'cite.query' nodes to theirs content and compare
        if (controlNode != null && controlNode.getNodeName().equals(ignoreNodeParentName)
                && testNode != null && testNode.getNodeName().equals(ignoreNodeParentName)) {
            Node newControlNode = removeIgnoredNodeDefinition(controlNode, ignoreNodeName);
            Node newTestNode = removeIgnoredNodeDefinition(testNode, ignoreNodeName);

            return compareNodesTextContent(newControlNode, newTestNode);
        }

        // Else just return Comparison Result
        return comparisonResult;
    }

    private ComparisonResult compareNodesTextContent(@NonNull Node controlNode, @NonNull Node testNode) {

        String testText = testNode.getTextContent();
        String controlText = controlNode.getTextContent();

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
