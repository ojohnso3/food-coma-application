package edu.brown.cs.student.kdtree;

import edu.brown.cs.student.kdtree.KDNode;

import java.util.Comparator;
import java.util.List;

/**
 * Compares two nodes' distance to their target node.
 * @param <N> - Node type
 */
class EuclideanComparator<N extends KDNode<N>> implements Comparator<N> {
  private N target;
  EuclideanComparator(N target) {
    this.target = target;
  }

  @Override
  public int compare(N o1, N o2) {
    double dist1 = 0.;
    double dist2 = 0.;
    // get each list of coords only once
    List<Double> ls1 = o1.getCoords();
    List<Double> ls2 = o2.getCoords();
    List<Double> lst = this.target.getCoords();
    // slightly lazier distance (one loop, no sqrt)
    int sz = o1.getCoords().size();
    for (int i = 0; i < sz; i++) {
      Double d1 = ls1.get(i) - lst.get(i);
      dist1 += d1 * d1;
      Double d2 = ls2.get(i) - lst.get(i);
      dist2 += d2 * d2;
    }
    // switched (* -1) so that more positive, larger distances are at the head and can be polled
    return Double.compare(dist2, dist1);
  }
}
