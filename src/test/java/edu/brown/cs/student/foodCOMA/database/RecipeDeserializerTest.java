package edu.brown.cs.student.foodCOMA.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import edu.brown.cs.student.database.RecipeDeserializer;
import edu.brown.cs.student.food.Recipe;
import org.junit.Test;

public class RecipeDeserializerTest {
  @Test
  public void testDeserializer() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonDeserializer<Recipe> recipeDeserializer = new RecipeDeserializer();
    gsonBuilder.registerTypeAdapter(Recipe.class, recipeDeserializer);
    Gson gson = gsonBuilder.create();

    String json = "{\"uri\": \"http://hello\", \"label\": \"goodbye\", \"image\": \"http://google.com/testimage.jpeg\"," +
        "";

  }
}
