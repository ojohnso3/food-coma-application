package edu.brown.cs.student.database;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.Ingredient;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Class to test methods contained in the RecipeDatabase class
 */
public class RecipeDatabaseTest {

  Recipe r2;
  Recipe r3;
  Recipe apiRecipe;

  /**
   * Function to set up the recipe fields and call other test functions.
   */
  @Test
  public void testAll() {
//    List<Ingredient> ingredients = new ArrayList<>();
//    Ingredient i = new Ingredient("text", 0.0);
//    ingredients.add(i);
//
//    Map<String, double[]> nutrients = new HashMap<>();
//    double[] testVal = new double[2];
//    testVal[0] = 1.0;
//    testVal[1] = 2.0;
//    nutrients.put("CA", testVal);
//
//    Random rand = new Random();
//    String uri = rand.nextInt(1000) + "";
//    this.r2 = new Recipe(uri, "label", "image", "source", "url", 0.0,
//        0.0, 0.0, 0.0, ingredients, nutrients);
//
//    int num = rand.nextInt(1000);
//    this.r3 = new Recipe("http://edamam.api.com/Ontology#" + num, "label", "image" , "source",
//        "url", 0.0, 0.0, 0.0, 0.0, ingredients, nutrients);
//
//    this.testInsertRecipe(uri, num);
//    this.testGetRecipeFromUri();
//    this.testQueryAlreadyInDb();
  }

  /**
   * Function to test the insertRecipe method in RecipeDatabase.
   */
  public void testInsertRecipe(String uri, int num) {
    try {
      RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
      NutrientInfo.createNutrientsList();

      //Test w/giving all fields to Recipe constructor.
      RecipeDatabase.insertRecipe(r2);
      Recipe rTest2 = RecipeDatabase.getRecipeFromURI(uri);
      assertEquals(r2, rTest2);
      assertEquals(r2.getImage(), rTest2.getImage());
      assertEquals(r2.getNutrientVals("CA")[0], rTest2.getNutrientVals("CA")[0], 0.00001);

      //Test w/giving all fields to constructor and uri that needs to be reformatted.
      RecipeDatabase.insertRecipe(r3);
      Recipe rTest3 = RecipeDatabase.getRecipeFromURI("http://edamam.api.com/Ontology#" + num);
      assertEquals(this.r3, rTest3);
      assertEquals(this.r3.getImage(), rTest3.getImage());
      assertEquals(this.r3.getNutrientVals("CA")[0], rTest3.getNutrientVals("CA")[0], 0.00001);


      //Test w/giving a Recipe that is already in the database.
      assertThrows(SQLException.class, () -> {RecipeDatabase.insertRecipe(r2);});

      //should go through and delete all of these rows at the end.

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Function to test the getRecipeFromUri method in RecipeDatabase.
   */
  public void testGetRecipeFromUri() {
    try {
      RecipeDatabase.loadDatabase("recipeDatabase.sqlite3");
      NutrientInfo.createNutrientsList();

      //Test with regular uris.
      Recipe r2Test = RecipeDatabase.getRecipeFromURI(r2.getUri());
      Recipe r3Test = RecipeDatabase.getRecipeFromURI(r3.getUri());

      assertEquals(r2Test, r2);
      assertEquals(r3Test, r3);

      //Test when uri is not in database and API has to be accessed.
      this.apiRecipe = RecipeDatabase.getRecipeFromURI(
          "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6");

      assertEquals("http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
          apiRecipe.getUri());
      assertNotNull(apiRecipe.getIngredients());
      assertNotNull(apiRecipe.getNutrientVals("CA"));
      assertNotNull(apiRecipe.getLabel());

      //Test when uri is not in database and not in API.
      assertThrows(APIException.class, () -> {RecipeDatabase.getRecipeFromURI("x");});

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    }
  }


  public void testQueryAlreadyInDb(){
    try {
      System.out.println("ALREADY IN DATABASE? " + RecipeDatabase.checkQueryInDatabase("sauce"));
    } catch (SQLException e) {
      System.out.println("SQLException in testing");
    }
  }


}
