package utils.xml;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

@AllArgsConstructor
public class IgnoreNodeDefinitionDifferenceEvaluator implements DifferenceEvaluator {

    private final String nodeParent;
    private final String ignoreChildNodeDefinition;

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult comparisonResult) {
        if (comparisonResult.equals(ComparisonResult.EQUAL) || comparisonResult.equals(ComparisonResult.SIMILAR)) {
            return comparisonResult;
        }
        final Node testNode = comparison.getTestDetails().getTarget();
        final Node controlNode = comparison.getControlDetails().getTarget();

        if (controlNode != null && testNode == null && controlNode.getParentNode().getNodeName().equals(nodeParent)) {
            return ComparisonResult.EQUAL;
        }
        if (controlNode == null && testNode != null && testNode.getParentNode().getNodeName().equals(nodeParent)) {
            return ComparisonResult.EQUAL;
        }
        if (controlNode instanceof Element && testNode instanceof Element) {
            if (controlNode.getNodeName().equals(nodeParent)) {
                return compareNodesValues(controlNode, testNode);
            }

//            Element controlElement = (Element) controlNode.getParentNode();
//            Element testElement = (Element) testNode.getParentNode();
//            if (controlElement.getNodeName().equals(nodeParent)) {
//                final String controlValue = controlElement.getTextContent();
//                final String testValue = testElement.getTextContent();
//                if (new BigDecimal(controlValue).compareTo(new BigDecimal(testValue)) == 0) {
//                    return ComparisonResult.SIMILAR;
//                }
//            }
        } else if (controlNode != null && controlNode.getParentNode() instanceof Element
                && testNode != null && testNode.getParentNode() instanceof Element) {
            if (controlNode.getParentNode().getNodeName().equals(nodeParent)) {
                return compareNodesValues(controlNode.getParentNode(), testNode.getParentNode());
            }
        }
        return comparisonResult;
    }

    private ComparisonResult compareNodesValues(@NonNull Node controlNode, @NonNull Node testNode) {
        String testText = testNode.getTextContent().replaceAll("\\s", "");
        String controlText = controlNode.getTextContent().replaceAll("\\s", "");
        if (testText.equals(controlText)) {
            return ComparisonResult.EQUAL;
        } else {
            return ComparisonResult.DIFFERENT;
        }
    }

}
