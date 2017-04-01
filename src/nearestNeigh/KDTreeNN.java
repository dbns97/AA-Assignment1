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
    private Point point = null;
    private KDTreeNN left = null;
    private KDTreeNN right = null;

    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
        points = sort(points);

        // Find hte median point
        int median = ceil(points.size() / 2);
        point = points.get(middle);

        // Build the left and right tree lists
        left = new KDTreeNN();
        right = new KDTreeNN();
        left.buildIndex(points.subList(0, median));
        right.buildIndex(points.subList(median + 1, points.size()));

    }

    /**
     * @description sort the list of points using bubble sort
     * @param points the list of points to sort
     * @return List<Point> the sorted list of points
     **/
    public List<Point> sort(List<Point> points) {

        // Sort the provided list
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size() - 1; j++) {
                if (points.get(j) > points.get(j+1)) {
                    Point temp = points.get(j);
                    points.set(j, points.get(j+1));
                    points.set(j+1, temp);
                }
            }
        }

        return points;
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

}
