package edu.brown.cs.student.foodCOMA.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.BCrypt;
import edu.brown.cs.student.login.User;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * test User.
 */
public class UserTest {

  @Test
  public void constructorTest() throws AccountException {
    String path = "test.csv";
    String user = "user";
    String pass = "pass";
    User u = new User(user, pass, path);

    assertEquals(user, u.getUsername());

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String[] info = br.readLine().split(",");
      assertEquals(user, info[0]);
      assertTrue(BCrypt.checkpw(pass, info[1]));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

