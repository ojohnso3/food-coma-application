package edu.brown.cs.student.login;

/**
 * Exception for invalid user creation input.
 */
public class UserCreationException extends Exception {
  public UserCreationException(String message) {
    super(message);
  }
}

