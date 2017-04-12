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
        root = setNode(points, Axis.X, null, null);
    }

    /**
     * @description initialise a node in the tree (including creating sub-trees)
     * @param points the list of points for the tree
     * @param axis the axis to sort on at this node
     * @return KDNode the node that has been created
     **/
    private KDNode setNode(List<Point> points, Axis axis, KDNode parent, Direction direction) {
        // Sort the list of points on the supplied axis
        points = sort(points, axis);

        // Find the median point and initialise the node
        int median = (int) Math.floor(points.size() / 2);
        KDNode node = new KDNode(points.get(median), axis, null, null, parent, direction);

        // Set axis for next layer of the tree
        Axis nextAxis = (axis == Axis.X) ? Axis.Y : Axis.X;

        // Get lists of points for two sub trees
        List<Point> leftPoints = points.subList(0, median);
        List<Point> rightPoints = points.subList(median + 1, points.size());

        // Set the left and right nodes
        if (leftPoints.size() > 0) node.setLeft(setNode(leftPoints, nextAxis, node, Direction.LEFT));
        if (rightPoints.size() > 0) node.setRight(setNode(rightPoints, nextAxis, node, Direction.RIGHT));

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
        for (int i = 0; i < k; i++) {
            nearestPoints.add(null);
        }

        return findNearest(root, searchTerm, nearestPoints);

        /*
        // Main body of method
        //////////////////////
        KDNode leafNode = getNearestLeaf(searchTerm);
        double leafDist = getManhattanDist(searchTerm, leafNode.getPoint());
        nearestPoints.add(0, leafNode.getPoint());

        // TODO: This "loop" is just pseudo code because what is inside will need to be looped somehow
        loop {
            KDNode currentNode = leafNode.getParent();

            // Check if we need to check other branch
            if (nearestPoints.get(k - 1) == null || getAxisDist(searchTerm, currentNode) < getManhattanDist(searchTerm, nearestPoints.get(k - 1))) {
                if (leafNode.getBranch() == Branch.LEFT && currentNode.getRight() != null) {
                    // - search down the opposite branch until nearest leaf
                    // - then repeat the process that we've already done
                } else if (leafNode.getBranch() == Branch.RIGHT && currentNode.getLeft() != null){
                    // - search down the opposite branch until nearest leaf
                    // - then repeat the process that we've already done
                } else {
                    // - don't need to check other branch
                    // (if nearestPoints contains nulls then add currentNode. Maybe always check whether to add currentNode)
                }
            }
        }

        //////////////////////
        */
    }

    /**
     * @description Recursive method to find the nearest points at each point (where each point is the root of its own tree)
     * @param currentNode the starting node for this iteration
     * @param searchTerm the point that's nearest points are being searched for
     * @param nearestPoints the current state of the nearestPoints list
     * @return ArrayList<Point> the updated nearestPoints list
     **/
    private ArrayList<Point> findNearest(KDNode currentNode, Point searchTerm, ArrayList<Point> nearestPoints) {

        // Calculate the direction to go down
        Direction direction;
        if (currentNode.getAxis() == Axis.X) {
            direction = (searchTerm.lat <= currentNode.getPoint().lat) ? Direction.LEFT : Direction.RIGHT;
        } else {
            direction = (searchTerm.lon <= currentNode.getPoint().lon) ? Direction.LEFT : Direction.RIGHT;
        }

        // Store the next node and the other node based on the direction
        KDNode nextNode = (direction == Direction.LEFT) ? currentNode.getLeft() : currentNode.getRight();
        KDNode otherNode = (direction == Direction.LEFT) ? currentNode.getRight() : currentNode.getLeft();

        // Check if we are at a leaf node
        if (nextNode == null) {
            // If we are at a leaf node, add it to the nearestPoints list
            nearestPoints.set(0, currentNode);
        } else {
            // Recurse on this function for the next node
            nearestPoints = findNearest(nextNode, searchTerm, nearestPoints);
        }

        // Sort the existing list of nearest points (need to always know which is the furthest in this list)
        nearestPoints = sortNearest(searchTerm, nearestPoints);

        // Get the distance from the current point to the search point
        double currentPointDist = searchTerm.distTo(currentNode.getPoint());
        // Get the distance from the furthest point currently in nearestPoints to the search point
        double lastNearestPointDist = searchTerm.distTo(nearestPoints.get(nearestPoints.size() - 1));
        // If current point is closer, swap it into the nearestPoints list
        if (currentPointDist < lastNearestPointDist) {
            nearestPoints.set(nearestPoints.size() - 1, currentNode.getPoint());
        }

        // Check if other branch has closer points
        if (otherNode != null) {
            // Get the distance along the cutting axis from current point to search point
            double axisDist = getAxisDist(currentNode.getPoint(), searchTerm);
            // Get the distance from the furthest point currently in nearestPoints to the search point
            double lastNearestPointDist = searchTerm.distTo(nearestPoints.get(nearestPoints.size() - 1));
            // Compare the two distances to determine whether we need to check the other branch
            if (axisDist < lastNearestPointDist) {
                // Check other branch for closer points
                // TODO: not sure about this (do we just set nearestPoints to equal the return value from the other branch)
                nearestPoints = findNearest(otherNode, searchTerm, nearestPoints);
            }
        }

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
            if (point.equals(currentPoint)) {
                System.out.println("Point not added. Point already exists.");
                return false;
            }

            // Find cutting axis and branch to go down
            Direction direction;
            if (currentNode.getAxis() == Axis.X) {
                direction = (point.lat <= currentPoint.lat) ? Direction.LEFT : Direction.RIGHT;
            } else {
                direction = (point.lon <= currentPoint.lon) ? Direction.LEFT : Direction.RIGHT;
            }

            if (direction == Direction.LEFT) {
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
        // Get the node to delete
        nodeToDelete = getNode(point);
        if (nodeToDelete == null) return false;

        // Get the replacement node
        KDNode replacement = getInnerLeaf(nodeToDelete);

        if (replacement == null) {
            // If the point being deleted is a leaf, just delete the parent's pointer to the point being deleted
            if (nodeToDelete.getParent() != null) {
                if (nodeToDelete.getDirection() == Direction.LEFT) {
                    nodeToDelete.getParent().setLeft(null);
                } else {
                    nodeToDelete.getParent().setRight(null);
                }
            }
        } else {
            // Replace currentNode's point with replacement's point
            nodeToDelete.setPoint(replacement.getPoint());

            // Remove the replacement node from the leaf
            if (replacement.getDirection() == Direction.LEFT) {
                replacement.getParent().setLeft(null);
            } else {
                replacement.getParent().setRight(null);
            }
        }

        return true;
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
            if (point.equals(currentPoint)) {
                System.out.println("Found point in tree");
                return true;
            }

            // Find cutting axis and branch to go down
            Direction direction;
            if (currentNode.getAxis() == Axis.X) {
                direction = (point.lat <= currentPoint.lat) ? Direction.LEFT : Direction.RIGHT;
            } else {
                direction = (point.lon <= currentPoint.lon) ? Direction.LEFT : Direction.RIGHT;
            }

            currentNode = (direction == Direction.LEFT) ? currentNode.getLeft() : currentNode.getRight();

            if (currentNode == null) {
                System.out.println("Point not found in tree");
                return false;
            }

        }
        System.out.println("Point not found in tree");
        return false;
    }

    /* ---------- Private Methods ---------- */

    /**
     * @description returns a node with a specific point
     * @param point the point to find
     * @return KDNode the node containing the point
     **/
    private KDNode getNode(Point point) {
        KDNode currentNode = root;

        while (currentNode != null) {

            Point currentPoint = currentNode.getPoint();

            // Check if the new point is the same as the current point
            if (point.equals(currentPoint)) {
                return currentPoint;
            }

            // Find cutting axis and branch to go down
            Direction direction;
            if (currentNode.getAxis() == Axis.X) {
                direction = (point.lat <= currentPoint.lat) ? Direction.LEFT : Direction.RIGHT;
            } else {
                direction = (point.lon <= currentPoint.lon) ? Direction.LEFT : Direction.RIGHT;
            }

            currentNode = (direction == Direction.LEFT) ? currentNode.getLeft() : currentNode.getRight();

            if (currentNode == null) {
                return null;
            }

        }
        return null;
    }

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

        return points;
    }

    private ArrayList<Point> sortNearest(Point searchTerm, ArrayList<Point> points) {
        // Sort the provided list by distance from searchTerm
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size() - 1; j++) {
                // Calculate distances of the current point and the next point
                double pointDist = getDist(searchTerm, points.get(j));
                double nextPointDist = getDist(searchTerm, points.get(j+1));
                if (pointDist > nextPointDist) {
                    Point temp = points.get(j);
                    points.set(j, points.get(j+1));
                    points.set(j+1, temp);
                }
            }
        }
        return points;
    }

    private double getAxisDist(Point p, KDNode n) {
        if (n.getAxis() == Axis.X) {
            return Math.abs(p.lat - n.getPoint().lat);
        } else {
            return Math.abs(p.lon - n.getPoint().lon);
        }
    }

    /**
     * @description get an inner-leaf of the tree
     * @param root the root of the tree
     * @return KDNode teh leaf node
     **/
    private KDNode getInnerLeaf(KDNode root) {
        if (root.getLeft() != null) {
            return getOuterLeaf(root.getLeft(), Direction.RIGHT);
        } else if (root.getRight() != null) {
            return getOuterLeaf(root.getRight(), Direction.LEFT);
        } else {
            return null;
        }
    }

    /**
     * @description get the right-most leaf of the current tree
     * @param root the root of the tree
     * @param direction which outer leaf to get
     * @return KDNode the leaf node
     **/
    private KDNode getOuterLeaf(KDNode root, Direction direction) {

        // Check if we are at leaf
        if (root.getLeft() == null && root.getRight() == null) {
            return root;
        }

        // Determine which branch to go down
        if (direction == Direction.LEFT) {
            return (root.getLeft() != null) ? getOuterLeaf(root.getLeft(), direction) : getOuterLeaf(root.getRight(), direction);
        } else {
            return (root.getRight() != null) ? getOuterLeaf(root.getRight(), direction) : getOuterLeaf(root.getLeft(), direction);
        }
    }

}
