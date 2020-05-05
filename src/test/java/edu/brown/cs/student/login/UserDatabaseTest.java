package edu.brown.cs.student.login;

import edu.brown.cs.student.database.APIException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Class that contains functions to test the methods in UserDatabase.
 */
public class UserDatabaseTest {

  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/login/test.csv";
  private User user1;
  private User user2;

  /**
   * Function to setup objects to test the methods in UserDatabase.
   */
  @Test
  public void testAll() {
    try {
      this.user1 = new User("username", "password", PATH_CSV);
      this.user2 = new User("username 2", "password", PATH_CSV);
    } catch (AccountException e) {
      e.printStackTrace();
    }

  }

  /**
   * Function to test the checkUsername method in UserDatabase.
   */
  public void testCheckUsername() {
    try {
      assertTrue(UserDatabase.checkUsername(this.user1.getUsername()));
      assertTrue(UserDatabase.checkUsername(this.user2.getUsername()));
      assertFalse(UserDatabase.checkUsername("aslkjfa;afljksda"));
      assertFalse(UserDatabase.checkUsername(""));
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * Function to test the insertUser method in UserDatabase.
   */
  public void testInsertUser() {
    try {
      UserDatabase.insertUser(this.user1);
      User user1Test = UserDatabase.getUser(this.user1.getUsername());
      assertEquals(this.user1, user1Test);
      UserDatabase.insertUser(this.user2);
      User user2Test = UserDatabase.getUser(this.user2.getUsername());
      assertEquals(this.user2, user2Test);

      assertThrows(SQLException.class, () -> {UserDatabase.insertUser(this.user2);});
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (AccountException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
