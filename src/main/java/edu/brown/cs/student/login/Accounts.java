package edu.brown.cs.student.login;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Static utility class that deals with login generation, storing, and attempts for all Accounts.
 */
public class Accounts {
  private static final int MIN_PASS_LENGTH = 6;
  private static final int MAX_LENGTH = 32;
  private static final String LOGIN_INFO_PATH = "src/main/resources/login/account-login-info.csv";

  private static Map<String, User> nameUserMap;

  /**
   * static utility class, don't instantiate except for testing.
   */
  protected Accounts() { }

  /**
   * get a specific User.
   * @param username - name
   * @return the User, null if they don't exist
   */
  public static User getUser(String username) throws AccountException {
    User user = nameUserMap.get(username);
    if (user == null) {
      throw new AccountException("no user found with name " + username);
    } else {
      return user;
    }
  }

  protected static void addUserMap(User user) throws AccountException {
    if (nameUserMap == null) {
      throw new AccountException("ERROR: name-user map not initialized");
    } else {
      nameUserMap.put(user.getUsername(), user);
      for (String k : nameUserMap.keySet()) {
        System.out.println("USER " + k + " done");
      }
      System.out.println("ADDING USER " + nameUserMap.get(user.getUsername()) + " finished");
    }
  }

  /**
   * adds previously added users in the UserDatabase to the current process' nameUser map;
   * to be called at the start of running application.
   * @throws AccountException on file UserDatabase failure
   */
  public static void initializeMap() throws AccountException, 
      FileNotFoundException, ClassNotFoundException, SQLException {
    
    UserDatabase.loadDatabase("data/userDatabase.sqlite3");
    initializeMap(LOGIN_INFO_PATH);
  }
  /*
   * implementation
   */
  protected static void initializeMap(String path) throws AccountException {
    nameUserMap = new HashMap<>();
    // create users from info files and databases
    try (Scanner loginInfo = new Scanner(new FileReader(path))) {
      // create each user
      System.out.println("GETS HERE");
      while (loginInfo.hasNext()) {
        System.out.println("Line 1");
        String[] login = loginInfo.nextLine().split(",");
        System.out.println("Line 2");
        String username = login[0];
        System.out.println("Line 3 " + username);
        User user = UserDatabase.getUser(username);
        System.out.println("Line 4");
        nameUserMap.putIfAbsent(username, user);
        System.out.println("Line 5");
      }
    } catch (Exception e) {
      throw new AccountException(e.getMessage());
    }
  }

  /**
   * reads the first line of the csv, for testing / checking files for proper format.
   * @return the header
   * @throws AccountException on file error
   */
  public static String readHeader() throws AccountException {
    return readHeader(LOGIN_INFO_PATH);
  }
  /*
   * implementation
   */
  protected static String readHeader(String path) throws AccountException {
    try (Scanner loginInfo = new Scanner(new FileReader(path))) {
      if (loginInfo.hasNext()) {
        return loginInfo.next();
      } else {
        throw new AccountException("header of login info file does not exist");
      }
    } catch (FileNotFoundException e) {
      throw new AccountException("login info file does not exist");
    }
  }

  /**
   * checks if the first line of the given file matches the desired header.
   * @throws AccountException if the header is malformed
   */
  public static void checkHeader() throws AccountException {
    checkHeader(LOGIN_INFO_PATH);
  }
  /*
   * implementation
   */
  public static void checkHeader(String path) throws AccountException {
    if (!readHeader(path).equals("username,passwordHash,salt")) {
      throw new AccountException("login CSV header malformed");
    }
  }


