public class KDNode {

    // The point contained in this node
    private Point point = null;
    // THe point to the left of this node
    private Node left = null;
    // THe point to the right of this node
    private Node right = null;
    // The axis to split on at this node
    private Axis axis = null;

    // Constructor
    public KDNode(Point point, Axis axis) {
        this.point = point;
        this.axis = axis;
    }

    /*---------- Getters ----------*/

    public Point getPoint() {
        return point;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Axis getAxis() {
        return axis;
    }

    /*---------- Setters ----------*/

    public Point setPoint(Point point) {
        this.point = point;
    }

    public Node setLeft(Node node) {
        this.left = node;
    }

    public Node setRight(Node node) {
        this.right = node;
    }

    public Axis setAxis(Axis axis) {
        this.axis = axis;
    }

}
