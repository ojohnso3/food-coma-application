package edu.brown.cs.student.database;

import edu.brown.cs.student.food.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.Connection;

import com.google.common.io.Files;
import edu.brown.cs.student.recommendation.RecipeNode;

/**
 * This class contains all interactions with the sqlite database.
 */

public final class RecipeDatabase {
  
  private static Connection conn;

  public RecipeDatabase() {

  }


  /**
   * Loads in database.
   * @throws FileNotFoundException - thrown if the given filepath is not a valid .sqlite3 file.
   * @throws ClassNotFoundException - thrown if there is an error when connecting to the file.
   * @throws SQLException - thrown if there is an error when connecting to the file.
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
   * Function to create tables if the given database file is empty.
   * @throws SQLException - thrown if there is an error when creating the tables in the database.
   */
  public static void createTables() throws SQLException {

    PreparedStatement prep;
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS recipe("
        + "uri TEXT,"
        + "label TEXT,"
        + "image TEXT,"
        + "source TEXT,"
        + "url TEXT,"
        + "yield REAL,"
        + "calories REAL,"
        + "totalWeight REAL,"
        + "totalTime REAL,"
        + "PRIMARY KEY (uri));");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ingredient("
      + "recipe_uri TEXT,"
      + "ingredient TEXT,"
      + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS nutrient_info("
      + "code TEXT,"
      + "recipe_uri TEXT,"
      + "total_daily_val REAL,"
      + "total_nutrient_val REAL,"
      + "FOREIGN KEY (recipe_rui) REFERENCES recipe(uri)");
    prep.executeUpdate();
  }


  /**
   * Function to insert a recipe into the sqlite database.
   * @param recipe - the Recipe object to be inserted.
   * @return - a boolean representing whether insertion was successful.
   */
  public static void insertRecipe(Recipe recipe) throws SQLException {

    PreparedStatement prep = conn.prepareStatement("INSERT INTO recipe VALUES("
        + recipe.prepareForInsert() + ");");
    prep.executeUpdate();

    for (Ingredient ingredient : recipe.getIngredients()) {
      String line = recipe.getUri() + "," + ingredient.getText();

      prep = conn.prepareStatement("INSERT INTO ingredients VALUES("
          + line + ");");
      prep.executeUpdate();
    }

    for (String code : NutrientInfo.nutrients.keySet()) {
      double[] currVals = recipe.getNutrientVals(code);

      String line = code + "," + recipe.getUri() + "," + currVals[0] + "," + currVals[1];

      prep = conn.prepareStatement("INSERT INTO nutrient_info VALUES("
          + line + ")");
      prep.executeUpdate();
    }
  }

  /**
   * Function to retrieve a recipe from the database that corresponds to the given uri.
   * @param uri - String uri of the desired recipe.
   * @return - A Recipe object corresponding to the given uri.
   */
  public Recipe getRecipeFromURI(String uri) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipe WHERE uri = ?");
    prep.setString(1, uri);
    ResultSet recipeSet = prep.executeQuery();

    prep = conn.prepareStatement("SELECT * FROM ingredient WHERE uri = ?");
    prep.setString(1, uri);


    return null;
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
