package edu.brown.cs.student.kdtree;

import java.util.List;

public class BaseKDNode implements KDNode<BaseKDNode> {
  private String id;
  private List<Double> coords;
  private BaseKDNode leftChild;
  private BaseKDNode rightChild;

  public BaseKDNode(String id, List<Double> coords, BaseKDNode leftChild, BaseKDNode rightChild) {
    this.id = id;
    this.coords = coords;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
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
    boolean coordsB = true;
    for (int i = 0; i < coords.size(); i++) {
      if (!node.getCoords().get(i).equals(this.getCoords().get(i))) {
        coordsB = false;
        break;
      }
    }
    boolean bool = this.id.equals(node.getId()) && coordsB;
    // left
    if (this.leftChild != null) {
      if (node.getLeftChild() == null) {
        return false;
      } else {
        bool = bool && this.leftChild.equals(node.getLeftChild());
      }
    } else {
      bool = bool && (node.getLeftChild() == null);
    }
    // right
    if (this.rightChild != null) {
      if (node.getRightChild() == null) {
        return false;
      } else {
        bool = bool && this.rightChild.equals(node.getRightChild());
      }
    } else {
      bool = bool && (node.getRightChild() == null);
    }

    return bool;
  }

  /**
   * attempts to create a unique hashcode based on all fields used in equals.
   * @return int hashcode
   */
  @Override public int hashCode() {
    int result = 0;
    final int magicNumber = 31; // from the textbook (prime and 2^5 - 1)
    result = magicNumber * result + this.id.hashCode();
    if (this.leftChild != null) {
      result = magicNumber * result + this.leftChild.hashCode();
    }
    if (this.rightChild != null) {
      result = magicNumber * result + this.rightChild.hashCode();
    }
    for (Double coord : this.coords) {
      result = magicNumber * result + Double.hashCode(coord);
    }
    return result;
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
