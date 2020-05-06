package edu.brown.cs.student.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.brown.cs.student.food.Recipe;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private static final int MIN_NUM_RECS = 10;

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
  private static String handleParamsAndRestrictions(List<String> dietaryRestrictions,
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
  public static Recipe[] getRecipesFromQuery(String query, List<String> dietaryRestrictions,
                                             Map<String, String[]> paramsMap)
      throws IOException, InterruptedException, APIException, SQLException {
    query = query.replace(" ", "+");
    HttpClient httpClient = HttpClient.newBuilder().build();
    String queryUri = handleParamsAndRestrictions(dietaryRestrictions, paramsMap);

    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?q=" + query
            + "&app_id=" + APP_ID + "&app_key=" + APP_KEY + "&to=99" + queryUri)).build();

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
   * Test api function.
   * @return the response
   */
  public static String apiCall() {
    HttpClient httpClient = HttpClient.newBuilder().build();
    HttpRequest httpRequest = HttpRequest.newBuilder().GET()
        .uri(URI.create("https://api.edamam.com/search?")).build();

    try {
      HttpResponse<String> response = httpClient.send(httpRequest,
          HttpResponse.BodyHandlers.ofString());
      System.out.println(response.statusCode());
//      System.out.println(response.body());
      return response.body();
    } catch (IOException | InterruptedException ioe) {
      ioe.printStackTrace();
      return null;
    }
  }

  /**
   * Test Gson function.
   * @return the recipe
   */
  public static Recipe parseJSON() {
    String json = apiCall();
    assert json != null;
    Recipe[] recipes = parseRecipeJSON(json);
    assert recipes != null;
    for (Recipe recipe : recipes) {
      System.out.println(recipe.getUri());
      System.out.println(recipe.getNutrientVals("FE")[0]);
    }
    return recipes[0];
  }

  /**
   * This function checks that a recipe conforms to the given parameters.
   * @param r - the Recipe to check for validity.
   * @param dietaryRestrictions - a list of health labels to check for.
   * @param paramsMap - the map that contains which diet and health labels to check for.
   * @return - true if the Recipe has the correct labels, false otherwise.
   */
  private static boolean checkRecipeValidity(Recipe r, List<String> dietaryRestrictions,
                                             Map<String, String[]> paramsMap) {
    List<String> dietLabels = r.getDietLabels();
    List<String> healthLabels = r.getHealthLabels();
    for (String label : dietaryRestrictions) {
      if (!healthLabels.contains(label)) {
        return false;
      }
    }

    for (String param : paramsMap.get("health")) {
      if (!healthLabels.contains(param)) {
        return false;
      }
    }

    for (String param : paramsMap.get("diet")) {
      if (!dietLabels.contains(param)) {
        return false;
      }
    }

    return true;
  }

  /**
   * This function retrieves recipes that correspond to the given query in the api.
   * @param query - the desired query to search for in the api.
   * @param dietaryRestrictions - the diet and health labels to include in the query.
   * @param paramsMap - the map of constraints for the query -- see comment in InputMatcher for
   * more details.
   * @return - an array of recipes that correspond to the given query in the api.
   */
  public static List<Recipe> pullPrevRecipes(String query, List<String> dietaryRestrictions,
                                             Map<String, String[]> paramsMap)
          throws IOException, InterruptedException, APIException, SQLException {
    List<String> uris = RecipeDatabase.getQueryURIListFromDatabase(query);
    List<Recipe> recipesForRet = new ArrayList<>();
    for (String s : uris) {
      Recipe currRec = RecipeDatabase.getRecipeFromURI(s);
      if (checkRecipeValidity(currRec, dietaryRestrictions, paramsMap)) {
        System.out.println("VALID RECIPE: " + currRec.getLabel());
        recipesForRet.add(currRec);
      }
    }
    return recipesForRet;
  }

  /**
   * This function retrieves recipes that correspond to the given query in the api.
   * @param query - the desired query to search for in the api.
   * @param dietaryRestrictions - the diet and health labels to include in the query.
   * @param paramsMap - the map of constraints for the query -- see comment in InputMatcher for
   * more details.
   * @return - an array of recipes that correspond to the given query in the api.
   */
  public static List<Recipe> pullSimilar(String query, List<String> dietaryRestrictions,
                                         Map<String, String[]> paramsMap) throws SQLException,
      InterruptedException, IOException, APIException {
    List<String> uris = RecipeDatabase.getSimilar(query);
    List<Recipe> recipeList = new ArrayList<>();
    for (String s : uris) {
      Recipe currRec = RecipeDatabase.getRecipeFromURI(s);
      if (checkRecipeValidity(currRec, dietaryRestrictions, paramsMap)) {
        recipeList.add(RecipeDatabase.getRecipeFromURI(s));
      }
    }
    return recipeList;
  }
}
