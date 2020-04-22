package edu.brown.cs.student.foodCOMA.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.BCrypt;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class AccountsTest {
  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.csv";

  @Test
  public void readHeaderTest() throws AccountException {
    assertEquals(Accounts.readHeader(), "username,encodedPassword,salt");
  }

  @Test
  public void writeLoginInfoTest() throws AccountException {
    String user = "user";
    String pass = "pass";
    String salt = BCrypt.gensalt();
    String hash = BCrypt.hashpw(pass, salt);
    assertTrue(BCrypt.checkpw(pass, hash));
    // access protected write info method
    new Accounts() {
      public void callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
        writeLoginInfo(user, pass, salt, path);
      }
    }.callProtectedMethod(user, pass, salt, PATH_CSV);
    // check the that written in things match what was created and
    try (BufferedReader br = new BufferedReader(new FileReader(PATH_CSV))) {
      String[] info = br.readLine().split(",");
      assertEquals(user, info[0]);
      assertEquals(salt, info[2]);
      // the password can be recreated w/ the salt
      assertEquals(BCrypt.hashpw(pass, info[2]), info[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkLoginTest() throws AccountException {
    String user = "user";
    String pass = "pass";
    String salt = BCrypt.gensalt();
    String hash = BCrypt.hashpw(pass, salt);
    assertTrue(BCrypt.checkpw(pass, hash));

    new Accounts() {
      public void callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
        writeLoginInfo(user, pass, salt, path);
      }
    }.callProtectedMethod(user, pass, salt, PATH_CSV);

    assertEquals("logged in!", Accounts.checkLogin(user, pass, PATH_CSV));
    assertEquals("login failed", Accounts.checkLogin("fake user", pass, PATH_CSV));
    assertEquals("login failed", Accounts.checkLogin(user, "fake pass", PATH_CSV));
    assertEquals("login failed", Accounts.checkLogin("fake user", "fake pass", PATH_CSV));
  }
}
