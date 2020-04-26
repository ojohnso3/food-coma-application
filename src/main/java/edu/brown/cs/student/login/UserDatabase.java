package edu.brown.cs.student.login;

import com.google.common.io.Files;
import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.food.Recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains all interactions with the userDatabase file.
 */
public final class UserDatabase {

  private static Connection conn;

  private UserDatabase() { }

  /**
   * Loads in database.
   * @param fileName - name of the database file.
   * @throws FileNotFoundException - thrown if the given filepath is not a valid .sqlite3 file.
   * @throws ClassNotFoundException - thrown if there is an error when connecting to the file.
   * @throws SQLException - thrown if there is an error when connecting to the file.
   */
  public static void loadDatabase(String fileName) throws FileNotFoundException, SQLException, ClassNotFoundException {
    String ext = Files.getFileExtension(fileName);
    if (!ext.equals("sqlite3")) {
      throw new FileNotFoundException("ERROR: File must be .sqlite3!");
    }

    File f = new File(fileName);
    if (!f.exists()) {
      throw new FileNotFoundException("ERROR: File does not exist");
    }
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + fileName;

    conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    createTables(stat);
  }

  /**
   * Function to create the tables in the database file.
   * @param stat -
   */
  private static void createTables(Statement stat) throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS account("
        + "username TEXT,"
        + "PRIMARY KEY (username));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS prev_recipe("
        + "username TEXT,"
        + "recipe_uri TEXT,"
        + "FOREIGN KEY (username) REFERENCES account(username));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS restriction("
        + "username TEXT,"
        + "health_label TEXT,"
        + "FOREIGN KEY (username) REFERENCES account(username));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS nutrient("
        + "username TEXT,"
        + "code TEXT,"
        + "FOREIGN KEY (username) REFERENCES account(username));";
    stat.executeUpdate(sql);
  }

  /**
   * Function to check if a given username is already in the database.
   * @param username - the username to check for in the database.
   * @return - boolean representing whether the username is in the database.
   */
  public static boolean checkRepeatUsername(String username) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
    prep.setString(1, username);
    ResultSet userSet = prep.executeQuery();
    return userSet.next();
  }


  /**
   * Function to insert a User object into the database.
   * @param user - a User object to be inserted.
   */
  public static void insertUser(User user) throws SQLException, AccountException {

    if (checkRepeatUsername(user.getUsername())) {
      throw new AccountException("Username not available");
    }

    PreparedStatement prep = conn.prepareStatement("INSERT INTO account VALUES("
        + "\"" + user.getUsername() + "\");");
    prep.executeUpdate();

    PreparedStatement prep;
    for (String s : user.getDietaryRestrictions()) {
      prep = conn.prepareStatement("INSERT INTO restriction VALUES(\"" + user.getUsername()
          + "\",\"" + s + "\");");
      prep.executeUpdate();
    }

    for (String s : user.getNutrients()) {
      prep = conn.prepareStatement("INSERT INTO nutrient VALUES(\"" + user.getUsername()
          + "\",\"" + s + "\");");
      prep.executeUpdate();
    }

    for (Recipe r : user.getPreviousRecipes()) {
      prep = conn.prepareStatement("INSERT INTO prev_recipe VALUES(\"" + user.getUsername()
          + "\",\"" + r.getUri() + "\");");
      prep.executeUpdate();
    }
  }

  /**
   * Database test function.
   */
  public static void testDatabaseFile() {
    try {
      User user = new User("hello");
      List<String> nutrients = new ArrayList<>();
      nutrients.add("A");
      nutrients.add("B");
      user.setNutrients(nutrients);
      user.addToRestrictions("A");
      user.addToRestrictions("B");
      Recipe r = new Recipe("uri");
      user.addToPreviousRecipes(r);
      insertUser(user);
    } catch (SQLException | AccountException e) {
      e.printStackTrace();
    }
  }


}
