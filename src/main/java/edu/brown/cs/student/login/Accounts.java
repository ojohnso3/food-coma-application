package edu.brown.cs.student.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * Class comment.
 *
 */
public final class Accounts {
  private static final String loginInfoFile = "data/account-login-info.csv";
  private static Scanner loginInfo;

  static {
    try {
      loginInfo = new Scanner(new File(loginInfoFile));
    } catch (FileNotFoundException e) { //TODO: is this okay?
      System.err.println(e.getCause());
      System.exit(1);
    }
  }

  /**
   * constructor.
   * @param loginInfo - File with login information
   * @throws LoginException - if the file doesn't exist
   */
  private Accounts(File loginInfo) throws LoginException {
    try {
      this.loginInfo = new Scanner(loginInfo);
    } catch (FileNotFoundException e) {
      throw new LoginException("ERROR: checkLogin: account user/pass file not found", e);
    }
  }

  public void initializeAccounts() {

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