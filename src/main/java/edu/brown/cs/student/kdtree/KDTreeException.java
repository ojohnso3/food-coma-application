package edu.brown.cs.student.kdtree;

/**
 * Exception class thrown by non-fatal kdtree errors. In these cases, no proper output can be given
 * and user should just propagate the error message.
 */
public class KDTreeException extends Exception {
  /**
   * Constructor for a KDTreeException.
   * @param message String error message
   */
  public KDTreeException(String message) {
    super(message);
  }
}
