package edu.brown.cs.student.kdtree;

import java.util.List;

/**
 * A generic type for KDNode.
 */
public class BaseKDNode implements KDNode<BaseKDNode> {
  private final String id;
  private final List<Double> coords;
  private BaseKDNode leftChild;
  private BaseKDNode rightChild;

  /**
   * a node constructor.
   * @param id the node id
   * @param coords the node's coordinates
   */
  public BaseKDNode(String id, List<Double> coords) {
    this.id = id;
    this.coords = coords;
  }

  /**
   * checks if two KDTreeNodes are equal.
   * @param o - should be a KDTreeNode
   * @return - true if equal id, name, coords, leftChild, rightChild
   */
  @Override public boolean equals(Object o) {
    // if same object, skip computation
    if (o == this) {
      return true;
    }
    // check if node, if not false
    if (!(o instanceof BaseKDNode)) {
      return false;
    }

    // cast to KDTreeNode to compare fields
    BaseKDNode node = (BaseKDNode) o;

    // check fields
    return this.id.equals(node.getId());
  }

  /**
   * attempts to create a unique hashcode based on all fields used in equals.
   * @return int hashcode
   */
  @Override public int hashCode() {
    return this.id.hashCode();
  }

  /**
   * outputs a String representation of the KDTreeNode.
   * @return KDTreeNode string
   */
  @Override public String toString() {
    StringBuilder str = new StringBuilder(this.id  + ",");
    boolean notFirst = false;
    for (Double coord : coords) {
      if (notFirst) {
        str.append(",");
      } else {
        notFirst = true;
      }
      str.append(coord.toString());
    }
    return str.toString();
  }

  /**
   * getter.
   * @return getter for KDTreeNodeID
   */
  public String getId() {
    return this.id;
  }

  /**
   * getter.
   * @return getter for list of coordinates
   */
  @Override
  public List<Double> getCoords() {
    return this.coords;
  }
  /**
   * getter.
   * @return getter for left child
   */
  @Override
  public BaseKDNode getLeftChild() {
    return this.leftChild;
  }

  /**
   * setter.
   * @param lc to set
   */
  @Override
  public void setLeftChild(BaseKDNode lc) {
    this.leftChild = lc;
  }
  /**
   * getter.
   * @return getter for right child
   */
  @Override
  public BaseKDNode getRightChild() {
    return this.rightChild;
  }
  /**
   * setter.
   * @param rc to set
   */
  @Override
  public void setRightChild(BaseKDNode rc) {
    this.rightChild = rc;
  }
}
