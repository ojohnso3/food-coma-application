package edu.brown.cs.student.login;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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


}
