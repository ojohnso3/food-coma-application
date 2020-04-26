package edu.brown.cs.student.login;

/**
 * Exception for invalid user creation input.
 */
public class UserCreationException extends Exception {
  public UserCreationException() {
    super();
  }

  public UserCreationException(String message) {
    super(message);
  }

  public UserCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserCreationException(Throwable cause) {
    super(cause);
  }
}

