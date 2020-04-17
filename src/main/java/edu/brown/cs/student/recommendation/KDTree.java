package edu.brown.cs.student.recommendation;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

/**
 * The KDTree class of the project.
 *
 * @param <T> This is the type parameter that extends the KDNode of T
 */
public final class KDTree<T extends KDNode<T>> {

  private int numDims;
  private T root;

  /**
   * Constructor for the KD-Tree.
   *
   * @param numDim The number of dimensions the node will store for coordinates (3
   *               for this project)
   * @param root   the root of the tree
   */
  public KDTree(int numDim, T root) {
    this.root = root;
    this.numDims = numDim;
  }

  /**
   * Method is responsible for building the KDTree recursively. First sorts the
   * list of stars using the dimension comparator, then finds the median node and
   * recurses on the left and right subtree of this node.
   *
   * @param currDim The current dimension to compare (i.e. x=0, y=1, or z=2)
   * @param nodes     The list of star nodes
   * @return The root of the KDTree
   */
  public T buildTree(int currDim, List<T> nodes) {
    int size = nodes.size();
    // sorts nodes along a specific dimension using vector comparator
    DimensionComparator<T> vectorComp = new DimensionComparator<T>();
    currDim = currDim % this.numDims;
    vectorComp.setDimension(currDim % this.numDims);
    // cannot sort because nodes don't have coordinates
    Collections.sort(nodes, vectorComp);
    int median = size / 2;
    T node = nodes.get(median);
    // makes list of nodes of left/right subtrees to be built recursively
    List<T> leftTree = nodes.subList(0, median);
    List<T>  rightTree = nodes.subList(median + 1, size);
    if (leftTree.size() > 0) {
      node.setLeft(this.buildTree(currDim + 1, leftTree));
    }
    if (rightTree.size() > 0) {
      node.setRight(this.buildTree(currDim + 1, rightTree));
    }
    root = node;
    return root;
  }


  /**
   * Finds the nearest neighbors given the target node, the number of neighbors to
   * find, and a boolean representing whether we have to remove the closest node
   * (this is true when the user input the name of a node in quotes). Calls on the
   * helper function traverse_tree to find the close neighbors!
   *
   * @param k           The number of neighbors to find
   * @param target      The node to find the closest neighbors with respect to
   * @param removeFirst Boolean responsible for being true when we do not want to
   *                    include the star itself as a neighbor
   * @return the MinMaxPriorityQueue of KD Nodes queue of close stars
   */
  public MinMaxPriorityQueue<T> findKNearest(int k, T target, boolean removeFirst) {
    int numToAdd = k;
    if (this.root == null || k < 0) {
      return null;
    }
    EuclidComp<T> eucComp = new EuclidComp<T>();
    eucComp.setTarget(target);
    if (k == 0) {
      return MinMaxPriorityQueue.orderedBy(eucComp).create();
    }
    if (removeFirst) {
      numToAdd += 1; // extra because have to remove itself here
    }
    MinMaxPriorityQueue<T> queueCloseStars = MinMaxPriorityQueue.orderedBy(eucComp)
        .maximumSize(numToAdd).create();
    queueCloseStars = this.traverseTree(queueCloseStars, target, this.root, 0, false, -1);
    if (removeFirst) {
      // removing the star with "name" entered by user
      queueCloseStars.removeFirst();
    }
    return queueCloseStars;
  }

  /**
   * Finds the nearest nodes given a radius, the target node, and a boolean
   * representing whether we have to remove the closest node (this is true when
   * the user input the name of a node in quotes). Calls on the helper function
   * traverse_tree to find the close nodes within the provided radius!
   *
   * @param target      The node to find the closest nodes with respect to the
   *                    radius
   * @param radius      The radial limit for finding neighbors
   * @param removeFirst Boolean responsible for being true when we do not want to
   *                    include the star itself as a neighbor
   * @return the MinMaxPriorityQueue of kD Nodes queue of close stars
   */
  public MinMaxPriorityQueue<T> radiusSearch(T target, double radius, boolean removeFirst) {
    if (root == null) {
      return null;
    }
    EuclidComp<T> eucComp = new EuclidComp<T>();
    eucComp.setTarget(target);
    MinMaxPriorityQueue<T> queueCloseStars = MinMaxPriorityQueue.orderedBy(eucComp).create();
    if (radius < 0) {
      return null;
    }
    queueCloseStars = this.traverseTree(queueCloseStars, target, this.root, 0, true, radius);
    if (removeFirst) {
      // removing the star with "name" entered by user
      queueCloseStars.removeFirst();
    }
    return queueCloseStars;
  }

  /**
   * Finds the nearest nodes by traversing the KDTree recursively. Followed the
   * pseudocode in order to implement this method. Also checks if doing a neighbor
   * or radius search and adjusts accordingly. With radius search, only adds to
   * the MinMax priority queue when the distance between the current node and the
   * target is less than or equal to the radius distance. If both children of the
   * current node (root) are null, we return the queue as a base case. I explain
   * some more details with in-line comments below!
   *
   * @param queue     The MinMax queue for storing the closest nodes
   * @param target    The node to find the closest nodes with respect to
   * @param root      The current node to compare to the target and then recurse
   *                  on the left and right subtree of this node
   * @param dimension The current dimension to compare with respect to (x, y, z)
   * @param isRadius  True for radius search, false for neighbors search
   * @param radius    The radial limit for finding neighbors
   *
   * @return the MinMaxPriorityQueue of KD Nodes queue of close stars
   */
  private MinMaxPriorityQueue<T> traverseTree(MinMaxPriorityQueue<T> queue, T target, T currNode,
      int dimension, boolean isRadius, double radius) {
    if (!isRadius) {
      // add current node to queue if neighbor search
      queue.add(currNode);
    } else {
      if (target.euclidDist(currNode) <= radius) {
        // if radius search, must check distance<=radius before adding
        // node
        queue.add(currNode);
      }
    }
    // base case
    if (currNode.getLeft() == null && currNode.getRight() == null) {
      return queue;
    }
    int currDim = dimension % this.numDims;
    double[] rootCoords = currNode.getCoords();
    double[] targetCoords = target.getCoords();
    double totalDistFar = 0;
    if (!isRadius) {
      // finding the farthest distance from target to a node in the queue
      // for comparison sake in the neighbor search
      T farNode = queue.peekLast();
      totalDistFar = target.euclidDist(farNode);
    } else {
      // sets total max distance to radius for radius search
      totalDistFar = radius;
    }
    // calculates the axis distance
    double axisDist = Math.abs(targetCoords[currDim] - rootCoords[currDim]);
    if (totalDistFar >= axisDist) {
      // increments the dimension before recursing on subtree
      currDim++;

      if (currNode.getLeft() != null) {
        queue = this.traverseTree(queue, target, currNode.getLeft(), currDim, isRadius, radius);
      }

      if (currNode.getRight() != null) {
        queue = this.traverseTree(queue, target, currNode.getRight(), currDim, isRadius, radius);
      }

    } else {

      if (currNode.getRight() != null && rootCoords[currDim] < targetCoords[currDim]) {
        // increments the dimension before recursing on subtree
        currDim++;
        queue = this.traverseTree(queue, target, currNode.getRight(), currDim, isRadius, radius);
      } else {
        // we know left must be null here because already checked to make
        // sure both not null
        currDim++;
        queue = this.traverseTree(queue, target, currNode.getLeft(), currDim, isRadius, radius);
      }
    }
    return queue;
  }
}