  /**
   * Stores a user's login info securely by encoding the password using salt hashing. Writes user,
   * password hash, salt to a csv.
   * @param user username
   * @param pass password
   * @throws AccountException on write error
   */
  protected static void writeLoginInfo(String user, String pass) throws AccountException {
    writeLoginInfo(user, pass, BCrypt.gensalt(), LOGIN_INFO_PATH);
  }
  /*
   * implementation
   */
  protected static void writeLoginInfo(String user, String pass, String salt, String path) throws
          AccountException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
      String hash = BCrypt.hashpw(pass, salt);
      writer.write("\n" + user + "," + hash + "," + salt);
    } catch (IOException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
  /*
   * for testing
   */
  protected static void writeLoginInfo(String user, String pass, String path) throws
          AccountException {
    writeLoginInfo(user, pass, BCrypt.gensalt(), path);
  }

  /**
   * checks if a username and login pair are stored in the csv (user exists).
   * @param inpUser username
   * @param inpPass password
   * @return true if can login, false else
   * @throws AccountException for file errors
   */
  public static boolean checkLogin(String inpUser, String inpPass) throws AccountException {
    return checkLogin(inpUser, inpPass, LOGIN_INFO_PATH);
  }
  /*
   * for repl, reads in username and password from user keyboard (System.in)
   */
  public static boolean checkLogin() throws AccountException {
    try (Scanner keyboard = new Scanner(System.in)) {
      // get input from user
      System.out.println("Username: ");
      String inpUser = keyboard.nextLine();
      System.out.println("Password: ");
      String inpPass = keyboard.nextLine();
      return checkLogin(inpUser, inpPass, LOGIN_INFO_PATH);
    }
  }
  /*
   * implementation
   */
  public static boolean checkLogin(String inpUser, String inpPass, String path) throws
          AccountException {
    try (Scanner loginInfo = new Scanner(new FileReader(path))) {
      String[] login;
      String user;
      String passHash;
      String salt;
      // check each entry of login info csv to see if there's a match
      while (loginInfo.hasNext()) {
        login = loginInfo.nextLine().split(",");
        if (login.length != 3) {
          throw new AccountException("CSV malformed. All lines must adhere to the structure "
                  + "\"username,passwordHash,salt\" and the file must not end in a newline");
        }
        user = login[0];
        passHash = login[1];
        salt = login[2];
        // check if user and pass match. BCrypt.checkpw(inpPass, passHash)
        if (user.equals(inpUser) && passHash.equals(BCrypt.hashpw(inpPass, salt))) {
          return true;
        }
      }
    } catch (FileNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    // none of the lines fit
    return false;
  }

  /**
   * handles logging and getting user specific data.
   * @param user username
   * @return text output
   */
  public static String login(String user) {
    // give access to data of user for future commands
    return user + " successfully Logged in!";
  }

  /**
   * Method for signup validity.
   * @param user - username
   * @param pass1 - password
   * @param pass2 - password
   * @return boolean for conditions
   */
  public static boolean checkSignUpValidity(String user, String pass1, String pass2) throws
          UserCreationException {
    return acceptableUserLength(user) && userExists(user) && acceptablePasswordLength(pass1)
            && comparePasswords(pass1, pass2);
  }

  /**
   * checks if username length is on [1,32].
   * @param user - username
   * @return bool
   * @throws UserCreationException if false
   */
  private static boolean acceptableUserLength(String user) throws UserCreationException {
    if (1 <= user.length() && user.length() <= MAX_LENGTH) {
      return true;
    } else {
      throw new UserCreationException("ERROR: username length must be on [1,32]");
    }
  }

  /**
   * checks if username is taken.
   * @param user - username
   * @return bool
   * @throws UserCreationException if false
   */
  private static boolean userExists(String user) throws UserCreationException {
    System.out.println("USER EXISTS " + nameUserMap.containsKey(user));
    System.out.println("USER VAL " + nameUserMap.get(user));
    if (!nameUserMap.containsKey(user)) {
      return true;
    } else {
      throw new UserCreationException("ERROR: username already taken");
    }
  }

  /**
   * checks if password length is on [6,32].
   * @param pass - password
   * @return bool
   * @throws UserCreationException if false
   */
  private static boolean acceptablePasswordLength(String pass) throws UserCreationException {
    if (MIN_PASS_LENGTH <= pass.length() && pass.length() <= MAX_LENGTH) {
      return true;
    } else {
      throw new UserCreationException("ERROR: password length must be on [6,32]");
    }
  }

  /**
   * checks if passwords match.
   * @param pass1 - password
   * @param pass2 - password
   * @return bool
   * @throws UserCreationException if false
   */
  private static boolean comparePasswords(String pass1, String pass2) throws UserCreationException {
    if (pass1.equals(pass2)) {
      return true;
    } else {
      throw new UserCreationException("ERROR: passwords do not match");
    }
  }
}
