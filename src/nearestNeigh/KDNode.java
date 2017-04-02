public class KDNode {

    // The point contained in this node
    public Point point = null;
    // THe point to the left of this node
    public Node left = null;
    // THe point to the right of this node
    public Node right = null;
    // The axis to split on at this node
    public Axis axis = null;

    // Constructor
    public KDNode(Point point, Axis axis) {
        this.point = point;
        this.axis = axis;
    }

}
