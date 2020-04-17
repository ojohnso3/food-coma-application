package edu.brown.cs.student.database;

import edu.brown.cs.student.food.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.google.common.io.Files;

/**
 * 
 * Class comment.
 *
 */
public class RecipeDatabase {
  
  public RecipeDatabase() {
    
  }
  
  
  /**
   * Loads in database.
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

    Connection conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Test api function
   */
  public void apiCall() {
    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?q=chicken&app_id=2a676518"
            + "&app_key=" +
            "158f55a83eee58aff1544072b788784f&from=0&to=3&calories=591-722&health=alcohol-free")).build();

    try {
      HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
      System.out.println(response.statusCode());
    } catch (IOException | InterruptedException ioe) {
      ioe.printStackTrace();
    }



  }
  
  
  /**
   * Gets a Recipe from id from the database.
   * @param recipeID string id that corresponds to a recipe
   * @return Recipe object
   */
  public Recipe getRecipeByID(String recipeID) {
    Recipe recipe = new Recipe();
    try {
      PreparedStatement prep = conn.prepareStatement(
          "SELECT actor_film.film FROM actor_film WHERE actor_film.actor = ?;");
      prep.setString(1, actorId);
      ResultSet results = prep.executeQuery();

      while (results.next()) {
        films.add(results.getString(1));
      }
      results.close();
    } catch (SQLException e) {
      System.err.println("ERROR: Database unable to perform given SQL.");
    }
    return films;
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
  
  
  /**
   * 
   * @param ingredients
   * @return
   */
  public List<Recipe> getRecipeListByIngriedent(Ingredient ingredients) {
    return null;
  }
  
  
  
  
  
}