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
  
  
  public Accounts() {
    
  }
  
  // Store w/cookies, database, or hashmap
  
  // Validate method, which affirms that the given password and username are valid and match.
  // https://stackoverflow.com/questions/16627910/how-to-code-a-very-simple-login-system-with-java
  public void checkLogin() {
    Scanner keyboard = new Scanner (System.in);
    // get input from user
    System.out.println("Username: ");
    String inpUser = keyboard.nextLine();
    System.out.println("Password: ");
    String inpPass = keyboard.nextLine();
    Scanner scan = null;
    try {
      scan = new Scanner(new File("the\\dir\\myFile.extension"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    String[] account = new String[2];
    String user;
    String pass;
    while (scan.hasNext()) {
      account = scan.nextLine().split(",");
      user = account[0];
      pass = account[1];

      if (inpUser.equals(user) && inpPass.equals(pass)) {
        
      }
    }

    if (inpUser.equals(user) && inpPass.equals(pass)) {
      System.out.print("your login message");
    } else {
      System.out.print("your error message");
    }
  }
  

}