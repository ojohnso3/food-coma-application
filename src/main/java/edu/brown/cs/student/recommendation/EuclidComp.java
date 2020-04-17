package edu.brown.cs.student.recommendation;

import java.util.Comparator;

/**
 * Euclidean Comparator class implements Comparator of Kd Node.
 *
 * @param <T> This is the type parameter that extends the KDNode of T
 */
public final class EuclidComp<T extends KDNode<T>> implements Comparator<T> {

  private KDNode<T> target;

  /**
   * Compares distances between target and two nodes to find which is closer.
   *
   * @param o1 First node to find distance to from target.
   * @param o2 Second node to find distance to from target.
   * @return integer representing if o1 is closer to target (-1), or o2 (1), or if
   *         they are equally distant (0)
   */
  @Override
  public int compare(T o1, T o2) {

    double eucO1 = target.euclidDist(o1);
    double eucO2 = target.euclidDist(o2);

    if (eucO1 < eucO2) {
      return -1;
    } else if (eucO1 > eucO2) {
      return 1;
    }
    return 0;
  }

  /**
   * For setting the target node.
   *
   * @param t The node that target node will be set to.
   */
  public void setTarget(KDNode<T> t) {
    target = t;
  }

  /**
   * For getting the target node.
   *
   * @return The target node.
   */
  public KDNode<T> getTarget() {
    return target;
  }

}
