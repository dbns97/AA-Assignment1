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

    // Axis to sort on
    private char axis;

    public void KDTreeNN() {
        this.axis = 'x';
    }

    public void KDTreeNN(char axis) {
        this.axis = axis;
    }

    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
        points = sort(points);

        // Find hte median point
        int median = ceil(points.size() / 2);
        point = points.get(middle);

        // Get the sort axis for the next layer down
        char nextAxis = (axis == 'x') ? 'y' : 'x';

        // Build the left tree
        List<Point> leftList = points.subList(0, median);
        if (leftList.size() > 0) {
            left = new KDTreeNN(nextAxis);
            left.buildIndex(leftList);
        }

        // Build the right tree
        List<Point> rightList = points.subList(median + 1, points.size());
        if (rightList.size() > 0) {
            right = new KDTreeNN(nextAxis);
            right.buildIndex(rightList);
        }

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
    private List<Point> sort(List<Point> points) {

        if (axis == 'x') {

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

        } else if (axis == 'y') {

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
