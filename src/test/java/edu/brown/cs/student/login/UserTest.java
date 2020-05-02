package edu.brown.cs.student.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * test User.
 */
public class UserTest {
  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.csv";

  @Test
  public void constructorTest() throws AccountException {
    String user = "user";
    String pass = "pass";
//    User u = new User(user, pass, PATH_CSV);

    assertEquals(user, u.getUsername());
    assertEquals(true, Accounts.checkLogin(u.getUsername(), pass, PATH_CSV));
    assertEquals(false, Accounts.checkLogin("fake user", pass, PATH_CSV));
    assertEquals(false, Accounts.checkLogin(user, "fake pass", PATH_CSV));
    assertEquals(false, Accounts.checkLogin("fake user", "fake pass", PATH_CSV));

  }
}

