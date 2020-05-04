package edu.brown.cs.student.database;

import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.food.Ingredient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.util.Map;

import com.google.common.io.Files;

/**
 * This class contains all interactions with the sqlite database.
 */

public final class RecipeDatabase {

  private static Connection conn;

  private RecipeDatabase() {

  }

  /**
   * Loads in database.
   * @param fileName - name of the database file.
   * @throws FileNotFoundException - thrown if the given filepath is not a valid .sqlite3 file.
   * @throws ClassNotFoundException - thrown if there is an error when connecting to the file.
   * @throws SQLException - thrown if there is an error when connecting to the file.
   */
  public static void loadDatabase(String fileName) throws FileNotFoundException,
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
    createTables(stat);
  }

  /**
   * Function to create tables if the given database file is empty.
   * @throws SQLException - thrown if there is an error when creating the tables in the database.
   */
  private static void createTables(Statement stat) throws SQLException {

    String sql = "CREATE TABLE IF NOT EXISTS recipe("
        + "uri TEXT,"
        + "label TEXT,"
        + "image TEXT,"
        + "source TEXT,"
        + "url TEXT,"
        + "yield REAL,"
        + "calories REAL,"
        + "total_weight REAL,"
        + "total_time REAL,"
        + "PRIMARY KEY (uri));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS ingredient("
      + "recipe_uri TEXT,"
      + "ingredient TEXT,"
      + "weight REAL,"
      + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS nutrient_info("
      + "code TEXT,"
      + "recipe_uri TEXT,"
      + "total_daily_val REAL,"
      + "total_nutrient_val REAL,"
      + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS queries("
            + "query TEXT,"
            + "recipe_uri TEXT,"
            + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);
    stat.close();
  }


  /**
   * Function to insert a recipe into the sqlite database.
   * @param recipe - the Recipe object to be inserted.
   * @return - a boolean representing whether insertion was successful.
   */
  public static void insertRecipe(Recipe recipe) throws SQLException {

    if (checkRecipeInDatabase(recipe.getUri())) {
      throw new SQLException("duplicate");
    }

    PreparedStatement prep = conn.prepareStatement("INSERT INTO recipe VALUES("
        + recipe.prepareForInsert() + ");");
    prep.executeUpdate();
    for (Ingredient ingredient : recipe.getIngredients()) {
      String line = "\"" + recipe.getUri() + "\",\"" + ingredient.getText() + "\","
          + ingredient.getWeight();

      prep = conn.prepareStatement("INSERT INTO ingredient VALUES("
          + line + ");");
      prep.executeUpdate();
    }

    for (String code : NutrientInfo.nutrients.keySet()) {
      double[] currVals = recipe.getNutrientVals(code);

      if (currVals != null) {
        String line = "\"" + code + "\",\"" + recipe.getUri() + "\"," + currVals[0] + "," + currVals[1];

        prep = conn.prepareStatement("INSERT INTO nutrient_info VALUES("
            + line + ")");
        prep.executeUpdate();
      }
    }
    prep.close();
  }

  public static void insertQuery(String query, String[] uriList) throws SQLException {
    if (checkQueryInDatabase(query)) {
      throw new SQLException("duplicate");
    }

    for(String uri : uriList){
      PreparedStatement prep = conn.prepareStatement("INSERT INTO queries VALUES("
              +"\"" + query + "\" , \"" + uri + "\");");
      prep.executeUpdate();
      prep.close();
    }
  }

  /**
   * Function to get a Recipe from the api and add it to the database.
   * @param uri - the uri of the desired recipe.
   * @return - A Recipe object that contains data from the given uri in the api.
   */
  private static Recipe loadFromApi(String uri) throws SQLException, InterruptedException,
    APIException, IOException {
    Recipe recipe = FieldParser.getRecipeFromURI(uri);
    insertRecipe(recipe);
    return recipe;
  }


  /**
   * Function to create a nutrients map from a given ResultSet.
   * @param nutrientSet - the ResultSet with data from the nutrient_info table.
   * @return - a map from String nutrient codes to a double of the total daily value and the total
   * nutrient value of that nutrient for a certain recipe.
   */
  private static Map<String, double[]> createNutrients(ResultSet nutrientSet) throws SQLException {
    Map<String, double[]> nutrients = new HashMap<>();
    while (nutrientSet.next()) {
      String code = nutrientSet.getString("code");
      double[] valsArray = new double[2];
      valsArray[0] = nutrientSet.getDouble("total_daily_val");
      valsArray[1] = nutrientSet.getDouble("total_nutrient_val");
      nutrients.put(code, valsArray);
    }
    return nutrients;
  }

  /**
   * Function to create Ingredient objects from a given ResultSet.
   * @param ingredientSet - the ResultSet with data from the ingredient table.
   * @return - a list of the Ingredients containing the given data.
   */
  private static List<Ingredient> createIngredients(ResultSet ingredientSet) throws SQLException {
    List<Ingredient> ingredients = new ArrayList<>();
    while (ingredientSet.next()) {
      String text = ingredientSet.getString("ingredient");
      double weight = ingredientSet.getDouble("weight");
      ingredients.add(new Ingredient(text, weight));
    }
    return ingredients;
  }

  /**
   * Function to create a Recipe object from the given ResultSets.
   * @param recipeSet - The ResultSet with data from the recipe table.
   * @param ingredientSet - The ResultSet with data from the ingredient table.
   * @param nutrientSet - The ResultSet with data from the nutrient_info table.
   * @return - a Recipe object containing all of the given data.
   */
  private static Recipe createRecipe(ResultSet recipeSet, ResultSet ingredientSet,
                                     ResultSet nutrientSet, String uri) throws
      SQLException, InterruptedException, IOException, APIException {
    if (recipeSet.next()) {
      String label = recipeSet.getString("label");
      String image = recipeSet.getString("image");
      String source = recipeSet.getString("source");
      String url = recipeSet.getString("url");
      double yield = recipeSet.getDouble("yield");
      double calories = recipeSet.getDouble("calories");
      double totalWeight = recipeSet.getDouble("total_weight");
      double totalTime = recipeSet.getDouble("total_time");
      System.out.println("all primitives passed");
      List<Ingredient> ingredients = createIngredients(ingredientSet);
      System.out.println("after ingredients");
      Map<String, double[]> nutrients = createNutrients(nutrientSet);
      System.out.println("after nutrients");

      return new Recipe(uri, label, image, source, url, yield, calories, totalWeight, totalTime,
          ingredients, nutrients);
    }

    return loadFromApi(uri);
  }

  /**
   * Function to retrieve a recipe from the database that corresponds to the given uri.
   * @param uri - String uri of the desired recipe.
   * @return - A Recipe object corresponding to the given uri.
   */
  public static Recipe getRecipeFromURI(String uri) throws SQLException, InterruptedException,
      APIException, IOException {

    PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipe WHERE uri = ?");
    prep.setString(1, uri);
    ResultSet recipeSet = prep.executeQuery();

    prep = conn.prepareStatement("SELECT * FROM ingredient WHERE recipe_uri = ?");
    prep.setString(1, uri);
    ResultSet ingredientSet = prep.executeQuery();

    prep = conn.prepareStatement("SELECT * FROM nutrient_info WHERE recipe_uri = ?");
    prep.setString(1, uri);
    ResultSet nutrientSet = prep.executeQuery();

    Recipe r = createRecipe(recipeSet, ingredientSet, nutrientSet, uri);
    prep.close();
    recipeSet.close();
    ingredientSet.close();
    nutrientSet.close();

    return r;
  }


  /**
   * Gets a list of Ingredients from recipe id from the database.
   * @param uri string id that corresponds to a recipe
   * @return List of Ingredients
   */
  public static List<Ingredient> getIngredientsByRecipeID(String uri) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
        "SELECT * FROM ingredients WHERE recipe_uri = ?");
    prep.setString(1, uri);
    ResultSet ingredientSet = prep.executeQuery();
    return createIngredients(ingredientSet);
  }

  /**
   * Function to check if the given recipe uri is already in the database.
   * @param uri - String uri of a recipe.
   * @return - boolean representing whether the given uri is in the database.
   */
  public static boolean checkRecipeInDatabase(String uri) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipe WHERE uri = ?");
    prep.setString(1, uri);
    ResultSet recipeSet = prep.executeQuery();
    boolean retVal = recipeSet.next();
    prep.close();
    recipeSet.close();
    return retVal;
  }

  /**
   * Function to check if the given query is already in the database.
   * @param query - String uri of a recipe.
   * @return - boolean representing whether the given uri is in the database.
   */
  public static boolean checkQueryInDatabase(String query) throws SQLException {

    System.out.println("REQUESTED QUERY IS " + query);
    PreparedStatement prep = conn.prepareStatement("SELECT query FROM queries WHERE query = ?");
    prep.setString(1, query);
    ResultSet recipeSet = prep.executeQuery();
    boolean retVal = recipeSet.next();
    prep.close();
    recipeSet.close();
    return retVal;
  }

  public static List<String> getQueryURIListFromDatabase(String query) throws SQLException{
    PreparedStatement prep = conn.prepareStatement("SELECT recipe_uri FROM queries WHERE query = ?");
    prep.setString(1, query);
    ResultSet recipeSet = prep.executeQuery();
    List<String> recipesFromExactQuery = new ArrayList<String>();
    while(recipeSet.next()){
      String uri = recipeSet.getString("recipe_uri");
      recipesFromExactQuery.add(uri);
    }
    prep.close();
    recipeSet.close();
    return recipesFromExactQuery;
  }

  public static List<String> getSimilar(String query) throws SQLException{
    String q = "%" + query + "%";
    PreparedStatement prep = conn.prepareStatement("SELECT uri FROM recipe WHERE label LIKE ?");
    prep.setString(1,q);
    List<String> recipesFromSimilarQuery = new ArrayList<String>();
    ResultSet recipeSet = prep.executeQuery();
    while(recipeSet.next()){
      recipesFromSimilarQuery.add(recipeSet.getString("uri"));
    }
    return recipesFromSimilarQuery;
  }



    /**
     * Database test function.
     */
  public static void testDatabaseFile() {
    try {
      Recipe r = getRecipeFromURI("http%3A%2F%2Fwww.edamam.com%2Fontologies%2Fedamam.owl%23recipe_9b5945e03f05acbf9d69625138385408");
      System.out.println("URI: " + r.getUri());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

