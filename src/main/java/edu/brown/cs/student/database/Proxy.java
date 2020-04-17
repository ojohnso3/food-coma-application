package edu.brown.cs.student.database;

import edu.brown.cs.student.food.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 
 * Class comment.
 *
 */
public class Proxy {
  
  private LoadingCache<String, Recipe> recipeCache;
  private LoadingCache<Ingredient, List<Recipe>> ingredientCache;
  private final int CACHE_SIZE = 50000;
  private RecipeDatabase database;

  public Proxy(RecipeDatabase db) {
    database = db;
  }
  
  
//  /**
//   * Loads in database.
//   */
//  public void setCaches() {
//    recipeCache = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE)
//        .build(new CacheLoader<String, Recipe>() {
//          @Override
//          public Recipe load(String id) throws Exception {
//              return database.getRecipeByID(id);
//          }
//        });
//    ingredientCache = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE)
//        .build(new CacheLoader<Ingredient, List<Recipe>>() {
//          @Override
//          public List<Recipe> load(Ingredient ingredient) throws Exception {
//              return database.getRecipeListByIngriedent(ingredient);
//          }
//        });
//  }
  
  
  /**
   * Gets a Recipe from id from the database.
   * @param recipeID string id that corresponds to a recipe
   * @return Recipe object
   */
  public Recipe getRecipeByID(String recipeID) {
    try {
      return recipeCache.get(recipeID);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Method comment.
   * @param ingredient
   * @return
   */
  public List<Recipe> getRecipeListByIngredient(Ingredient ingredient) {
    try {
      return ingredientCache.get(ingredient);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  
  
}