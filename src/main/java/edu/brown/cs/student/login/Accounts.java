package edu.brown.cs.student.login;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
//import org.springframework.security.crypto.password;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * Static utility class that deals with login generation, storing, and attempts for all Accounts.
 */
public final class Accounts {
  private static final Random RANDOM = new SecureRandom();
  private static final int ITERATIONS = 10000;
  private static final int KEY_LENGTH = 256;
  private static final int SALT_LENGTH = 16;
//  private static PasswordEncoder passwordEncoder = null;

  //TODO: is this okay?
  private static final String LOGIN_INFO_PATH = "src/main/resources/login/account-login-info.csv";
//  private static File loginInfoFile;
//  private static Scanner loginInfo;
//  private static PrintWriter writer;
//
//  static {
//    try (Scanner loginInfo = new Scanner(new FileReader(LOGIN_INFO_PATH));
//         PrintWriter writer = new PrintWriter(new File(LOGIN_INFO_PATH))) { } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    }

//    try {
//      loginInfo = new Scanner(loginInfoFile);
//    } catch (FileNotFoundException e) {
//      System.err.println(e.getStackTrace());
//      System.err.println("login info file was not found");
//    }
//
//    try {
//      writer = new PrintWriter(Accounts.getLoginInfoFile());
//    } catch (FileNotFoundException e) {
//      System.err.println(e.getStackTrace());
//      System.err.println("login info file corrupted");
//    }
//  }
  // Accounts.class.getClassLoader().getResource(loginInfoPath).getFile()
  // Accounts.class.getResourceAsStream(loginInfoPath)
  // new File(loginInfoFile)

  /**
   * static utility class.
   */
  private Accounts() { }

//  protected static File getLoginInfoFile() {
//    return loginInfoFile;
//  }

  public static String readHeader() throws AccountException {
    try (Scanner loginInfo = new Scanner(new FileReader(LOGIN_INFO_PATH))) {
      if (loginInfo.hasNext()) {
        return loginInfo.next();
      } else {
        throw new AccountException("header of login info file does not exist");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    throw new AccountException("login info file does not exist");
  }

  // handles salt-hashing for storing password when creating users
  // https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
  /**
   * Returns a random salt to be used to hash a password.
   *
   * @return a 16 bytes random salt
   */
  public static byte[] generateSalt() {
    byte[] salt = new byte[SALT_LENGTH];
    RANDOM.nextBytes(salt);
    return salt;
  }

  public static byte[] hashPassword(String rawPassword, byte[] salt) {
    return simpleHash(rawPassword, salt);
  }

  /**
   * basic hash coding algorithm. mainly for testing; not necessarily secure.
   * @param rawPassword pass
   * @param salt salt
   * @return hash as String
   */
  public static byte[] simpleHash(String rawPassword, byte[] salt) {
    int res = 0;
    res = res * 31 + rawPassword.hashCode();
    res = res * 31 + salt.hashCode();
    return null;
  }

  /**
   * PBKDF2 password encoding using salting and hashing.
   * @param rawPassword - input password string
   * @return the hashed password as a byte array
   * @throws AccountException
   */
  public static byte[] hashPasswordPBKDF2(String rawPassword, byte[] salt) throws AccountException {
    // when first hashing a new password, generate a random salt for it.
    // Otherwise, use the given salt stored with the password hash.
    KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
    // hash the password using the salt
    SecretKeyFactory factory;
    byte[] hashedPassword;
    try {
      factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      hashedPassword = factory.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new AccountException(e.getMessage(), e);
    }

    return hashedPassword;
  }

  /**
   * PBKDF2 password encoding using salting and hashing.
   * @param rawPassword
   * @return the hashed password as a byte array
   * @throws AccountException
   */
  public static byte[] hashPasswordPBKDF2(String rawPassword) throws AccountException {
    byte[] salt = generateSalt();
    return hashPasswordPBKDF2(rawPassword, salt);
  }


  // Store w/cookies, database, or hashmap

  protected static void writeLoginInfo(String user, String pass) throws AccountException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(LOGIN_INFO_PATH))) {
      byte[] salt = generateSalt();
      byte[] hash = hashPassword(pass, salt);
      writer.write(user + "," + hash + "," + salt);
    } catch (IOException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
  
  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public static String checkLogin() throws AccountException {
    try (Scanner keyboard = new Scanner(System.in);
         Scanner loginInfo = new Scanner(new FileReader(LOGIN_INFO_PATH))) {
      // get input from user
      System.out.println("Username: ");
      String inpUser = keyboard.nextLine();
      System.out.println("Password: ");
      String inpPass = keyboard.nextLine();

      String[] login;
      String user;
      String passHash;
      byte[] salt;

      while (loginInfo.hasNext()) {
        login = loginInfo.nextLine().split(",");
        user = login[0];
        passHash = login[1];
        salt = login[2].getBytes();

        if (user.equals(inpUser) && passHash.equals(hashPasswordPBKDF2(inpPass, salt))) {
          return login(user, passHash);
        } else {
          return "login failed; please try again";
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    throw new AccountException("ERROR: reached end of checkLogin");
  }

  public static String login(String user, String pass) {
    // give access to data of user for future commands
    return "";
  }
}
