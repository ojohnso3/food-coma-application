package edu.brown.cs.student.login;

/**
 * login exception class.
 */
public class AccountException extends Exception {
  public AccountException(String e) {
    super(e);
  }

  public AccountException(String e, Throwable t) {
    super(e, t);
  }
}
