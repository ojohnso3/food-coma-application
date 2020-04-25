package edu.brown.cs.student.food;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * This class stores the data for a recipe, received from the Edamam api.
 *
 */
public class Recipe {
  /**
   * nutrients -  Map from a nutrient code to an array of the total daily value and the total
   * nutrient value of the Recipe.
   */
  private String uri;
  private String label;
  private String image;
  private String source;
  private String url;
  private double yield;
  private double calories;
  private double totalWeight;
  private double totalTime;
  private List<Ingredient> ingredients;
  private String[] dietLabels;
  private String[] healthLabels;
  private Map<String, double[]> nutrients = new HashMap<>();


  public Recipe(String uri, String label, String image, String source, String url, double yield,
                double calories, double totalWeight, double totalTime, List<Ingredient> ingredients,
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

  public Recipe(String uri, String label, String image, String source, String url, double yield,
                double calories, double totalWeight, double totalTime, List<Ingredient> ingredients,
                Map<String, double[]> nutrients) {
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
    this.nutrients = nutrients;
  }

  public Recipe(String uri) {
    this.uri = uri;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getUri() {
    return uri;
  }

  public double[] getNutrientVals(String nutrientCode) {
    return this.nutrients.get(nutrientCode);
  }

  public String prepareForInsert() {
    return "\"" + uri + "\",\"" + label + "\",\"" + image + "\",\"" + source + "\",\"" + url
        + "\"," + yield + "," + calories + "," + totalWeight + "," + totalTime;
  }

  public String getLabel(){
    return label;
  }
  public String getUrl(){
    return url;
  }
  public String getCompactUri() {
    Pattern load = Pattern.compile("#recipe_(.+)");
    Matcher matchUri = load.matcher(this.getUri());
    if (matchUri.find()) {
      return matchUri.group(1);
    } else {
      return "";
    }
  }

}