package edu.brown.cs.student.login;

import com.google.common.io.Files;
import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.RecipeDatabase;
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
  public static void loadDatabase(String fileName) throws FileNotFoundException, SQLException,
      ClassNotFoundException {
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
  public static boolean checkUsername(String username) throws SQLException {
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

    if (checkUsername(user.getUsername())) {
      throw new AccountException("Username not available");
    }

    PreparedStatement prep = conn.prepareStatement("INSERT INTO account VALUES("
        + "\"" + user.getUsername() + "\");");
    prep.executeUpdate();
  }

  /**
   * Function to insert to the restriction table.
   * @param username - the username to add the restriction to.
   * @param label - the label of the restriction.
   * @throws SQLException
   */
  public static void insertToRestriction(String username, String label) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO restriction VALUES(\"" + username
        + "\",\"" + label + "\");");
    prep.executeUpdate();
  }

  /**
   * Function to insert to the prev_recipe table.
   * @param username - the username to add the recipe to.
   * @param uri - the uri of the previous Recipe.
   */
  public static void insertToPrevRecipe(String username, String uri) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO prev_recipe VALUES(\"" + username
        + "\",\"" + uri + "\");");
    prep.executeUpdate();
  }

  /**
   * Function to insert to the nutrients table.
   * @param username - the username to add the recipe to.
   * @param code - the uri of the previous Recipe.
   */
  public static void insertToNutrients(String username, String code) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO nutrient VALUES(\"" + username
        + "\",\"" + code + "\");");
    prep.executeUpdate();
  }

  /**
   * Function to convert a ResultSet to a List.
   * @param rs - ResultSet to convert.
   * @return - the List representation of the ResultSet.
   */
  private static List<String> setToList(ResultSet rs) throws SQLException {
    List<String> toReturn = new ArrayList<>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    return toReturn;
  }

  /**
   * Function to retrieve a User object from the database.
   * @param username - the username of the desired User.
   * @return - the User object that was retrieved from the database.
   */
  public static User getUser(String username) throws SQLException, InterruptedException,
      IOException, APIException, AccountException {
    if (!checkUsername(username)) {
      throw new AccountException("Cannot retrieve user: user does not exist");
    }

    PreparedStatement prep = conn.prepareStatement("SELECT health_label FROM restriction"
            + " WHERE username = ?");
    prep.setString(1, username);
    ResultSet restrictionSet = prep.executeQuery();
    List<String> dietaryRestrictions = setToList(restrictionSet);

    prep = conn.prepareStatement("SELECT code FROM nutrient WHERE username = ?");
    prep.setString(1, username);
    ResultSet nutrientSet = prep.executeQuery();
    List<String> nutrients = setToList(nutrientSet);

    prep = conn.prepareStatement("SELECT recipe_uri FROM prev_recipe WHERE username = ?");
    prep.setString(1, username);
    ResultSet recipeSet = prep.executeQuery();

    List<Recipe> prevRecipes = new ArrayList<>();
    while (recipeSet.next()) {
      Recipe r = RecipeDatabase.getRecipeFromURI(recipeSet.getString(1));
      prevRecipes.add(r);
    }

    return new User(username, prevRecipes, dietaryRestrictions, nutrients);
  }

  /**
   * Function to delete a user from the database. Used during testing.
   * @param username - the username of the user to delete.
   * @throws SQLException - thrown if there is a database error.
   */
  public static void deleteUser(String username) throws SQLException {

    PreparedStatement prep = conn.prepareStatement("DELETE FROM prev_recipe WHERE username = ?");
    prep.setString(1, username);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM nutrient WHERE username = ?");
    prep.setString(1, username);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM restriction WHERE username = ?");
    prep.setString(1, username);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM account WHERE username = ?");
    prep.setString(1, username);
    prep.executeUpdate();
  }
}
