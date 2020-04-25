package edu.brown.cs.student.database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import edu.brown.cs.student.food.Ingredient;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to provide a deserializer to parse the Edamam Api JSON. It implements the
 * JsonDeserializer interface, which takes in an object of type Ingredient.
 */
public class RecipeDeserializer implements JsonDeserializer<Recipe> {

  /**
   * @param totalDailyObject - the JSON object for the totalDaily list.
   * @param totalNutrientsObject - the JSOn object ofr the totalNutrients list.
   * Helper function for deserialize to store all of the totalDaily and totalNutrient info in the
   * JSON.
   * @return - A Map with keys as the nutrient code and an array of the daily quantity and the
   * total quantity, as doubles.
   */
  private Map<String, double[]> makeNutrientMap(JsonObject totalDailyObject, JsonObject totalNutrientsObject) {
    Map<String, double[]> nutrients = new HashMap<>();

    for (String code : NutrientInfo.nutrients.keySet()) {
      double[] nutrientArray = new double[2];
      if (totalDailyObject.has(code)) {
        JsonElement currDailyNutrient = totalDailyObject.get(code);
        JsonObject currDailyNutrientObject = currDailyNutrient.getAsJsonObject();
        double totalDailyQuantity = currDailyNutrientObject.get("quantity").getAsDouble();
        nutrientArray[0] = totalDailyQuantity;
      }
      if (totalNutrientsObject.has(code)) {
        JsonElement currNutrientJson = totalNutrientsObject.get(code);
        JsonObject currNutrientObject = currNutrientJson.getAsJsonObject();
        double totalNutrientQuantity = currNutrientObject.get("quantity").getAsDouble();

        nutrientArray[1] = totalNutrientQuantity;
      }
      nutrients.put(code, nutrientArray);
    }

    return nutrients;
  }

  @Override
  public Recipe deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String uri = jsonObject.get("uri").getAsString();
    String label = jsonObject.get("label").getAsString();
    String image = jsonObject.get("image").getAsString();
    String source = jsonObject.get("source").getAsString();
    String url = jsonObject.get("url").getAsString();
    String shareAs = jsonObject.get("shareAs").getAsString();
    double yield = jsonObject.get("yield").getAsDouble();

    JsonElement dietArray = jsonObject.get("dietLabels");
    JsonArray jsonDietLabels = dietArray.getAsJsonArray();
    String[] dietLabels = new String[jsonDietLabels.size()];
    for (int i = 0; i < jsonDietLabels.size(); i++) {
       dietLabels[i] = jsonDietLabels.get(i).getAsString();
    }

    JsonElement healthArray = jsonObject.get("healthLabels");
    JsonArray jsonHealthLabels = healthArray.getAsJsonArray();
    String[] healthLabels = new String[jsonHealthLabels.size()];
    for (int j = 0; j < jsonHealthLabels.size(); j++) {
      healthLabels[j] = jsonHealthLabels.get(j).getAsString();
    }

    JsonElement ingredientsArray = jsonObject.get("ingredients"); //get the json for the array of ingredients
    JsonArray jsonIngredients = ingredientsArray.getAsJsonArray(); //get the array from the json
    List<Ingredient> ingredients = new ArrayList<>();
    for (int k = 0; k < jsonIngredients.size(); k++) { //each element of the array should be an ingredient
      JsonDeserializer<Ingredient> ingredientDeserializer = new IngredientDeserializer();
      gsonBuilder.registerTypeAdapter(Ingredient.class, ingredientDeserializer);
      Gson customGson = gsonBuilder.create();
      Ingredient currIngredient = customGson.fromJson(jsonIngredients.get(k), Ingredient.class); //parse ingredients
      ingredients.add(currIngredient);
    }
    double calories = jsonObject.get("calories").getAsDouble();
    double totalWeight = jsonObject.get("totalWeight").getAsDouble();
    double totalTime = jsonObject.get("totalTime").getAsDouble();

    JsonElement totalDaily = jsonObject.get("totalDaily");
    JsonObject totalDailyObject = totalDaily.getAsJsonObject();

    JsonElement totalNutrients = jsonObject.get("totalNutrients");
    JsonObject totalNutrientsObject = totalNutrients.getAsJsonObject();

    Map<String, double[]> nutrients = this.makeNutrientMap(totalDailyObject, totalNutrientsObject);

    return new Recipe(uri, label, image, source, url, yield, calories, totalWeight, totalTime,
    ingredients, nutrients, dietLabels, healthLabels);
  }
}
