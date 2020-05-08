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
import java.util.*;
import java.sql.Connection;

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

    sql = "CREATE TABLE IF NOT EXISTS diet_label("
        + "recipe_uri TEXT,"
        + "label TEXT,"
        + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS health_label("
        + "recipe_uri TEXT,"
        + "label TEXT,"
        + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);

    sql = "CREATE TABLE IF NOT EXISTS new_queries("
            + "query TEXT,"
            + "recipe_uri TEXT,"
            + "restrictions TEXT,"
            + "FOREIGN KEY (recipe_uri) REFERENCES recipe(uri));";
    stat.executeUpdate(sql);
    stat.close();
  }


  /**
   * Function to insert a recipe into the sqlite database.
   * @param recipe - the Recipe object to be inserted.
   */
  public static void insertRecipe(Recipe recipe) throws SQLException {

    if (checkRecipeInDatabase(recipe.getUri())) {
      throw new SQLException("duplicate");
    }
    System.out.println(recipe.prepareForInsert());
    PreparedStatement prep = conn.prepareStatement("INSERT INTO recipe VALUES("
            + recipe.prepareForInsert() + ");");
    prep.executeUpdate();

    for (Ingredient ingredient : recipe.getIngredients()) {
      String text = ingredient.getText().replace("\"", "");
      String line = "\"" + recipe.getUri() + "\",\"" + text + "\","
          + ingredient.getWeight();

      prep = conn.prepareStatement("INSERT INTO ingredient VALUES("
          + line + ");");
      prep.executeUpdate();
    }

    for (String code : NutrientInfo.getNutrients().keySet()) {
      double[] currVals = recipe.getNutrientVals(code);

      if (currVals != null) {
        String line = "\"" + code + "\",\"" + recipe.getUri() + "\"," + currVals[0] + ","
            + currVals[1];

        prep = conn.prepareStatement("INSERT INTO nutrient_info VALUES("
            + line + ")");
        prep.executeUpdate();
      }
    }

    for (String label : recipe.getDietLabels()) {
      String line = "\"" + recipe.getUri() + "\",\"" + label + "\"";

      prep = conn.prepareStatement("INSERT INTO diet_label VALUES("
              + line + ")");
      prep.executeUpdate();
    }

    for (String label : recipe.getHealthLabels()) {
      String line = "\"" + recipe.getUri() + "\",\"" + label + "\"";

      prep = conn.prepareStatement("INSERT INTO health_label VALUES("
              + line + ")");
      prep.executeUpdate();
    }

    prep.close();

  }
  /**
   * Function to insert a query into the query table of the database.
   * @param query - the query that corresponds to the given recipes.
   * @param uriList -  a list of recipes that conform to the given query.
   */
  public static void insertQuery(String query, String[] uriList, Set<String> restrictions, Map<String, String[]> paramsMap) throws SQLException {
//    if (checkQueryInDatabase(query)) {
//      throw new SQLException("duplicate");
//    }
    String inputRestrict = prepRestrictionsForDB(restrictions);

    for (String uri : uriList) {
      PreparedStatement prep = conn.prepareStatement("INSERT INTO new_queries VALUES("
              +"\"" + query + "\" , \"" + uri + "\" , \"" + inputRestrict + "\");");
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
    if (recipe != null) {
      insertRecipe(recipe);
      return recipe;
    }
    throw new APIException("No recipes correspond to the given uri.");
  }

  /**
   * Function to create a list of diet or health labels from a given ResultSet.
   * @param labelSet - the ResultSet with data from a table.
   * @return - a list of the labels.
   */
  private static List<String> createLabel(ResultSet labelSet) throws SQLException {
    List<String> labels = new ArrayList<>();
    while (labelSet.next()) {
      labels.add(labelSet.getString("label"));
    }
    return labels;
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
                                     ResultSet nutrientSet, ResultSet dietSet,
                                     ResultSet healthSet, String uri) throws
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

      List<String> dietLabels = createLabel(dietSet);
      List<String> healthLabels = createLabel(healthSet);
      List<Ingredient> ingredients = createIngredients(ingredientSet);
      Map<String, double[]> nutrients = createNutrients(nutrientSet);


      return new Recipe(uri, label, image, source, url, yield, calories, totalWeight, totalTime,
          ingredients, nutrients, healthLabels, dietLabels);
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

    prep = conn.prepareStatement("SELECT * FROM diet_label WHERE recipe_uri = ?");
    prep.setString(1, uri);
    ResultSet dietSet = prep.executeQuery();

    prep = conn.prepareStatement("SELECT * FROM health_label WHERE recipe_uri = ?");
    prep.setString(1, uri);
    ResultSet healthSet = prep.executeQuery();

    Recipe r = createRecipe(recipeSet, ingredientSet, nutrientSet, dietSet, healthSet, uri);
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
  public static boolean checkRecipeInDatabase(String uri) {

    boolean retVal = false;
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT * FROM recipe WHERE uri = ?");
      prep.setString(1, uri);
      ResultSet recipeSet = prep.executeQuery();
      retVal = recipeSet.next();
      prep.close();
      recipeSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return retVal;
  }

  /**
   * Function to check if the given query is already in the database.
   * @param query - String uri of a recipe.
   * @param restrictions - the restrictions to apply to the results of the query.
   * @return - boolean representing whether the given uri is in the database.
   */
  public static boolean checkQueryInDatabase(String query, Set<String> restrictions){

    boolean retVal = false;
    try {
      PreparedStatement prep = conn.prepareStatement("SELECT query FROM new_queries WHERE query = ? AND restrictions = ?");
      prep.setString(1, query);
      prep.setString(2, prepRestrictionsForDB(restrictions));
      ResultSet recipeSet = prep.executeQuery();
      retVal = recipeSet.next();
      prep.close();
      recipeSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("QUERY IN DB : " + retVal);
    return retVal;
  }

  /**
   * Function to turn the set of restrictions into a string that can be inserted into the database.
   * @param restrictions - the set of restrictions to insert to the database.
   * @return - the string to insert to the database.
   */
  public static String prepRestrictionsForDB(Set<String> restrictions) {
    String insertThis = "";
    if (restrictions.contains("alcohol-free")) {
      insertThis += "a";
    }
    if (restrictions.contains("vegan")) {
      insertThis += "e";
    }
    if (restrictions.contains("peanut-free")) {
      insertThis += "p";
    }
    if (restrictions.contains("sugar-conscious")) {
      insertThis += "s";
    }
    if (restrictions.contains("tree-nut-free")) {
      insertThis += "t";
    }
    if (restrictions.contains("vegetarian")) {
      insertThis += "v";
    }
    return insertThis;
  }
      /**
       * Function to retrieve the list of uris that correspond to the given query.
       * @param query - the query to find recipes for.
       * @param restrictions - the restrictions to apply to the results of the given query.
       * @return - a list of the recipes that correspond to the given query.
       */
  public static List<Recipe> getQueryRecipesFromDatabase(String query, Set<String> restrictions,
                                                         Map<String, String[]> paramsMap) {
    List<String> recipesFromExactQuery = new ArrayList<String>();
    List<Recipe> recipesFromExact = new ArrayList<>();
    try {
      PreparedStatement prep = conn.prepareStatement(
          "SELECT recipe_uri FROM new_queries WHERE query = ? AND restrictions = ?");

      System.out.println("query: " +query);
      prep.setString(1, query);
      System.out.println("prepRest: " + prepRestrictionsForDB(restrictions));
      prep.setString(2, prepRestrictionsForDB(restrictions));
      ResultSet recipeSet = prep.executeQuery();
      while (recipeSet.next()) {
        String uri = recipeSet.getString("recipe_uri");

        Recipe currRecipe = RecipeDatabase.getRecipeFromURI(uri);
//        if (FieldParser.checkRecipeValidity(currRecipe, restrictions, paramsMap)) {
          recipesFromExactQuery.add(uri);
          recipesFromExact.add(currRecipe);
//        }
      }
      prep.close();
      recipeSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return recipesFromExact;
  }
      /**
       * Function to find recipes whose labels are similar to the given query.
       * @param query - the query to search on.
       * @return - a list of recipe uris that correspond to the given query.
       */
  public static List<Recipe> getSimilar(String query, Set<String> dietaryRestrictions, Map<String, String[]> paramsMap) {

    List<String> recipesFromSimilarQuery = new ArrayList<>();
    List<Recipe> recipesFromSimilar = new ArrayList<>();
    try {
      String q = "%" + query + "%";
      PreparedStatement prep = conn.prepareStatement("SELECT uri FROM recipe WHERE label LIKE ?");
      prep.setString(1,q);
      ResultSet recipeSet = prep.executeQuery();
      while (recipeSet.next()) {
        Recipe currRecipe = RecipeDatabase.getRecipeFromURI(recipeSet.getString("uri"));
        if (FieldParser.checkRecipeValidity(currRecipe, dietaryRestrictions, paramsMap)) {
          recipesFromSimilarQuery.add(recipeSet.getString("uri"));
          recipesFromSimilar.add(currRecipe);
        }
      }
      recipeSet.close();
      prep.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return recipesFromSimilar;
  }

  /**
   * Function to delete a recipe from the database. Used during testing.
   * @param uri - the uri of the recipe to delete.
   * @throws SQLException - thrown if there is a database error.
   */
  public static void deleteUser(String uri) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("DELETE FROM diet_label WHERE recipe_uri = ?");
    prep.setString(1, uri);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM health_label WHERE recipe_uri = ?");
    prep.setString(1, uri);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM ingredient WHERE recipe_uri = ?");
    prep.setString(1, uri);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM nutrient_info WHERE recipe_uri = ?");
    prep.setString(1, uri);
    prep.executeUpdate();

    prep = conn.prepareStatement("DELETE FROM recipe WHERE uri = ?");
    prep.setString(1, uri);
    prep.executeUpdate();
  }
}

