import java.util.Collections;
import java.util.Comparator;

public class UCT {
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWinScore / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.state.visitCount;
        Node bestNode = null;
        double maxValue = Double.MIN_VALUE;
        double value;
        for (Node child : node.children) {
            value = uctValue(parentVisit, child.state.winScore, child.state.visitCount);
            if (value > maxValue) {
                bestNode = child;
                maxValue = value;
            }
        }
        return bestNode;
    }
}