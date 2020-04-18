package edu.brown.cs.student.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import edu.brown.cs.student.food.Recipe;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 
 * This class is used to handle interactions with the Edamam api, and parse the resulting JSON
 * into Java objects.
 *
 */
public final class FieldParser {

  /**
   * APP_ID - The application id for api authentication.
   * APP_KEY - The key for api authentication.
   */
  private static final String APP_ID = "2a676518";
  private static final String APP_KEY = "158f55a83eee58aff1544072b788784f";
  
  public FieldParser() {
    
  }

  /**
   * This function formats an input uri so that it can be used in a get request.
   * @param uri - The uri that must be formatted
   * @return - The uri with all / and : replaced.
   */
  private static String formatURI(String uri) {
    String temp;
    temp = uri.replace(":", "%3A");
    temp = temp.replace("/", "%2F");
    temp = temp.replace("#", "%23");
    return temp;
  }

  /**
   * This function takes the json for each Recipe in an array and returns the information in
   * object form.
   * @param json - the JSON text of the recipe.
   * @return - the array of Recipe objects containing information from the JSON.
   */
  private static Recipe[] parseRecipeJSON(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonDeserializer<Recipe> recipeDeserializer = new RecipeDeserializer();
    gsonBuilder.registerTypeAdapter(Recipe.class, recipeDeserializer);
    Gson gson = gsonBuilder.create();
    return gson.fromJson(json, Recipe[].class);
  }

  /**
   * This function retrieves a recipe from the api that corresponds to the given uri.
   * @param uri - the uri of the desired recipe.
   * @return - the recipe object corresponding to the given uri.
   * @throws IOException - when httpClient.send fails.
   * @throws InterruptedException - when httpClient.send fails.
   */
  public static Recipe getRecipeFromURI(String uri) throws IOException, InterruptedException {
    String reformattedUri = formatURI(uri);

    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?r=" + reformattedUri +
            "&app_id=" + APP_ID + "&app_key=" + APP_KEY)).build();

    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      //error
    }
    Recipe[] recipeArray = parseRecipeJSON(response.body());
    return recipeArray[0];
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
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonDeserializer<Recipe> recipeDeserializer = new RecipeDeserializer();
    gsonBuilder.registerTypeAdapter(Recipe.class, recipeDeserializer);
    Gson gson = gsonBuilder.create();
    Recipe[] parsed = gson.fromJson(json, Recipe[].class);
    System.out.println(parsed[0].getUri());
  }
}