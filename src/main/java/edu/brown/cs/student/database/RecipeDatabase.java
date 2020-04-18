package edu.brown.cs.student.database;

import edu.brown.cs.student.food.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.Connection;

import com.google.common.io.Files;
import edu.brown.cs.student.recommendation.RecipeNode;

/**
 * 
 * Class comment.
 *
 */

public final class RecipeDatabase {
  
  private static Connection conn;

  public RecipeDatabase() {

  }


  /**
   * Loads in database.
   *
   * @throws FileNotFoundException
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void loadDatabase(String fileName) throws FileNotFoundException,
          ClassNotFoundException, SQLException {

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
  }

  /**
   * Function to insert a recipe into the sqlite database.
   * @param recipe - the Recipe object to be inserted.
   * @return - a boolean representing whether insertion was successful.
   */
  public static boolean insertRecipe(Recipe recipe) {
    return false;
  }

  
  /**
   * Gets a list of Ingredients from recipe id from the database.
   * @param recipeID string id that corresponds to a recipe
   * @return List of Ingredients
   */
  public List<Ingredient> getIngredientsByRecipeID(String recipeID) {
    return null;
  }


    /**
     *
     * @param ingredients
     * @return
     */
  public Recipe getRecipeByIngriedentList(List<Ingredient> ingredients) {
    return null;
  }

  public static List<RecipeNode> getRecipeSubset(){
    return null;
  }

    /**
     *
     * @param ingredients
     * @return
     */
  public List<Recipe> getRecipeListByIngredient(Ingredient ingredients) {
    return null;
  }


  }
