package edu.brown.cs.student.foodCOMA.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import org.junit.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.Assert.*;

public class AccountsTest {

  @Test
  public void readHeaderTest() throws AccountException {
    assertEquals(Accounts.readHeader(), "username,encodedPassword,salt");
  }

  @Test
  public void generateSaltTest() {
    byte[] salt1 = Accounts.generateSalt();
    byte[] salt2 = Accounts.generateSalt();
    byte[] salt3 = Accounts.generateSalt();
    System.out.println("str(s): " + new String(salt1));
    System.out.println("salt: " + salt1);
    System.out.println("salt tostr: " + salt1.toString());
    System.out.println("salt arr tostr: " + Arrays.toString(salt1));
    //Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for(int i=0; i< salt1.length ;i++)
    {
      sb.append(Integer.toString((salt1[i] & 0xff) + 0x100, 16).substring(1));
    }
    System.out.println("hex salt: " + sb.toString());
    try (FileOutputStream sout = new FileOutputStream("test.txt");
         PrintWriter test = new PrintWriter("test.txt");
         BufferedReader b64 = new BufferedReader(new FileReader("test.txt"))) {
      sout.write(salt1);
//      String salt1s = new BASE64Encoder().encode(salt); //DatatypeConverter.printBase64Binary(salt1);
//      String s1s =
      System.out.println("file encode bytes: " + (new FileReader("test.txt")).getEncoding().getBytes());
      String salt1Read = b64.readLine();
      System.out.println("br readline: " + salt1Read);
      System.out.println("br readline getbytes: " + salt1Read.getBytes());
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
}
