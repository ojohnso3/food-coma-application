package edu.brown.cs.student.kdtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * KDTree class, stores a root and dimension of coordinates, tree uses Node nodes.
 * @param <N> a node type that implements the Node interface included in package
 */
public class KDTree<N extends KDNode<N>> {
  private N root;
  private final int dim;
  private static final double EPSILON = 1e-6;

  /**
   * Constructor.
   *
   * @param dim - dimension of nodes
   * @throws KDTreeException when the dimension is invalid
   */
  public KDTree(int dim) throws KDTreeException {
    if (dim < 1) {
      throw new KDTreeException("dim must be a positive integer");
    } else {
      this.dim = dim;
    }
  }

  /**
   * dim getter.
   *
   * @return dim
   */
  public int getDim() {
    return dim;
  }

  /**
   * root getter.
   *
   * @return root
   */
  public N getRoot() {
    return root;
  }

  /**
   * prints a String representation of the whole tree.
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    LinkedList<N> toVisit = new LinkedList<>();
    // add the root if possible
    if (root != null) {
      toVisit.add(this.root);
    }
    // iterate through the set, continuously calling toString and adding the children until empty
    boolean first = true;
    while (!toVisit.isEmpty()) {
      N node = toVisit.pop();
      // call the node's to string method
      if (first) {
        str.append(node.toString());
        first = false;
      } else {
        str.append("\n").append(node.toString());
      }
      // add the child nodes
      if (node.getLeftChild() != null) {
        toVisit.add(node.getLeftChild());
      }
      if (node.getRightChild() != null) {
        toVisit.add(node.getRightChild());
      }
    }

    return str.toString();
  }

  /**
   * gives a list of all the nodes.
   * @return all nodes in the tree
   */
  public List<N> getNodes() {
    List<N> nodes = new ArrayList<>();
    if (this.root != null) {
      nodes.add(this.root);
    }
    assert (root != null);
    getNodesRec(this.root.getLeftChild(), nodes);
    getNodesRec(this.root.getRightChild(), nodes);
    return nodes;
  }
  /*
   * recursive component.
   */
  private void getNodesRec(N curr, List<N> nodes) {
    if (curr == null) {
      return;
    }
    nodes.add(curr);
    getNodesRec(curr.getLeftChild(), nodes);
    getNodesRec(curr.getRightChild(), nodes);
  }

  /**
   * Calculates Euclidean distance between two nodes.
   *
   * @param node1 - node
   * @param node2 - node to compare
   * @return - a Double for the distance btw nodes
   */
  public Double distance(N node1, N node2) {
    double sum = 0.;
    // get each list of coords only once
    List<Double> ls1 = node1.getCoords();
    List<Double> ls2 = node2.getCoords();
    // each (x_2 - x_1)^2
    int sz = ls1.size();
    for (int i = 0; i < sz; i++) {
      Double t = ls2.get(i) - ls1.get(i);
      sum += t * t; // Math.pow less efficient
    }
    return Math.sqrt(sum);
  }

  /**
   * generate tree from a list of nodes. Sets the root then calls a private recursive function
   * which does the rest. If called twice, it ends with a new tree of just the second list of nodes;
   * it does not add to existing tree from the first list. A null or empty list does not initialize.
   *
   * @param nodes - list of nodes composing the tree
   */
  public void initializeTree(List<N> nodes) {
    // if list is null or empty, abort
    if (nodes == null) {
      return;
    }
    int size = nodes.size();
    if (size == 0) {
      return;
    }
    // sort along first coordinate axis to get the median
    nodes.sort(new AxisComparator<>(0));
    int medIndex = size / 2;
    this.root = nodes.get(medIndex);
    // recur on children
    this.root.setLeftChild(initializeTreeRec(
            nodes.subList(0, medIndex), 1 % this.dim));
    this.root.setRightChild(initializeTreeRec(
            nodes.subList(medIndex + 1, nodes.size()), 1 % this.dim));
  }
  /*
   * Recursive generator. Finds median, recursively sets its children, then returns it.
   */
  private N initializeTreeRec(List<N> nodes, int depth) {
    // once subtree is empty, set children to null
    int size = nodes.size();
    if (size == 0) {
      return null;
    }
    // sort and get median
    nodes.sort(new AxisComparator<>(0));
    int medIndex = size / 2;
    N median = nodes.get(medIndex);
    // set children
    depth = (depth + 1) % this.dim;
    median.setLeftChild(initializeTreeRec(nodes.subList(0, medIndex), depth));
    median.setRightChild(initializeTreeRec(nodes.subList(medIndex + 1, size), depth));
    // return median as child of the parent that called this function
    return median;
  }

