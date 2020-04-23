
package edu.brown.cs.student.foodCOMA.login;

import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.AccountsOld;
import edu.brown.cs.student.login.BCrypt;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.*;

public class AccountsOldTest {
  private static final String PATH_TXT = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.txt";
  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/foodCOMA/login/test.csv";

  @Test
  public void readHeaderTest() throws AccountException {
//    assertEquals(AccountsOld.readHeader(), "username,encodedPassword,salt");
  }

  @Test
  public void generateSaltTest() {
    byte[] salt1 = AccountsOld.generateSalt();
    byte[] salt2 = AccountsOld.generateSalt();
    byte[] salt3 = AccountsOld.generateSalt();
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
    try (FileOutputStream sout = new FileOutputStream(PATH_TXT);
         PrintWriter test = new PrintWriter(PATH_TXT);
         BufferedReader b64 = new BufferedReader(new FileReader(PATH_TXT))) {
      sout.write(salt1);
//      String salt1s = new BASE64Encoder().encode(salt); //DatatypeConverter.printBase64Binary(salt1);
//      String s1s =
      System.out.println("file encode bytes: " + Arrays.toString((new FileReader(PATH_TXT)).getEncoding().getBytes()));
      String salt1Read = b64.readLine();
      System.out.println("br readline: " + salt1Read);
      assertEquals(new String(salt1), salt1Read);
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
}