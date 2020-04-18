package edu.brown.cs.student.login;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
//import org.springframework.security.crypto.password;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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
  private static File loginInfoFile =
          new File(Objects.requireNonNull(
                  Accounts.class.getClassLoader().getResource(LOGIN_INFO_PATH)).getFile());
  private static Scanner loginInfo;

  static {
    try {
      loginInfo = new Scanner(loginInfoFile);
    } catch (FileNotFoundException e) {
      System.err.println("login info file was not found");
    }
  }
  // Accounts.class.getClassLoader().getResource(loginInfoPath).getFile()
  // Accounts.class.getResourceAsStream(loginInfoPath)
  // new File(loginInfoFile)

  /**
   * static utility class.
   */
  private Accounts() { }

  protected static File getLoginInfoFile() {
    return loginInfoFile;
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
  
  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public static String checkLogin() throws AccountException {
    Scanner keyboard = new Scanner (System.in);
    // get input from user
    System.out.println("Username: ");
    String inpUser = keyboard.nextLine();
    System.out.println("Password: ");
    String inpPass = keyboard.nextLine();

    String[] login;
    String user;
    String passHash;
    byte[] salt;
    while (Accounts.loginInfo.hasNext()) {
      login = Accounts.loginInfo.nextLine().split(",");
      user = login[0];
      passHash = login[1];
      salt = login[2].getBytes();
      System.out.println(salt); //TODO:

      if (user.equals(inpUser) && passHash.equals(hashPasswordPBKDF2(inpPass, salt))) {
        return login(user, passHash);
      } else {
        return "login failed; please try again";
      }
    }

    throw new AccountException("ERROR: reached end of checkLogin");
  }

  public static String login(String user, String pass) {
    // give access to data of user for future commands
    return "";
  }
}
