package edu.brown.cs.student.kdtree;

import java.util.List;

/**
 * interface for nodes for a KDTree.
 * @param <N> - Nodable type
 */
public interface KDNode<N extends KDNode<N>>  {
  /**
   * id getter.
   * @return id
   */
  String getId();
  /**
   * getter.
   * @return getter for list of coordinates
   */
  List<Double> getCoords();
  /**
   * getter.
   * @return getter for left child
   */
  N getLeftChild();
  /**
   * setter.
   * @param lc to set
   */
  void setLeftChild(N lc);
  /**
   * getter.
   * @return getter for right child
   */
  N getRightChild();
  /**
   * setter.
   * @param rc to set
   */
  void setRightChild(N rc);
}
