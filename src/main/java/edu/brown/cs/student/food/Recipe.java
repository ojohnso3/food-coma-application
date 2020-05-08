package edu.brown.cs.student.food;

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
  private final String uri;
  private String label;
  private final String image;
  private final String source;
  private final String url;
  private final double yield;
  private final double calories;
  private final double totalWeight;
  private final double totalTime;
  private final List<Ingredient> ingredients;
  private List<String> dietLabels;
  private List<String> healthLabels;
  private final Map<String, double[]> nutrients;


  /**
   * This is the constructor for our recipe. It creates a recipe with the given fields.
   * @param uri This recipe's URI
   * @param label This recipe's title (label)
   * @param image This recipe's image URL
   * @param source The source of the recipe URL
   * @param url The Edamam Recipe URL
   * @param yield The yield of this recipe
   * @param calories The calories of this recipe
   * @param totalWeight The total weight of this recipe
   * @param totalTime The total time of this recipe
   * @param ingredients The ingredients of this recipe
   * @param nutrients The nutrients of this recipe
   * @param dietLabels The diet labels of this recipe
   * @param healthLabels The health labels of this recipe
   */
  public Recipe(String uri, String label, String image, String source, String url, double yield,
                double calories, double totalWeight, double totalTime, List<Ingredient> ingredients,
                Map<String, double[]> nutrients, List<String> dietLabels,
                List<String> healthLabels) {
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

  /**
   * This is another constructor for our Recipe. It differs because it has less.
   * fields due to testing
   * @param uri This recipe's URI
   * @param label This recipe's title (label)
   * @param image This recipe's image URL
   * @param source The source of the recipe URL
   * @param url The Edamam Recipe URL
   * @param yield The yield of this recipe
   * @param calories The calories of this recipe
   * @param totalWeight The total weight of this recipe
   * @param totalTime The total time of this recipe
   * @param ingredients The ingredients of this recipe
   * @param nutrients The nutrients of this recipe
   */
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

  /**
   * This method gets the ingredients in this recipe.
   * @return The ingredients
   */
  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * This method gets the URI in this recipe.
   * @return The URI of the recipe
   */
  public String getUri() {
    return uri;
  }

  /**
   * This method gets the Nutrient Values in this recipe.
   * @param nutrientCode the nutrient code searching for
   * @return The nutrient of the recipe
   */
  public double[] getNutrientVals(String nutrientCode) {
    if (this.nutrients == null) {
      return null;
    }
    return this.nutrients.get(nutrientCode);
  }

  /**
   * Gets the nutrient map of this recipe.
   * @return The nutrient map
   */
  public Map<String, double[]> getNutrientsMap() {
    return nutrients;
  }

  /**
   * This method prepares the recipe for insertion into the DB.
   * @return The string of the insertion
   */
  public String prepareForInsert() {
    label = label.replace("\"", "");
    return "\"" + uri + "\",\"" + label + "\",\"" + image + "\",\"" + source + "\",\"" + url
        + "\"," + yield + "," + calories + "," + totalWeight + "," + totalTime;
  }

  /**
   * This method gets the name of the recipe.
   * @return The name of the recipe
   */
  public String getLabel() {
    return label;
  }
  /**
   * This method gets the image of the recipe.
   * @return The image of the recipe
   */
  public String getImage() {
    return image;
  }
  /**
   * This method gets the URL of the recipe.
   * @return The URL of the recipe
   */
  public String getUrl() {
    return url;
  }
  /**
   * This method gets the Compact URL of the recipe for our website's URL generation.
   * @return The URL of the recipe
   */
  public String getCompactUri() {
    Pattern load = Pattern.compile("#recipe_(.+)");
    Matcher matchUri = load.matcher(this.getUri());
    if (matchUri.find()) {
      return matchUri.group(1);
    } else {
      return "";
    }
  }

  /**
   * Gets the diet labels of this recipe.
   * @return The diet labels
   */
  public List<String> getDietLabels() {
    return dietLabels;
  }

  /**
   * Gets the health labels of this recipe.
   * @return The health labels
   */
  public List<String> getHealthLabels() {
    return healthLabels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Recipe)) {
      return false;
    }
    Recipe recipe = (Recipe) o;
    return uri.equals(recipe.uri);
  }

  @Override
  public int hashCode() {
    return this.uri.hashCode();
  }
}
