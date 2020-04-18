package edu.brown.cs.student.food;


import com.google.gson.annotations.SerializedName;

/**
 * 
 * Class comment.
 *
 */
public class Recipe {
  private String uri;
  private String label;
  private String image;
  private String source;
  private String url;
  private double yield;
  private double calories;
  private double totalWeight;
  private double totalTime;
  private Ingredient[] ingredients;
  private NutrientInfo[] totalNutrients;
  private NutrientInfo[] totalDaily;
  private String[] dietLabels;
  private String[] healthLabels;


  public Recipe(String uri, String label, String image, String source, String url, double yield,
                double calories, double totalWeight, double totalTime, Ingredient[] ingredients,
                NutrientInfo[] totalNutrients, NutrientInfo[] totalDaily, String[] dietLabels,
                String[] healthLabels) {
    this.uri = uri;
    this.label = label;
    this.image = image;
    this.source = source;
    this.url = url;
    this.yield = yield;
    this.calories = calories;
    this.totalWeight = totalWeight;
    this.totalTime = totalTime;
    this.ingredients = ingredients;
    this.dietLabels = dietLabels;
    this.healthLabels = healthLabels;
    this.totalNutrients = totalNutrients;
    this.totalDaily = totalDaily;
  }
  public Ingredient[] getIngredients() {
    return ingredients;
  }

  public String getUri() {
    return uri;
  }


  public Recipe(String uri) {
    this.uri = uri;
  }
  
//  /**
//   * Loads in attributes of a Recipe.
//   * @param name
//   * @param num
//   * @param diet
//   * @param health
//   * @param cuisine
//   * @param meal
//   * @param dish
//   * @param cals
//   * @param time
//   */
//  public void loadRecipe(String name, int num, String diet, String health, String cuisine, String meal, String dish, double cals, double time) {
//    recipeName = name;
//    numIngredients = num;
//    dietLabel = diet;
//    healthLabel = health;
//    cuisineType = cuisine;
//    mealType = meal;
//    dishType = dish;
//    calories = cals;
//    timeRange = time;
//  }

}