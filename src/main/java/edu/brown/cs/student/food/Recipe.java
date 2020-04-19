package edu.brown.cs.student.food;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

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
  private String[] dietLabels;
  private String[] healthLabels;
  private Map<String, double[]> nutrients = new HashMap<>();


  public Recipe(String uri, String label, String image, String source, String url, double yield,
                double calories, double totalWeight, double totalTime, Ingredient[] ingredients,
                Map<String, double[]> nutrients, String[] dietLabels,
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
    this.nutrients = nutrients;
  }

  public Recipe(String uri) {
    this.uri = uri;
  }

  public Ingredient[] getIngredients() {
    return ingredients;
  }

  public String getUri() {
    return uri;
  }

  public double[] getNutrientVals(String nutrientCode) {
    return this.nutrients.get(nutrientCode);
  }

}