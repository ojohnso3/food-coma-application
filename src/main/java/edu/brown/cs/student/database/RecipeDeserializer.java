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

/**
 * This class is used to provide a deserializer to parse the Edamam Api JSON. It implements the
 * JsonDeserializer interface, which takes in an object of type Ingredient.
 */
public class RecipeDeserializer implements JsonDeserializer<Recipe> {

  /**
   * Helper function for deserialize to parse NutrientInfo objects.
   * @return - the parsed NutrientInfo object.
   */
  private NutrientInfo parseNutrientInfo() {
    return null;
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
    Ingredient[] ingredients = new Ingredient[jsonIngredients.size()];
    for (int k = 0; k < jsonIngredients.size(); k++) { //each element of the array should be an ingredient
      JsonDeserializer<Ingredient> ingredientDeserializer = new IngredientDeserializer();
      gsonBuilder.registerTypeAdapter(Ingredient.class, ingredientDeserializer);
      Gson customGson = gsonBuilder.create();
      Ingredient currIngredient = customGson.fromJson(jsonIngredients.get(k), Ingredient.class); //parse ingredients
      ingredients[k] = currIngredient;
    }

    double calories = jsonObject.get("calories").getAsDouble();
    double totalWeight = jsonObject.get("totalWeight").getAsDouble();
    double totalTime = jsonObject.get("totalTime").getAsDouble();

    JsonElement nutrientsArray = jsonObject.get("totalNutrients");
    JsonArray jsonNutrients = nutrientsArray.getAsJsonArray(); //these aren't stored as arrays why did they do this????????????
    NutrientInfo[] totalNutrients = new NutrientInfo[jsonNutrients.size()];
    for (int l = 0; l < jsonNutrients.size(); l++) {
      JsonElement nutrient = jsonNutrients.get(l);
      JsonObject nutrientObject = nutrient.getAsJsonObject();
      String nutrientLabel = nutrientObject.get("label").getAsString();
      double quantity = nutrientObject.get("quantity").getAsDouble();
      String unit = nutrientObject.get("unit").getAsString();
      totalNutrients[l] = new NutrientInfo(nutrientLabel, quantity, unit);
    }

    JsonElement dailyArray = jsonObject.get("totalDaily");
    JsonArray jsonDaily = dailyArray.getAsJsonArray();
    NutrientInfo[] totalDaily = new NutrientInfo[jsonNutrients.size()];
    for (int l = 0; l < jsonDaily.size(); l++) {
      JsonElement nutrient = jsonDaily.get(l);
      JsonObject nutrientObject = nutrient.getAsJsonObject();
      String nutrientLabel = nutrientObject.get("label").getAsString();
      double quantity = nutrientObject.get("quantity").getAsDouble();
      String unit = nutrientObject.get("unit").getAsString();
      totalDaily[l] = new NutrientInfo(nutrientLabel, quantity, unit);
    }

    return new Recipe(uri, label, image, source, url, yield, calories, totalWeight, totalTime,
        ingredients, totalNutrients, totalDaily, dietLabels, healthLabels);
  }
}
