package edu.brown.cs.student.login;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class AccountsTest {
  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/login/test.csv";

  @Test
  public void readHeaderTest() throws AccountException {
//    // clear the file
//    try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_CSV, false))) {
//      bw.write("username,passwordHash,salt");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    assertEquals("username,passwordHash,salt", Accounts.readHeader());
  }

  @Test
  public void writeLoginInfoTest() throws AccountException {
//    String user = "user";
//    String pass = "pass";
//    String salt = BCrypt.gensalt();
//    String hash = BCrypt.hashpw(pass, salt);
//    assertTrue(BCrypt.checkpw(pass, hash));
//    String user2 = "user2";
//    String pass2 = "pass2";
//    String salt2 = BCrypt.gensalt();
//    String hash2 = BCrypt.hashpw(pass2, salt2);
//    assertTrue(BCrypt.checkpw(pass2, hash2));
//
//    // clear the file
//    try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_CSV, false))) {
//      bw.write("username,passwordHash,salt");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    // access protected write info method
//    Accounts.writeLoginInfo(user, pass, salt, PATH_CSV);
//    Accounts.writeLoginInfo(user2, pass2, salt2, PATH_CSV);
//    new Accounts() {
//      public void callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
//        writeLoginInfo(user, pass, salt, path);
//      }
//    }.callProtectedMethod(user2, pass2, salt2, PATH_CSV);


//    // check the that written in things match what was created and
//    try (BufferedReader br = new BufferedReader(new FileReader(PATH_CSV))) {
//      assertEquals("username,passwordHash,salt", br.readLine());
//      String[] info = br.readLine().split(",");
//      assertEquals(user, info[0]);
//      assertEquals(salt, info[2]);
//      // the password can be recreated w/ the salt
//      assertEquals(BCrypt.hashpw(pass, info[2]), info[1]);
//      assertTrue(BCrypt.checkpw(pass, info[1]));
//
//      String[] info2 = br.readLine().split(",");
//      assertEquals(user2, info2[0]);
//      assertEquals(salt2, info2[2]);
//      // the password can be recreated w/ the salt
//      assertEquals(BCrypt.hashpw(pass2, info2[2]), info2[1]);
//      assertTrue(BCrypt.checkpw(pass2, info2[1]));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

  @Test
  public void checkLoginTest() throws AccountException {
//    String user = "user";
//    String pass = "pass";
//    String salt = BCrypt.gensalt();
//    String hash = BCrypt.hashpw(pass, salt);
//    assertTrue(BCrypt.checkpw(pass, hash));
//
//    // clear the file
//    try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_CSV, false))) {
//      bw.write("username,passwordHash,salt");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    Accounts.writeLoginInfo(user, pass, salt, PATH_CSV);
//
//    assertTrue(Accounts.checkLogin(user, pass, PATH_CSV));
//    assertFalse(Accounts.checkLogin("fake user", pass, PATH_CSV));
//    assertFalse(Accounts.checkLogin(user, "fake pass", PATH_CSV));
//    assertFalse(Accounts.checkLogin("fake user", "fake pass", PATH_CSV));
  }
}

