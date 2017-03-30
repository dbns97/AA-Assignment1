public class KDNode {

    // The point contained in this node
    public Point point = null;
    // THe point to the left of this node
    public Node left = null;
    // THe point to the right of this node
    public Node right = null;

    // Constructor
    public KDNode(Point point) {
        this.point = point;
    }

}
