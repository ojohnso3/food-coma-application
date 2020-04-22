package edu.brown.cs.student.foodCOMA.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.BCrypt;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.*;

public class AccountsTest {

  @Test
  public void readHeaderTest() throws AccountException {
//    assertEquals(Accounts.readHeader(), "username,encodedPassword,salt"); // TODO: not working here
  }

  @Test
  public void generateSaltTest() {
    byte[] salt1 = Accounts.generateSalt();
    byte[] salt2 = Accounts.generateSalt();
    byte[] salt3 = Accounts.generateSalt();
    System.out.println("str(s): " + new String(salt1));
    System.out.println("salt: " + Arrays.toString(salt1));
    System.out.println("salt tostr: " + salt1.toString());
    System.out.println("salt arr tostr: " + Arrays.toString(salt1));
    //Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (byte b : salt1) {
      sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
    }
    System.out.println("hex salt: " + sb.toString());
    try (FileOutputStream sout = new FileOutputStream("test.txt");
         PrintWriter test = new PrintWriter("test.txt");
         BufferedReader b64 = new BufferedReader(new FileReader("test.txt"))) {
      sout.write(salt1);
//      String salt1s = new BASE64Encoder().encode(salt); //DatatypeConverter.printBase64Binary(salt1);
//      String s1s =
      System.out.println("file encode bytes: " + Arrays.toString((new FileReader("test.txt")).getEncoding().getBytes()));
      String salt1Read = b64.readLine();
      System.out.println("br readline: " + salt1Read);
//      assertEquals(new String(salt1), salt1Read); // TODO: issue
      System.out.println("br readline getbytes: " + Arrays.toString(salt1Read.getBytes()));
//      assertEquals(salt1, salt1Read.getBytes());
//      test.write(salt1.toString() + "\n");
//      test.write(Arrays.toString(salt1) + "\n");

    } catch (IOException e) {
      e.printStackTrace();
    }

    assertNotEquals(salt1, salt2);
    assertNotEquals(salt1, salt3);
    assertNotEquals(salt2, salt3);
  }
//
//  @Test
//  public void simpleHashTest() throws AccountException {
//    byte[] salt = Accounts.generateSalt();
//    String pass = "password";
//    int hash = Accounts.simpleHash(pass, salt);
//    System.out.println(hash);
//    // check that hashing the same password and salt gives the same hash
//    assertEquals(hash, Accounts.simpleHash(pass, salt));
//    // check that differences in pass, salt => different hash
//    assertNotEquals(hash, Accounts.simpleHash(pass, Accounts.generateSalt()));
//    assertNotEquals(hash, Accounts.simpleHash("passw0rd", salt));
//    assertNotEquals(hash, Accounts.simpleHash("Password", salt));
//    assertNotEquals(hash, Accounts.simpleHash("wordpass", salt));
//  }

//  @Test
//  public void hashPasswordPBKDF2Test() throws AccountException {
//    byte[] salt = Accounts.generateSalt();
//    String pass = "password";
//    byte[] hash = Accounts.hashPasswordPBKDF2(pass, salt);
//    System.out.println(hash);
//    // check that hashing the same password and salt gives the same hash
//    assertEquals(hash, Accounts.hashPasswordPBKDF2(pass, salt));
//    // check that differences in pass, salt => different hash
//    assertNotEquals(hash, Accounts.hashPasswordPBKDF2(pass));
//    assertNotEquals(hash, Accounts.hashPasswordPBKDF2(pass, Accounts.generateSalt()));
//    assertNotEquals(hash, Accounts.hashPasswordPBKDF2("passw0rd", salt));
//    assertNotEquals(hash, Accounts.hashPasswordPBKDF2("Password", salt));
//    assertNotEquals(hash, Accounts.hashPasswordPBKDF2("wordpass", salt));
//  }

  @Test
  public void writeLoginInfoTest() throws AccountException {
    String user = "user";
    String pass = "pass";
    byte[] salt = Accounts.generateSalt();
    byte[] hash = Accounts.hashPassword(pass, salt);
    String path = "test.txt";

    assertEquals(user, new String(user.getBytes()));
    assertNotEquals(user.getBytes(), new String(user.getBytes()).getBytes());
    assertNotEquals(salt, new String(salt).getBytes());

    assertEquals(user, Base64.getEncoder().encodeToString(Base64.getDecoder().decode(user)));
    assertNotEquals(user, Base64.getEncoder().encodeToString(user.getBytes()));
    assertNotEquals(salt, Base64.getDecoder().decode(Base64.getEncoder().encodeToString(salt)));

    new Accounts() {
      public void callProtectedMethod(String user, String pass, byte[] salt, String path) throws AccountException {
        writeLoginInfo(user, pass, salt, path);
      }
    }.callProtectedMethod(user, pass, salt, path);

    try (BufferedReader br = new BufferedReader(new FileReader(path));
         FileInputStream is = new FileInputStream(path)) {
      String[] info = br.readLine().split(",");
      assertEquals(info[0], user);
      assertNotEquals(info[2].getBytes(), salt);
//      assertNotEquals(Base64.getDecoder().decode(info[2]), salt);
      assertNotEquals(info[1].getBytes(), hash);
      is.read();

      assertNotEquals(info[1].getBytes(), Accounts.hashPassword(pass, info[2].getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void writeLoginInfoBCryptTest() throws AccountException {
    String path = "test.txt";
    String user = "user";
    String pass = "pass";
    String salt = BCrypt.gensalt();
    String hash = BCrypt.hashpw(pass, salt);
    assertTrue(BCrypt.checkpw(pass, hash));

    new Accounts() {
      public void callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
        writeLoginInfo(user, pass, salt, path);
      }
    }.callProtectedMethod(user, pass, salt, path);

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String[] info = br.readLine().split(",");
      assertEquals(user, info[0]);
      assertEquals(salt, info[2]);
      assertEquals(BCrypt.hashpw(pass, info[2]), info[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkLoginTest() throws AccountException {
    String path = "test.csv";
    String user = "user";
    String pass = "pass";
    String salt = BCrypt.gensalt();
    String hash = BCrypt.hashpw(pass, salt);
    assertTrue(BCrypt.checkpw(pass, hash));

    Accounts accounts = new Accounts() {
      public Accounts callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
        writeLoginInfo(user, pass, salt, path);
        return this;
      }
    }.callProtectedMethod(user, pass, salt, path);

    assertEquals("Successful Login!", Accounts.checkLogin(user, pass, path));
    assertEquals("Failed Login: Please try again.", Accounts.checkLogin("fake user", pass, path));
    assertEquals("Failed Login: Please try again.", Accounts.checkLogin(user, "fake pass", path));
    assertEquals("Failed Login: Please try again.", Accounts.checkLogin("fake user", "fake pass", path));
  }
}
