package edu.brown.cs.student.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.gui.Gui;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  private FieldParser() { }

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
   * Function to format the user dietary restrictions and query parameters into uri format.
   * @param dietaryRestrictions - the user's saved dietary restrictions.
   * @param paramsMap - the HashMap of constraints for the query -- see comment in InputMatcher
   * for more details.
   * @return - String of each element in the restrictions list and the params map formatted for
   * uris.
   */
  private static String handleParamsAndRestrictions(Set<String> dietaryRestrictions,
                                                    Map<String, String[]> paramsMap) {
    StringBuilder line = new StringBuilder();
    for (String s : dietaryRestrictions) {
      line.append("&").append("health=").append(s);
    }

    for (String param : paramsMap.keySet()) {
      String[] option = paramsMap.get(param);
      for (String elt : option) {
        line.append("&").append(param).append("=").append(elt);
      }

    }
    return line.toString();
  }

  /**
   * This function takes the json for each Recipe in an array and returns the information in
   * object form.
   * @param json - the JSON text of the recipe.
   * @return - the array of Recipe objects containing information from the JSON.
   */
  private static Recipe[] parseRecipeJSON(String json) {
    if (json.equals("[")) {
      return new Recipe[0];
    }
    System.out.println("got here");
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonDeserializer<Recipe> recipeDeserializer = new RecipeDeserializer();
    gsonBuilder.registerTypeAdapter(Recipe.class, recipeDeserializer);
    Gson gson = gsonBuilder.create();
    JsonElement jsonTree = JsonParser.parseString(json);

    try {
      JsonObject jsonObject = jsonTree.getAsJsonObject();
      if (jsonObject.has("hits")) {
        JsonElement hits = jsonObject.get("hits");
        JsonArray hitsArray = hits.getAsJsonArray();
        Recipe[] recipes = new Recipe[hitsArray.size()];

        for (int i = 0; i < hitsArray.size(); i++) {
          JsonObject currElt = hitsArray.get(i).getAsJsonObject();
          recipes[i] = gson.fromJson(currElt.get("recipe"), Recipe.class);
        }
        return recipes;
      }
    } catch (IllegalStateException ise) {
      return gson.fromJson(json, Recipe[].class);
    }
    return null; //should never be reached.
  }

  /**
   * This function retrieves a recipe from the api that corresponds to the given uri.
   * @param uri - the uri of the desired recipe.
   * @return - the recipe object corresponding to the given uri.
   * @throws IOException - when httpClient.send fails.
   * @throws InterruptedException - when httpClient.send fails.
   */
  public static Recipe getRecipeFromURI(String uri) throws IOException, InterruptedException,
      APIException {
    String reformattedUri = formatURI(uri);

    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?r=" + reformattedUri
            + "&app_id=" + APP_ID + "&app_key=" + APP_KEY)).build();

    HttpResponse<String> response = httpClient.send(httpRequest,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new APIException("API returned error " + response.statusCode());
    }
