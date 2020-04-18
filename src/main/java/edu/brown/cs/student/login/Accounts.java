package edu.brown.cs.student.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * Class comment.
 *
 */
public final class Accounts {
  private static final Random RANDOM = new SecureRandom();
  private static final int ITERATIONS = 10000;
  private static final int KEY_LENGTH = 256;

  private static final String loginInfoPath = "src/main/resources/login/account-login-info.csv";
  private static final File loginInfoFile =
          new File(Accounts.class.getClassLoader().getResource(loginInfoPath).getFile());
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
  // new File(loginInfoFile) //TODO: is this okay?

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
  public static byte[] getNextSalt() {
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return salt;
  }


  // Store w/cookies, database, or hashmap
  
  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public static String naiveCheckLogin() throws LoginException {
    Scanner keyboard = new Scanner (System.in);
    // get input from user
    System.out.println("Username: ");
    String inpUser = keyboard.nextLine();
    System.out.println("Password: ");
    String inpPass = keyboard.nextLine();
    Scanner scan;


    String[] login;
    String user;
    String pass;
    String salt;
    while (Accounts.loginInfo.hasNext()) {
      login = Accounts.loginInfo.nextLine().split(",");
      user = login[0];
      pass = login[1];
      salt = login[2];

      if (inpUser.equals(user) && inpPass.equals(pass)) {
        return login(user, pass);
      } else {
        return "login failed; please try again";
      }
    }

    throw new LoginException("ERROR: reached end of checkLogin");
  }

  public static String login(String user, String pass) {
    // give access to data of user for future commands
    return "";
  }
  

}