package edu.brown.cs.student.login;

/**
 * login exception class.
 */
public class LoginException extends Exception {
  public LoginException(String e) {
    super(e);
  }

  public LoginException(String e, Throwable t) {
    super(e, t);
  }
}
