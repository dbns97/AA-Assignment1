package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

    // The root node of the tree
    private KDNode root = null;

    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.

        root = setNode(points, X);

    }

    private void setNode(List<Point> points, Axis axis) {
        // Sort the list of points on the supplied axis
        points = sort(points, axis);

        // Find the median point and initialise the node
        int median = floor(points.size() / 2);
        KDNode node = new KDNode(points.get(median), axis);

        // Set axis for next layer of the tree
        Axis nextAxis = (axis == X) ? Y : X;

        // Get lists of points for two sub trees
        List<Point> leftPoints = points.subList(0, median);
        List<Point> rightPoints = points.subList(median + 1, points.size());

        // Set the left and right nodes
        node.setLeft(setNode(leftPoints, nextAxis));
        node.setRight(setNode(rightPoints, nextAxis));

        return node;
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        return new ArrayList<Point>();
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean isPointIn(Point point) {
        // To be implemented.
        return false;
    }

    /* ---------- Private Methods ---------- */

    /**
     * @description sort the list of points using bubble sort
     * @param points the list of points to sort
     * @return List<Point> the sorted list of points
     **/
    private List<Point> sort(List<Point> points, Axis axis) {

        if (axis == X) {

            // Sort the provided list by latitude
            for (int i = 0; i < points.size(); i++) {
                for (int j = 0; j < points.size() - 1; j++) {
                    if (points.get(j).lat > points.get(j+1).lat) {
                        Point temp = points.get(j);
                        points.set(j, points.get(j+1));
                        points.set(j+1, temp);
                    }
                }
            }

        } else if (axis == Y) {

            // Sort the provided list by longitude
            for (int i = 0; i < points.size(); i++) {
                for (int j = 0; j < points.size() - 1; j++) {
                    if (points.get(j).lon > points.get(j+1).lon) {
                        Point temp = points.get(j);
                        points.set(j, points.get(j+1));
                        points.set(j+1, temp);
                    }
                }
            }

        }

        return points;
    }

}
