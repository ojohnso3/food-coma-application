package edu.brown.cs.student.recommendation;

/**
 * Interface for generic KD Nodes.
 *
 * @param <T> This is the type parameter that extends the KDNode of T
 */
public interface KDNode<T extends KDNode<T>> {

  /**
   * Sets an instance of kd node to the left node.
   *
   * @param left The node being set to the left child.
   */
  void setLeft(T left);

  /**
   * Sets an instance of kd node to the right node.
   *
   * @param right The node being set to the left child.
   */
  void setRight(T right);

  /**
   * Gets the left child node.
   *
   * @return The left child node.
   */
  T getLeft();

  /**
   * Gets the right child node.
   *
   * @return The left child node.
   */
  T getRight();

  /**
   * Gets the double array of coordinates.
   *
   * @return The double array of coordinates.
   */
  double[] getCoords();

  /**
   * Sets the array of coordinates to the coords passed in.
   *
   * @param coords The coordinate array being set.
   */
  void setCoords(double[] coords);

  /**
   * Gets the double array of coordinates.
   *
   * @param distToNode The node we are trying to find the distance from.
   * @return The double representing the distance from the current node to
   *         distToNode.
   */
  double euclidDist(T distToNode);

}
