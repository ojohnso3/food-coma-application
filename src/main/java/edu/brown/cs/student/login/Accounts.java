package edu.brown.cs.student.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * Class comment.
 *
 */
public class Accounts {
  private File accounts;
  
  
  public Accounts(File accounts) {
    this.accounts = accounts;
  }
  // Store w/cookies, database, or hashmap
  
  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public String naiveCheckLogin() throws LoginException {
    Scanner keyboard = new Scanner (System.in);
    // get input from user
    System.out.println("Username: ");
    String inpUser = keyboard.nextLine();
    System.out.println("Password: ");
    String inpPass = keyboard.nextLine();
    Scanner scan;
    try {
      scan = new Scanner(new File("the\\dir\\myFile.extension"));
    } catch (FileNotFoundException e) {
      throw new LoginException("ERROR: checkLogin: account user/pass file not found", e);
    }

    String[] account;
    String user;
    String pass;
    while (scan.hasNext()) {
      account = scan.nextLine().split(",");
      user = account[0];
      pass = account[1];

      if (inpUser.equals(user) && inpPass.equals(pass)) {
        return login(user, pass);
      } else {
        return "login failed; please try again";
      }
    }

    throw new LoginException("ERROR: reached end of checkLogin");
  }

  public String login(String user, String pass) {
    // give access to data of user for future commands
    return "";
  }
  

}