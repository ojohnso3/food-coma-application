package edu.brown.cs.student.kdtree;

import java.util.Comparator;

/**
 * comparator for Node by distance along dim field.
 * @param <N> - Node type
 */
class AxisComparator<N extends KDNode<N>> implements Comparator<N> {
  private final int axis;
  AxisComparator(int axis) {
    this.axis = axis;
  }

  @Override
  public int compare(N o1, N o2) {
    return Double.compare(o1.getCoords().get(this.axis), o2.getCoords().get(this.axis));
  }
}
