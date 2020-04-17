package edu.brown.cs.student.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.student.food.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.google.common.io.Files;

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
   * Test api function
   */
  public static String apiCall() {
    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?r=http%3A%2F%2Fwww.edamam.com%2Fontologies%2" +
            "Fedamam.owl%23recipe_9b5945e03f05acbf9d69625138385408&app_id=2a676518" //need to parse uris we get from JSON
            + "&app_key=" +
            "158f55a83eee58aff1544072b788784f")).build();

    try {
      HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.statusCode());
      System.out.println(response.body());
      return response.body();
    } catch (IOException | InterruptedException ioe) {
      ioe.printStackTrace();
      return null;
    }
  }

  /**
   * Test Gson function
   */
  public static void parseJSON() {
    String json = apiCall();
    Gson gson = new Gson();
    Recipe[] parsed = gson.fromJson(json, Recipe[].class);
    System.out.println(parsed[0].getUri());
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
//  public Recipe getRecipeByIngriedentList(List<Ingredient> ingredients) {
//    return null;
//  }


    /**
     *
     * @param ingredients
     * @return
     */
  public List<Recipe> getRecipeListByIngriedent(Ingredient ingredients) {
    return null;
  }


  }
