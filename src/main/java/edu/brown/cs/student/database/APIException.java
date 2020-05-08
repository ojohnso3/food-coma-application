package edu.brown.cs.student.database;

/**
 * This class is an exception that is thrown when the api responds with an error code.
 */
public class APIException extends Exception {

  /**
   * Constructor for APIException.
   * @param message - description of the error.
   */
  public APIException(String message) {
    super(message);
  }
}
