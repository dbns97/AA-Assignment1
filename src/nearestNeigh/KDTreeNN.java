package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

    // The root node of the tree
    private KDNode root = null;

    /**
     * @description build the tree form the supplied list of points
     * @param points the list of points to be in the tree
     * @return void
     **/
    @Override
    public void buildIndex(List<Point> points) {
        root = setNode(points, Axis.X, null);
    }

    /**
     * @description initialise a node in the tree (including creating sub-trees)
     * @param points the list of points for the tree
     * @param axis the axis to sort on at this node
     * @return KDNode the node that has been created
     **/
    private KDNode setNode(List<Point> points, Axis axis, KDNode parent) {
        // Sort the list of points on the supplied axis
        points = sort(points, axis);

        // Find the median point and initialise the node
        int median = (int) Math.floor(points.size() / 2);
        KDNode node = new KDNode(points.get(median), axis, null, null, parent);

        // Set axis for next layer of the tree
        Axis nextAxis = (axis == Axis.X) ? Axis.Y : Axis.X;

        // Get lists of points for two sub trees
        List<Point> leftPoints = points.subList(0, median);
        List<Point> rightPoints = points.subList(median + 1, points.size());

        // Set the left and right nodes
        if (leftPoints.size() > 0) node.setLeft(setNode(leftPoints, nextAxis));
        if (rightPoints.size() > 0) node.setRight(setNode(rightPoints, nextAxis));

        return node;
    }

    /**
     * @description search for the nearest points to a specific point
     * @param searchTerm the point from which to find the nearest points
     * @param k the number of points to return
     * @return List<Point> the list of nearest points in order of proximity
     **/
    @Override
    public List<Point> search(Point searchTerm, int k) {
        ArrayList<Point> nearestPoints = new ArrayList<Point>(k);

        nearestPoints.add(getNearestLeaf(searchTerm));

        

        return nearestPoints;
    }

    /**
     * @description add a new point to the tree
     * @param point the point to add
     * @return boolean whether or not the point was successfully added
     **/
    @Override
    public boolean addPoint(Point point) {
        KDNode currentNode = root;

        while (currentNode != null) {

            Point currentPoint = currentNode.getPoint();

            // Check if the new point is the same as the current point
            if (
            point.id.equals(currentPoint.id) &&
            point.cat == currentPoint.cat &&
            point.lat == currentPoint.lat &&
            point.lon == currentPoint.lon
            ) {
                System.out.println("Point not added. Point already exists.");
                return false;
            }

            // Find cutting axis and branch to go down
            boolean goLeft;
            if (currentNode.getAxis() == Axis.X) {
                goLeft = (point.lat <= currentPoint.lat);
            } else {
                goLeft = (point.lon <= currentPoint.lon);
            }

            if (goLeft) {
                // Determine if we are at a leaf node yet
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(point);
                    System.out.println("Adding point");
                    return true;
                } else {
                    currentNode = currentNode.getLeft();
                }
            } else {
                // Determine if we are at a leaf node yet
                if (currentNode.getRight() == null) {
                    currentNode.setRight(point);
                    System.out.println("Adding point");
                    return true;
                } else {
                    currentNode = currentNode.getRight();
                }
            }
        }
        return false;
    }

    /**
     * @description delete an existing point from the tree
     * @param point the point to be deleted
     * @return boolean whether or not the point was succcessfully deleted
     **/
    @Override
    public boolean deletePoint(Point point) {
        KDNode currentNode = root;
        KDNode parentNode = null;
        String direction = null;

        while (currentNode != null) {

            Point currentPoint = currentNode.getPoint();

            // Check if the new point is the same as the current point
            if (
            point.id.equals(currentPoint.id) &&
            point.cat == currentPoint.cat &&
            point.lat == currentPoint.lat &&
            point.lon == currentPoint.lon
            ) {
                if (currentNode.getLeft() == null && currentNode.getRight() == null) {
                    // Node is leaf. Delete parent's pointer to it
                    if (direction.equals("left")) {
                        parentNode.setLeft(null);
                    } else {
                        parentNode.setRight(null);
                    }
                } else {
                    // Node is not leaf. Must replace with inner leaf
                    KDNode replacement = getInnerLeaf(currentNode, direction);
                    if (direction.equals("left")) {
                        parentNode.setLeft(replacement);
                    } else {
                        parentNode.setRight(replacement);
                    }
                    replacement.setLeft(currentNode.getLeft());
                    replacement.setRight(currentNode.getRight());
                }
                return true;
            }

            // Find cutting axis and branch to go down
            boolean goLeft;
            if (currentNode.getAxis() == Axis.X) {
                goLeft = (point.lat <= currentPoint.lat);
            } else {
                goLeft = (point.lon <= currentPoint.lon);
            }

            if (goLeft) {
                // Determine if we are at a leaf node yet
                if (currentNode.getLeft() == null) {
                    System.out.println("Could not delete point, point does not exist.");
                    return false;
                } else {
                    direction = "left";
                    parentNode = currentNode;
                    currentNode = currentNode.getLeft();
                }
            } else {
                // Determine if we are at a leaf node yet
                if (currentNode.getRight() == null) {
                    System.out.println("Could not delete point, point does not exist.");
                    return false;
                } else {
                    direction = "right";
                    parentNode = currentNode;
                    currentNode = currentNode.getRight();
                }
            }
        }
        System.out.println("Could not delete point, point does not exist.");
        return false;
    }

    /**
     * @description check if a point exists in the tree
     * @param point the point to check for
     * @return boolean whether or not the point was found
     **/
    @Override
    public boolean isPointIn(Point point) {
        KDNode currentNode = root;

        while (currentNode != null) {

            Point currentPoint = currentNode.getPoint();

            // Check if the new point is the same as the current point
            if (
            point.id.equals(currentPoint.id) &&
            point.cat == currentPoint.cat &&
            point.lat == currentPoint.lat &&
            point.lon == currentPoint.lon
            ) {
                System.out.println("Found point in tree");
                return true;
            }

            // Find cutting axis and branch to go down
            boolean goLeft;
            if (currentNode.getAxis() == Axis.X) {
                goLeft = (point.lat <= currentPoint.lat);
            } else {
                goLeft = (point.lon <= currentPoint.lon);
            }

            if (goLeft) {
                // Determine if we are at a leaf node yet
                if (currentNode.getLeft() == null) {
                    System.out.println("Point not found in tree");
                    return false;
                } else {
                    currentNode = currentNode.getLeft();
                }
            } else {
                // Determine if we are at a leaf node yet
                if (currentNode.getRight() == null) {
                    System.out.println("Point not found in tree");
                    return false;
                } else {
                    currentNode = currentNode.getRight();
                }
            }
        }
        System.out.println("Point not found in tree");
        return false;
    }

    /* ---------- Private Methods ---------- */

    /**
     * @description sort the list of points using bubble sort
     * @param points the list of points to sort
     * @return List<Point> the sorted list of points
     **/
    private List<Point> sort(List<Point> points, Axis axis) {

        if (axis == Axis.X) {

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

        } else if (axis == Axis.Y) {

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

        /*
        Field dir = null;
        try {
            if (axis == Axis.X) {
                dir = Class.forName("nearestNeigh.Point").getField("lat");
            } else if (axis == Axis.Y) {
                dir = Class.forName("nearestNeigh.Point").getField("lon");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        // Sort the provided list by latitude
        for (int i = 0; i < points.size(); i++) {
            try {
                for (int j = 0; j < points.size() - 1; j++) {
                    if ((double) dir.get(points.get(j)) > (double) dir.get(points.get(j+1))) {
                        Point temp = points.get(j);
                        points.set(j, points.get(j+1));
                        points.set(j+1, temp);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        */

        return points;
    }

    /**
     * @description Find the nearest leaf (first step of search)
     * @param searchTerm the point to find nearest leaf to
     * @return KDNode The nearest leaf node
     **/
    private KDNode getNearestLeaf(Point searchTerm)
    {
        KDNode currentNode = root;

        while (currentNode != null) {

            Point currentPoint = currentNode.getPoint();

            // Find cutting axis and branch to go down
            boolean goLeft;
            if (currentNode.getAxis() == Axis.X) {
                goLeft = (searchTerm.lat <= currentPoint.lat);
            } else {
                goLeft = (searchTerm.lon <= currentPoint.lon);
            }

            if (goLeft) {
                // Determine if we are at a leaf node yet
                if (currentNode.getLeft() == null) {
                    return currentNode;
                } else {
                    currentNode = currentNode.getLeft();
                }
            } else {
                // Determine if we are at a leaf node yet
                if (currentNode.getRight() == null) {
                    return currentNode;
                } else {
                    currentNode = currentNode.getRight();
                }
            }
        }
    }

    /**
     * @description get one of the two inner-leafs from the tree where
     * the node provided is the root
     * @param root the root of the tree to get the leaf of
     * @return KDNode one of the two inner-leafs of the tree
     **/
    private KDNode getInnerLeaf(KDNode root, String direction) {
        String rootDirection;
        KDNode currentNode;
        KDNode parentNode = null;
        String parentDirection;

        if (root.getLeft() != null) {
            rootDirection = "left";
            currentNode = root.getLeft();
        } else if (root.getRight() != null) {
            rootDirection = "right";
            currentNode = root.getRight();
        } else {
            // Delete the pointer to currentNode from parentNode
            if (direction.equals("right")) {
                root.getParent().setRight(null);
            } else {
                root.getParent().setLeft(null);
            }
            return null;
        }

        // Keep going down the tree until we reach the leaf
        while (currentNode != null) {
            // If root direction was left, we always go right and vice-versa
            if (rootDirection.equals("left")) {
                // If currentNode has a right branch, go down it, else check left branch
                if (currentNode.getRight() != null) {
                    parentNode = currentNode;
                    parentDirection = "right";
                    currentNode = currentNode.getRight();
                } else {
                    // If currentNode has a left branch, go down it, else we are at the target leaf
                    if (currentNode.getLeft() != null) {
                        parentNode = currentNode;
                        parentDirection = "left";
                        currentNode = currentNode.getLeft();
                    } else {
                        // Delete the pointer to currentNode from parentNode
                        if (parentDirection.equals("right")) {
                            parentNode.setRight(null);
                        } else {
                            parentNode.setLeft(null);
                        }
                        return currentNode;
                    }
                }
            } else {
                // If currentNode has a left branch, go down it, else check right branch
                if (currentNode.getLeft() != null) {
                    parentNode = currentNode;
                    parentDirection = "left";
                    currentNode = currentNode.getLeft();
                } else {
                    // If currentNode has a right branch, go down it, else we are at the target leaf
                    if (currentNode.getRight() != null) {
                        parentNode = currentNode;
                        parentDirection = "right";
                        currentNode = currentNode.getRight();
                    } else {
                        // Delete the pointer to currentNode from parentNode
                        if (parentDirection.equals("right")) {
                            parentNode.setRight(null);
                        } else {
                            parentNode.setLeft(null);
                        }
                        return currentNode;
                    }
                }
            }
        }

    }

}
