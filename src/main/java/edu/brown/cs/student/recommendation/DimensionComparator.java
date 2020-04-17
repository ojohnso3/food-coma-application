package edu.brown.cs.student.recommendation;

import java.util.Comparator;

/**
 * Dimension Comparator class implements Comparator of KD node.
 *
 * @param <T> This is the type parameter that extends the KDNode of T
 */
public final class DimensionComparator<T extends KDNode<T>> implements Comparator<T> {

  private int dimension;

  /**
   * Compares current dimension of two kd nodes to see which is greater.
   *
   * @param o1 First node to compare current dimension
   * @param o2 Second node to compare current dimension
   * @return An integer representing if o1 at specified dimension less than o2
   *         (-1), or o2 less than o1 (1), or if they are equal (0). Returns 2 if
   *         current dimension invalid.
   */
  @Override
  public int compare(T o1, T o2) {
    double[] o1Coords = o1.getCoords();
    double[] o2Coords = o2.getCoords();


    if (dimension >= o1.getCoords().length) {
      return 2; // since dimension is used as index, it can't be more than num dims
    }

    if (o1Coords[dimension] < o2Coords[dimension]) {
      return -1;
    } else if (o1Coords[dimension] > o2Coords[dimension]) {
      return 1;
    }
    return 0;
  }

  /**
   * For setting the current dimension to compare on.
   *
   * @param currDim The dimension to set the instance variable to.
   */
  public void setDimension(int currDim) {
    dimension = currDim;
  }

  /**
   * For getting the current dimension to compare on.
   *
   * @return The current dimension being compared on.
   */
  public int getDimension() {
    return dimension;
  }

}
