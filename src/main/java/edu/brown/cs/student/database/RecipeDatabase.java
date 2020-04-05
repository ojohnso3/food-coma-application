package edu.brown.cs.student.database;

import edu.brown.cs.student.food.*;
import java.util.List;

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
   */
  public void loadDatabase() {
    
  }
  
  
  /**
   * Gets a Recipe from id from the database.
   * @param recipeID string id that corresponds to a recipe
   * @return Recipe object
   */
  public Recipe getRecipeByID(String recipeID) {
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