package edu.brown.cs.student.database;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import edu.brown.cs.student.food.Ingredient;

import java.lang.reflect.Type;

/**
 * This class is used to provide a deserializer to parse the Edamam Api JSON. It implements the
 * JsonDeserializer interface, which takes in an object of type Ingredient.
 */
public class IngredientDeserializer implements JsonDeserializer<Ingredient> {

  @Override
  public Ingredient deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {

    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String text = jsonObject.get("text").getAsString();
    double weight = jsonObject.get("weight").getAsDouble();
    return new Ingredient(text, weight);
  }
}
