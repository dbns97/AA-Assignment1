package nearestNeigh;

public class KDNode {

    // The point contained in this node
    private Point point = null;
    // THe point to the left of this node
    private KDNode left = null;
    // THe point to the right of this node
    private KDNode right = null;
    // The axis to split on at this node
    private Axis axis = null;

    // Constructor
    public KDNode(Point point, Axis axis, KDNode left, KDNode right) {
        this.point = point;
        this.axis = axis;
        this.left = left;
        this.right = right;
    }

    /*---------- Getters ----------*/

    public Point getPoint() {
        return point;
    }

    public KDNode getLeft() {
        return left;
    }

    public KDNode getRight() {
        return right;
    }

    public Axis getAxis() {
        return axis;
    }

    /*---------- Setters ----------*/

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setLeft(KDNode node) {
        this.left = node;
    }

    public void setLeft(Point point) {
        Axis axis = (this.axis == Axis.X) ? Axis.Y : Axis.X;
        KDNode node = new KDNode(point, axis, null, null);
        this.left = node;
    }

    public void setRight(Point point) {
        Axis axis = (this.axis == Axis.X) ? Axis.Y : Axis.X;
        KDNode node = new KDNode(point, axis, null, null);
        this.right = node;
    }

    public void setRight(KDNode node) {
        this.right = node;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

}
