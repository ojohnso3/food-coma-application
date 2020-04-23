//package edu.brown.cs.student.foodCOMA.login;
//
//import edu.brown.cs.student.login.AccountException;
//import edu.brown.cs.student.login.Accounts;
//import edu.brown.cs.student.login.AccountsOld;
//import edu.brown.cs.student.login.BCrypt;
//import org.junit.Test;
//
//import java.io.*;
//import java.util.Arrays;
//import java.util.Base64;
//
//import static org.junit.Assert.*;
//
//public class AccountsOldTest {
//  private static final String PATH_TXT = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.txt";
//  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.csv";
//
//  @Test
//  public void readHeaderTest() throws AccountException {
////    assertEquals(AccountsOld.readHeader(), "username,encodedPassword,salt");
//  }
//
//  @Test
//  public void generateSaltTest() {
//    byte[] salt1 = AccountsOld.generateSalt();
//    byte[] salt2 = AccountsOld.generateSalt();
//    byte[] salt3 = AccountsOld.generateSalt();
//    System.out.println("str(s): " + new String(salt1));
//    System.out.println("salt: " + Arrays.toString(salt1));
//    System.out.println("salt tostr: " + salt1.toString());
//    System.out.println("salt arr tostr: " + Arrays.toString(salt1));
//    //Convert it to hexadecimal format
//    StringBuilder sb = new StringBuilder();
//    for (byte b : salt1) {
//      sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
//    }
//    System.out.println("hex salt: " + sb.toString());
//    try (FileOutputStream sout = new FileOutputStream(PATH_TXT);
//         PrintWriter test = new PrintWriter(PATH_TXT);
//         BufferedReader b64 = new BufferedReader(new FileReader(PATH_TXT))) {
//      sout.write(salt1);
////      String salt1s = new BASE64Encoder().encode(salt); //DatatypeConverter.printBase64Binary(salt1);
////      String s1s =
//      System.out.println("file encode bytes: " + Arrays.toString((new FileReader(PATH_TXT)).getEncoding().getBytes()));
//      String salt1Read = b64.readLine();
//      System.out.println("br readline: " + salt1Read);
//      assertEquals(new String(salt1), salt1Read);
//      System.out.println("br readline getbytes: " + Arrays.toString(salt1Read.getBytes()));
////      assertEquals(salt1, salt1Read.getBytes());
////      test.write(salt1.toString() + "\n");
////      test.write(Arrays.toString(salt1) + "\n");
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    assertNotEquals(salt1, salt2);
//    assertNotEquals(salt1, salt3);
//    assertNotEquals(salt2, salt3);
//  }
////
////  @Test
////  public void simpleHashTest() throws AccountException {
////    byte[] salt = AccountsOld.generateSalt();
////    String pass = "password";
////    int hash = AccountsOld.simpleHash(pass, salt);
////    System.out.println(hash);
////    // check that hashing the same password and salt gives the same hash
////    assertEquals(hash, AccountsOld.simpleHash(pass, salt));
////    // check that differences in pass, salt => different hash
////    assertNotEquals(hash, AccountsOld.simpleHash(pass, AccountsOld.generateSalt()));
////    assertNotEquals(hash, AccountsOld.simpleHash("passw0rd", salt));
////    assertNotEquals(hash, AccountsOld.simpleHash("Password", salt));
////    assertNotEquals(hash, AccountsOld.simpleHash("wordpass", salt));
////  }
//
////  @Test
////  public void hashPasswordPBKDF2Test() throws AccountException {
////    byte[] salt = AccountsOld.generateSalt();
////    String pass = "password";
////    byte[] hash = AccountsOld.hashPasswordPBKDF2(pass, salt);
////    System.out.println(hash);
////    // check that hashing the same password and salt gives the same hash
////    assertEquals(hash, AccountsOld.hashPasswordPBKDF2(pass, salt));
////    // check that differences in pass, salt => different hash
////    assertNotEquals(hash, AccountsOld.hashPasswordPBKDF2(pass));
////    assertNotEquals(hash, AccountsOld.hashPasswordPBKDF2(pass, AccountsOld.generateSalt()));
////    assertNotEquals(hash, AccountsOld.hashPasswordPBKDF2("passw0rd", salt));
////    assertNotEquals(hash, AccountsOld.hashPasswordPBKDF2("Password", salt));
////    assertNotEquals(hash, AccountsOld.hashPasswordPBKDF2("wordpass", salt));
////  }
//
//  @Test
//  public void writeLoginInfoTest() throws AccountException {
//    String user = "user";
//    String pass = "pass";
//    byte[] salt = AccountsOld.generateSalt();
//    byte[] hash = AccountsOld.hashPassword(pass, salt);
//
//    assertEquals(user, new String(user.getBytes()));
//    assertNotEquals(user.getBytes(), new String(user.getBytes()).getBytes());
//    assertNotEquals(salt, new String(salt).getBytes());
//
//    assertEquals(user, Base64.getEncoder().encodeToString(Base64.getDecoder().decode(user)));
//    assertNotEquals(user, Base64.getEncoder().encodeToString(user.getBytes()));
//    assertNotEquals(salt, Base64.getDecoder().decode(Base64.getEncoder().encodeToString(salt)));
//
//    new AccountsOld() {
//      public void callProtectedMethod(String user, String pass, byte[] salt, String path) throws AccountException {
//        writeLoginInfo(user, pass, salt, path);
//      }
//    }.callProtectedMethod(user, pass, salt, PATH_TXT);
//
//    try (BufferedReader br = new BufferedReader(new FileReader(PATH_TXT));
//         FileInputStream is = new FileInputStream(PATH_TXT)) {
//      String[] info = br.readLine().split(",");
//      assertEquals(info[0], user);
//      assertNotEquals(info[2].getBytes(), salt);
////      assertNotEquals(Base64.getDecoder().decode(info[2]), salt);
//      assertNotEquals(info[1].getBytes(), hash);
//      is.read();
//
//      assertNotEquals(info[1].getBytes(), AccountsOld.hashPassword(pass, info[2].getBytes()));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void writeLoginInfoBCryptTest() throws AccountException {
//    String user = "user";
//    String pass = "pass";
//    String salt = BCrypt.gensalt();
//    String hash = BCrypt.hashpw(pass, salt);
//    assertTrue(BCrypt.checkpw(pass, hash));
//    // access protected write info method
//    new AccountsOld() {
//      public void callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
//        writeLoginInfo(user, pass, salt, path);
//      }
//    }.callProtectedMethod(user, pass, salt, PATH_CSV);
//    // check the that written in things match what was created and
//    try (BufferedReader br = new BufferedReader(new FileReader(PATH_CSV))) {
//      String[] info = br.readLine().split(",");
//      assertEquals(user, info[0]);
//      assertEquals(salt, info[2]);
//      // the password can be recreated w/ the salt
//      assertEquals(BCrypt.hashpw(pass, info[2]), info[1]);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void checkLoginTest() throws AccountException {
//    String user = "user";
//    String pass = "pass";
//    String salt = BCrypt.gensalt();
//    String hash = BCrypt.hashpw(pass, salt);
//    assertTrue(BCrypt.checkpw(pass, hash));
//
//    new AccountsOld() {
//      public AccountsOld callProtectedMethod(String user, String pass, String salt, String path) throws AccountException {
//        writeLoginInfo(user, pass, salt, path);
//        return this;
//      }
//    }.callProtectedMethod(user, pass, salt, PATH_CSV);
//
//    assertEquals("logged in!", AccountsOld.checkLogin(user, pass, PATH_CSV));
//    assertEquals("login failed", AccountsOld.checkLogin("fake user", pass, PATH_CSV));
//    assertEquals("login failed", AccountsOld.checkLogin(user, "fake pass", PATH_CSV));
//    assertEquals("login failed", AccountsOld.checkLogin("fake user", "fake pass", PATH_CSV));
//  }
//}
