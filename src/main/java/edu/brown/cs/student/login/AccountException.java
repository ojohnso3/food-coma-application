package edu.brown.cs.student.login;

/**
 * login exception class.
 */
public class AccountException extends Exception {
  /**
   * constructor.
   * @param e message.
   */
  public AccountException(String e) {
    super(e);
  }

  /**
   * constructor.
   * @param e message
   * @param t exception
   */
  public AccountException(String e, Throwable t) {
    super(e, t);
  }
}