//    System.out.println(response.body());
    Recipe[] recipeArray = parseRecipeJSON(response.body());
    if (recipeArray == null) {
      throw new APIException("API returned malformed JSON");
    }

    //API didn't find any recipes with the given uri.
    if (recipeArray.length == 0) {
      throw new APIException("No recipes correspond to the given uri.");
    }
    return recipeArray[0];
  }

  /**
   * This function retrieves recipes that correspond to the given query in the api.
   * @param query - the desired query to search for in the api.
   * @param dietaryRestrictions - the diet and health labels to include in the query.
   * @param paramsMap - the map of constraints for the query -- see comment in InputMatcher for
   * more details.
   * @return - an array of recipes that correspond to the given query in the api.
   */
  public static Recipe[] getRecipesFromQuery(String query, Set<String> dietaryRestrictions,
                                             Map<String, String[]> paramsMap)
      throws IOException, InterruptedException, APIException {
    query = query.replace(" ", "+");
    HttpClient httpClient = HttpClient.newBuilder().build();
    String queryUri = handleParamsAndRestrictions(dietaryRestrictions, paramsMap);

    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?q=" + query
            + "&app_id=" + APP_ID + "&app_key=" + APP_KEY + "&to=50" + queryUri)).build();

    HttpResponse<String> response = httpClient.send(httpRequest,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new APIException("API returned error " + response.statusCode());
    }

    Recipe[] recipes = parseRecipeJSON(response.body());
    if (recipes == null) {
      throw new APIException("API returned malformed JSON");
    }
    for (Recipe r : recipes) {
//      recipe uris in recipe database must be unique.
      try {
        if (!RecipeDatabase.checkRecipeInDatabase(r.getUri())) {
          RecipeDatabase.insertRecipe(r);
        }
      } catch (SQLException e) {
        System.out.println("Duplicate recipe attempted to be added to DB");
      }
    }

    return recipes;
  }

  /**
   * This function checks that a recipe conforms to the given parameters.
   * @param r - the Recipe to check for validity.
   * @param dietaryRestrictions - a list of health labels to check for.
   * @param paramsMap - the map that contains which diet and health labels to check for.
   * @return - true if the Recipe has the correct labels, false otherwise.
   */
  public static boolean checkRecipeValidity(Recipe r, Set<String> dietaryRestrictions,
                                             Map<String, String[]> paramsMap) {
    List<String> dietLabels = r.getDietLabels();
    List<String> healthLabels = r.getHealthLabels();
    for (String label : dietaryRestrictions) {
      System.out.println("valid loop");
      if (!healthLabels.contains(label)) {
//        System.out.println("LABEL " + label);
        System.out.println("contains label?");
        return false;
      }
    }
    System.out.println("OK Acceptable labels");
    return true;
  }

  /**
   * This function retrieves recipes from either the Database or API. The database is called if
   * the query was previously inputted, or if the API is out of calls and the only way to get
   * recipes is to check our database.
   * @param query - the desired query to search for in the api.
   * @param restrictions - the diet and health labels to include in the query.
   * @param paramsMap - the map of constraints for the query -- see comment in InputMatcher for
   * more details.
   * @return - an array of recipes that correspond to the given query in the api.
   */
  public static Recipe[] getRecipesDBandAPI(String query, Set<String> restrictions, Map<String, String[]> paramsMap){
    Recipe[] recipes = new Recipe[0];
    try{
    if(RecipeDatabase.checkQueryInDatabase(query, restrictions)){
      List<Recipe> exactQueryRecipes = RecipeDatabase.getQueryRecipesFromDatabase(query, restrictions, paramsMap);
      recipes = new Recipe[exactQueryRecipes.size()];
      for(int i = 0; i < exactQueryRecipes.size(); i++){
        recipes[i] = exactQueryRecipes.get(i);
      }
    }
    if(!RecipeDatabase.checkQueryInDatabase(query, restrictions) || recipes.length == 0){
      System.out.println("MAKING API CALL");

      recipes = FieldParser.getRecipesFromQuery(query, restrictions, paramsMap);
      String[] recipesForDb = new String[recipes.length];
      for(int i = 0; i < recipes.length; i++){
        recipesForDb[i] = recipes[i].getUri();
      }
      if(!RecipeDatabase.checkQueryInDatabase(query, restrictions)){
        RecipeDatabase.insertQuery(query, recipesForDb, restrictions, paramsMap);
      }
    }

  } catch (IOException e) {
    System.out.println("IOException getting recipes from query: " + e.getMessage());
  } catch (InterruptedException e) {
    System.out.println("InterruptedException getting recipes from query");
  } catch (APIException e) {
    System.out.println("API Exception getting recipes from query. Message:  " + e.getMessage());
//  } catch (ClassNotFoundException e) {
//    System.out.println("Database not found when loading during search");
  } catch (SQLException e){
    System.out.println("SQLException in getting recipes from database: " + e.getMessage());
  }

      if(recipes.length==0){
    List<Recipe> sim = RecipeDatabase.getSimilar(query, restrictions, paramsMap);
    if(sim.size() > 0){
      recipes = new Recipe[sim.size()];
      System.out.println("SIMILAR HAS BEEN CALLED. SIZE OF SIMILAR IS: " + sim.size());
      for(int i = 0; i < sim.size(); i ++){
          recipes[i] = sim.get(i);
      }
    }
  }
      return recipes;
  }
}
