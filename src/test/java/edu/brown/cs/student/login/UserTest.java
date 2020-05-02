package edu.brown.cs.student.login;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test User.
 */
public class UserTest {
  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/login/test.csv";

  @Test
  public void constructorTest() throws AccountException {
    String user = "user";
    String pass = "pass";
    User u = new User(user, pass, PATH_CSV);

    assertEquals(user, u.getUsername());
    assertTrue(Accounts.checkLogin(u.getUsername(), pass, PATH_CSV));
    assertFalse(Accounts.checkLogin("fake user", pass, PATH_CSV));
    assertFalse(Accounts.checkLogin(user, "fake pass", PATH_CSV));
    assertFalse(Accounts.checkLogin("fake user", "fake pass", PATH_CSV));

  }
}

