package edu.brown.cs.student.food;


/**
 * 
 * Class comment.
 *
 */
public class Recipe {
  private String recipeID;
  private String recipeName;
  private int numIngredients;
  private String dietLabel;
  private String healthLabel;
  private String cuisineType;
  private String mealType;
  private String dishType;
  private double calories;
  private double timeRange;


  
  
  public Recipe(String id) {
    recipeID = id;
  }
  
  public void loadRecipe(String name, int num, String diet, String health, String cuisine, String meal, String dish, double cals, double time) {
    recipeName = name;
    numIngredients = num;
    dietLabel = diet;
    healthLabel = health;
    cuisineType = cuisine;
    mealType = meal;
    dishType = dish;
    calories = cals;
    timeRange = time;
  }
  
  
  
  
  
}