package edu.brown.cs.student.login;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
//import org.springframework.security.crypto.password;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
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
public class AccountsOld {
  private static final Random RANDOM = new SecureRandom();
  private static final int ITERATIONS = 10000;
  private static final int KEY_LENGTH = 256;
  private static final int SALT_LENGTH = 16;
  private static final String LOGIN_INFO_PATH = "src/main/resources/login/account-login-info.csv";
//  private static PasswordEncoder passwordEncoder = null;

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
   * static utility class, don't instantiate except for testing.
   */
  protected AccountsOld() { }

//  protected static File getLoginInfoFile() {
//    return loginInfoFile;
//  }

  /**
   * reads the first line of the csv, for testing / checking files for proper format.
   * @return
   * @throws AccountException
   */
  public static String readHeader() throws AccountException {
    return readHeader(LOGIN_INFO_PATH);
  }

  public static String readHeader(String path) throws AccountException {
    try (Scanner loginInfo = new Scanner(new FileReader(path))) {
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

  /**
   * Stores a user's login info securely by encoding the password using salt hashing. Writes user,
   * password hash, salt to a csv.
   * @param user
   * @param pass
   * @param path
   * @throws AccountException
   */
  protected static void writeLoginInfo(String user, String pass, String salt, String path) throws AccountException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      String hash = BCrypt.hashpw(pass, salt);
      writer.write(user + "," + hash + "," + salt);
    } catch (IOException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }

  protected static void writeLoginInfo(String user, String pass, String path) throws AccountException {
    try {
      writeLoginInfo(user, pass, BCrypt.gensalt(), path);
    } catch (AccountException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }

  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public static String checkLogin() throws AccountException {
    try (Scanner keyboard = new Scanner(System.in)) {
      // get input from user
      System.out.println("Username: ");
      String inpUser = keyboard.nextLine();
      System.out.println("Password: ");
      String inpPass = keyboard.nextLine();
      return checkLogin(inpUser, inpPass, LOGIN_INFO_PATH);
    } //catch (AccountException e) {
//      throw new AccountException(e.getMessage(), e);
//    }
  }

  public static String checkLogin(String inpUser, String inpPass) throws AccountException {
    return checkLogin(inpUser, inpPass, LOGIN_INFO_PATH);
  }

  /**
   * checks if a username and login pair are stored in the csv (user exists).
   * @param inpUser username
   * @param inpPass password
   * @param path optional login info csv path, for testing.
   * @return text output for repl/gui
   * @throws AccountException for file errors
   */
  public static String checkLogin(String inpUser, String inpPass, String path) throws AccountException {
    try (Scanner loginInfo = new Scanner(new FileReader(path))) {
      String[] login;
      String user;
      String passHash;
      String salt;
      // check each entry of login info csv to see if there's a match
      while (loginInfo.hasNext()) {
        login = loginInfo.nextLine().split(",");
        user = login[0];
        passHash = login[1];
        salt = login[2];
        // check if user and pass match
        if (user.equals(inpUser) && BCrypt.checkpw(inpPass, passHash)) {
          return login(user);
        } else {
          return "login failed";
        }
      }
    } catch (FileNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    // should not be reached.
    throw new AccountException("ERROR: reached end of checkLogin");
  }

  /**
   * handles logging and getting user specific data.
   * @param user username
   * @return text output
   */
  public static String login(String user) {
    // give access to data of user for future commands
    return "logged in!";
  }


  // NON BCRYPT //

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

  public static byte[] hashPassword(String pass, byte[] salt) {
    return simpleHash(pass, salt);
  }

  /**
   * basic hash coding algorithm. mainly for testing; not necessarily secure.
   * @param rawPassword pass
   * @param salt salt
   * @return hash as String
   */
  public static byte[] simpleHash(String rawPassword, byte[] salt) {
    int res = 0;
    final int num = 31;
    res = res * num + rawPassword.hashCode();
    res = res * num + Arrays.hashCode(salt);
    return BigInteger.valueOf(res).toByteArray();
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

  protected static void writeLoginInfo(String user, String pass, byte[] salt, String path) throws AccountException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      byte[] hash = hashPassword(pass, salt);
      writer.write(user + "," + new String(hash) + "," + new String(salt));
    } catch (IOException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
  //
  protected static void writeLoginInfo(String user, String pass) throws AccountException {
    try {
      writeLoginInfo(user, pass, generateSalt(), LOGIN_INFO_PATH);
    } catch (AccountException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
  //
  protected static void writeLoginInfo(String user, String pass, byte[] salt) throws AccountException {
    try {
      writeLoginInfo(user, pass, salt, LOGIN_INFO_PATH);
    } catch (AccountException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
}
