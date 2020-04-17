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
  private int dim;

  /**
   * Constructor.
   *
   * @param dim - dimension of nodes
   */
  public KDTree(int dim) {
    this.dim = dim;
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
   * which does the rest.
   *
   * @param nodes - list of nodes composing the tree
   *              //   * @return the median node/root of the subtree to be a child of the parent
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

  @Deprecated
  public void naiveGenerateTree(List<N> nodes) {
    for (N node : nodes) {
      if (this.root == null) {
        this.root = node;
      } else {
        insert(node);
      }
    }
  }

  //TODO: requires parent nodes to work fully.
  public boolean validateTree() {
    double[] bounds = new double[this.dim];

    return validateTreeRec(bounds, this.root, 0);
  }

  private boolean validateTreeRec(double[] bounds, N curr, int axis) {
    if (curr == null) {
      return true;
    }
    // check children relation to curr
    double bound = bounds[axis];
    bound = this.root.getCoords().get(axis);
    boolean valid = true;
    valid = valid && this.root.getLeftChild().getCoords().get(axis) <= bound;
    valid = valid && this.root.getRightChild().getCoords().get(axis) >= bound;
    // check old part bounds

    for (int i = 0; i < this.dim; i++) {
      double boundi = bounds[i];
      valid = valid && curr.getCoords().get(i) <= boundi;
    }

    return valid && validateTreeRec(bounds, curr.getLeftChild(), axis)
            && validateTreeRec(bounds, curr.getRightChild(), axis);
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
   * Finds the k-nearest neighbors:
   * Take in a location in space (specified as either an x,y,z-coordinate or the name of a star) and
   * the desired number of neighbors to be found, a positive integer k.
   *
   * @param target target node to be close to
   * @param k      num of neighbors in the list
   * @return list of node
   */
  public List<N> nearestSearch(N target, int k) throws KDTreeException {
    // base cases, if returning 0 stars or have an empty tree, return null
    if (this.root == null) {
      throw new KDTreeException("ERROR: tree must be initialized");
    } else if (k < 1) {
      throw new KDTreeException("ERROR: must search for at least one neighbor");
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

  public List<N> radiusSearch(N target, int r) throws KDTreeException {
    // base cases, if negative radius or have an empty tree, return null
    if (this.root == null) {
      throw new KDTreeException("ERROR: tree must be initialized");
    } else if (r < 0) {
      throw new KDTreeException("ERROR: must search for at least one neighbor");
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

  private void radiusSearchRec(N target, int r, N curr, PriorityQueue<N> nearbyStars, int axis) {
    // Base: if curr is null after leaf, end.
    if (curr == null) {
      return;
    }
    // Curr: if it's within the radius, add it; must search both children
    if (distance(curr, target) <= r && !curr.equals(target)) { // don't add the target to list
      nearbyStars.add(curr);
      radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
      return;
    }

    // Children subtree elimination
    Double currCoord = curr.getCoords().get(axis);
    Double targetCoord = target.getCoords().get(axis);
    // if the axis distance is less than the radius, search both; else, only search under tree
    if (r >= Math.abs(currCoord - targetCoord)) {
      radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
    } else {
      if (currCoord < targetCoord) {
        // more negative of target, search greater side subtree
        radiusSearchRec(target, r, curr.getRightChild(), nearbyStars, (axis + 1) % this.dim);
      } else {
        // search more negative side subtree
        radiusSearchRec(target, r, curr.getLeftChild(), nearbyStars, (axis + 1) % this.dim);
      }
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
}