  /**
   * insert a node at the bottom of tree. Does not rebalance.
   *
   * @param insert - node to insert
   */
  public void insert(N insert) {
    if (this.root == null) {
      this.root = insert;
    } else {
      insertRec(insert, this.root, 0);
    }
  }
  /*
   * Recursively inserts a node into current tree.
   *
   * @param insert - node to insert
   * @param curr   - current node recurring on
   * @param axis   - coordinate axis
   */
  private void insertRec(N insert, N curr, int axis) {
    // if there is no child, insert is the new child; else, recur on the appropriate child
    N next;
    AxisComparator<N> dc = new AxisComparator<>(axis);
    if (dc.compare(insert, curr) <= 0) {
      next = curr.getLeftChild();
      if (next == null) {
        curr.setLeftChild(insert);
      } else {
        insertRec(insert, next, (axis + 1) % this.dim);
      }
    } else {
      next = curr.getRightChild();
      if (next == null) {
        curr.setRightChild(insert);
      } else {
        insertRec(insert, next, (axis + 1) % this.dim);
      }
    }
  }

  /**
   * Finds the k-nearest neighbors to a target node.
   *
   * @param target target node to be close to
   * @param k      num of neighbors to find
   * @return list of closest node
   */
  public List<N> nearestSearch(N target, int k) throws KDTreeException {
    // error cases: empty tree or returning negative stars
    if (this.root == null) {
      throw new KDTreeException("ERROR: tree must be initialized");
    } else if (k < 0) {
      throw new KDTreeException("ERROR: must search for a non-negative amount of neighbors");
    } else if (k == 0) {
      return new LinkedList<>();
    }
    // run recursive nearest neighbors fun
    PriorityQueue<N> nearbyStars = new PriorityQueue<>(new EuclideanComparator<>(target));
    nearestSearchRec(target, k, this.root, nearbyStars, 0);
    // convert pq to list, reversing order so that least distance is first
    LinkedList<N> ls = new LinkedList<>();
    while (!nearbyStars.isEmpty()) {
      ls.addFirst(nearbyStars.poll());
    }
    return ls;
  }
  /*
   * recursive part of finding the k-nearest neighbors
   */
  private void nearestSearchRec(N target, int k, N curr, PriorityQueue<N> nearbyStars, int axis) {
    // End/Base Case: if curr is null after leaf, end.
    if (curr == null) {
      return;
    }

    // Deal with current node: adding or not.
    EuclideanComparator<N> dc = new EuclideanComparator<>(target);
    // if neighbors is not full or curr is closer than something in it, add (and remove the far)
    if (curr.equals(target)) { // if it's the target, don't add it into neighbors; just move on
      nearestSearchRec(target, k, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      nearestSearchRec(target, k, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
      return;
    } else if (nearbyStars.size() < k) { // not full && !nearbyStars.contains(curr)
      nearbyStars.add(curr);
    } else {
      N farthest = nearbyStars.peek(); // peek O(log)
      assert farthest != null;
      if (dc.compare(curr, farthest) >= 0) { //closer && !nearbyStars.contains(curr)
        nearbyStars.remove(); // remove old/far O(log)
        nearbyStars.add(curr); // replace w/ new, close
      }
    }

    // Deal with which subtrees need be searched.
    // if dist btw target and farthest so far is >= than axis dist of curr,
    // must check both the the subtree that's closer on the axis and the far one
    Double currCoord = curr.getCoords().get(axis);
    Double targetCoord = target.getCoords().get(axis);
    N skippedTree = null;
    N newFarthest = nearbyStars.peek();
    assert newFarthest != null;
    if (distance(target, newFarthest) >= Math.abs(currCoord - targetCoord)) {
      nearestSearchRec(target, k, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      nearestSearchRec(target, k, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
    } else {
      // if the axis dist is already farther, then subtree will also be farther from just axis dist
      // so we only check the subtree that's closer on that axis
      if (currCoord <= targetCoord) {
        skippedTree = curr.getRightChild();
        nearestSearchRec(target, k, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      } else {
        skippedTree = curr.getLeftChild();
        nearestSearchRec(target, k, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
      }
    }

    // if there's a skipped subtree, go through if the list isn't full
    if (nearbyStars.size() < k && skippedTree != null) {
      nearestSearchRec(target, k, skippedTree, nearbyStars, (axis + 1) % this.dim);
    }
  }

  /**
   * Finds all nodes in the current tree within a given radius (inclusive) around a given target.
   *
   * @param target target node, center of radius
   * @param r      radius
   * @return list of node in radius
   */
  public List<N> radiusSearch(N target, double r) throws KDTreeException {
    // base cases, if negative radius or have an empty tree, return null
    if (this.root == null) {
      throw new KDTreeException("ERROR: tree must be initialized");
    } else if (r < 0) {
      throw new KDTreeException("ERROR: must search in a non-negative radius");
    }
    // run recursive nearest neighbors fun
    PriorityQueue<N> nearbyStars = new PriorityQueue<>(new EuclideanComparator<>(target));
    radiusSearchRec(target, r, this.root, nearbyStars, 0);
    // convert to list, reversing order so that least distance is first
    LinkedList<N> ls = new LinkedList<>();
    while (!nearbyStars.isEmpty()) {
      ls.addFirst(nearbyStars.poll());
    }
    return ls;
  }
  /*
   * recursive component.
   */
  private void radiusSearchRec(N target, double r, N curr, PriorityQueue<N> nearbyStars, int axis) {
    // Base: if curr is null after leaf, end.
    if (curr == null) {
      return;
    }
    // Curr: if it's within the radius, add it; must search both children
    if (distance(curr, target) <= r && !curr.equals(target)) { // don't add the target to list
      nearbyStars.add(curr);
      radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
      return; // End here.
    }

    // Children subtree elimination
    Double currCoord = curr.getCoords().get(axis);
    Double targetCoord = target.getCoords().get(axis);
    // if the axis distance is less than the radius, search both; else, only search under tree
    if (r + EPSILON >= Math.abs(currCoord - targetCoord)) {
      radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
    } else if (currCoord < targetCoord) {
      // curr is too negative of the target area, search greater side subtree
      radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
    } else {
      // search more negative side subtree
      radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
    }
  }

  /**
   * returns all nodes within a cube of the node space.
   *
   * @param coordBounds a dim-length list of pair-list of bounds
   * @return list of node in box
   */
  public List<N> boxSearch(List<List<Double>> coordBounds) throws KDTreeException {
    List<N> boxNodes = new ArrayList<>();
    if (this.root == null) {
      throw new KDTreeException("ERROR: Tree uninitialized");
    }
    // check for impossible bounds
    for (List<Double> bound : coordBounds) {
      double b1 = bound.get(0);
      double b2 = bound.get(1);
      if (b1 > b2) {
        throw new KDTreeException("ERROR: lower bound " + b1 + " greater than upper bound " + b2);
      }
    }
    // recur, filling box nodes list
    boxSearchRec(boxNodes, coordBounds, this.root, 0);
    return boxNodes;
  }
  /*
   * recursive component
   */
  private void boxSearchRec(List<N> boxNodes, List<List<Double>> coordBounds, N curr, int axis) {
    // Base case: curr is null after reaching the leaf end of a branch
    if (curr == null) {
      return;
    }

    // Curr: if in box, add to node list
    // within the box bounds, W->E [lat1, lat2] && S->N [lon2, lon1].
    List<Double> coords = curr.getCoords();
    boolean inBounds = true;
    for (int i = 0; i < dim; i++) {
      List<Double> currBound = coordBounds.get(i);
      double currCoord = coords.get(i);
      inBounds = inBounds && currBound.get(0) <= currCoord && currCoord <= currBound.get(1);
    }
    // Add, now we must must search both children if not in the box so skip Children code
    if (inBounds) {
      boxNodes.add(curr);
      boxSearchRec(boxNodes, coordBounds, curr.getLeftChild(), (axis + 1) % this.dim);
      boxSearchRec(boxNodes, coordBounds, curr.getRightChild(), (axis + 1) % this.dim);
      return; // End here.
    }

    // Children: if in the box along the axis, search both; else search the closer child
    Double currCoord = coords.get(axis);
    List<Double> currBound = coordBounds.get(axis);
    double lowerBound = currBound.get(0);
    double upperBound = currBound.get(1);
    // inside the box
    if (lowerBound <= currCoord && currCoord <= upperBound) {
      boxSearchRec(boxNodes, coordBounds, curr.getLeftChild(), (axis + 1) % this.dim);
      boxSearchRec(boxNodes, coordBounds, curr.getRightChild(), (axis + 1) % this.dim);
    } else if (currCoord < lowerBound) {
      // negative of box, search more pos
      boxSearchRec(boxNodes, coordBounds, curr.getRightChild(), (axis + 1) % this.dim);
    } else { // it's greater than the upper bound
      // search more neg
      boxSearchRec(boxNodes, coordBounds, curr.getLeftChild(), (axis + 1) % this.dim);
    }
  }

  /**
   * sets a target node with coords as the average/midpoint of given list of nodes.
   * @param target - node to change the coords of
   * @param nodes - list
   */
  public void makeAverageNode(N target, List<N> nodes) throws KDTreeException {
    if (nodes == null || nodes.size() == 0) {
      throw new KDTreeException("ERROR: nodes list cannot be null or empty");
    } else if (target == null) {
      throw new KDTreeException("ERROR: target node is null");
    }
    List<Double> coordsSum = new ArrayList<>();
    for (int i = 0; i < this.dim; i++) {
      coordsSum.add(0.);
    }

    // accumulate the sum of each dimension
    for (N node: nodes) {
      List<Double> coords = node.getCoords();
      for (int i = 0; i < coordsSum.size(); i++) {
        double currSum = coordsSum.get(i);
        currSum += coords.get(i);
        coordsSum.set(i, currSum);
      }
    }

    // find the average for each coord by dividing by the total # of recipes and set
    List<Double> targetCoords = target.getCoords();
    for (int i = 0; i < coordsSum.size(); i++) {
      targetCoords.set(i, coordsSum.get(i) / nodes.size());
    }
  }

  /**
   * takes a point in our dim space then checks if its in the given closed bounding box,
   * a dim (hyper) cube to search within.
   *
   * @param coords      point
   * @param coordBounds a dim-length list of pair-list of bounds
   * @return true if in box (inclusive), false else.
   * @throws KDTreeException if the bounds are out of order
   */
  public static boolean inBounds(List<Double> coords, List<List<Double>> coordBounds)
          throws KDTreeException {
    for (List<Double> bound : coordBounds) {
      double b1 = bound.get(0);
      double b2 = bound.get(0);
      if (b1 > b2) {
        throw new KDTreeException("ERROR: lower bound " + b1 + " greater than upper bound " + b2);
      }
    }

    return inBoundsRec(coords, coordBounds, 0, coords.size());
  }
  /*
   * recursively checks if the point is in each pair of bounds.
   */
  private static boolean inBoundsRec(List<Double> coords, List<List<Double>> coordBounds,
                                     int i, int dimen) {
    if (i >= dimen) {
      return true;
    }
    // check bounds, recur
    double currCoord = coords.get(i);
    List<Double> currBound = coordBounds.get(i);
    return currBound.get(0) <= currCoord && currCoord <= currBound.get(1)
            && inBoundsRec(coords, coordBounds, i + 1, dimen);
  }

  /**
   * Extra function to normalize the coordinates of a list of nodes.
   */
  public void normalizeTreeAxes() {
    normalizeAxes(getNodes());
  }

  /**
   * Function to normalize the coordinates of a list of nodes.
   * @param nodes - all nodes to be normalized.
   */
  public void normalizeAxes(List<N> nodes) {
    // create lists, maxes, mins for each considered nutrient
    List<List<Double>> axisCoordsLists = new ArrayList<>();
    List<Double> maxes = new ArrayList<>();
    List<Double> mins = new ArrayList<>();

    for (int i = 0; i < this.dim; i++) {
      axisCoordsLists.add(new ArrayList<>());
      maxes.add(Double.NEGATIVE_INFINITY);
      mins.add(Double.POSITIVE_INFINITY);
    }

    // add the each nodes' nutrients to its list, check for max/min
    for (N node : nodes) {
      List<Double> coords = node.getCoords();
      for (int i = 0; i < this.dim; i++) {
        double coord = coords.get(i);
        if (coord > maxes.get(i)) {
          maxes.set(i, coord);
        } // check min-ness
        if (coord < mins.get(i)) {
          mins.set(i, coord);
        }
        // add the nutrient to its list
        axisCoordsLists.get(i).add(coord);
      }
    }

    // normalize all nutrients for each type
    int sz = axisCoordsLists.size();
    for (int i = 0; i < sz; i++) { // dim
      List<Double> axisCoords = axisCoordsLists.get(i);
      int sz2 = axisCoords.size();
      for (int j = 0; j < sz2; j++) { //nodes size
        double n = axisCoords.get(j);
        double normalized = (n - mins.get(i)) / (maxes.get(i) - mins.get(i));
        // replace coords with their new values
        nodes.get(j).getCoords().set(i, normalized);
      }
    }
  }
}
