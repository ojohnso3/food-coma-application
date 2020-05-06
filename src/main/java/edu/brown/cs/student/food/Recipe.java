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

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getUri() {
    return uri;
  }

  public double[] getNutrientVals(String nutrientCode) {
    if (this.nutrients == null) {
      return null;
    }
    return this.nutrients.get(nutrientCode);
  }

  public Map<String, double[]> getNutrientsMap() {
    return nutrients;
  }

  public String prepareForInsert() {
    label = label.replace("\"", "");
    return "\"" + uri + "\",\"" + label + "\",\"" + image + "\",\"" + source + "\",\"" + url
        + "\"," + yield + "," + calories + "," + totalWeight + "," + totalTime;
  }

  public String getLabel() {
    return label;
  }

  public String getImage() {
    return image;
  }

  public String getUrl() {
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

  public List<String> getDietLabels() {
    return dietLabels;
  }

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

  public boolean containsLabel(String lab) {
    boolean ret = false;
    for (String item : healthLabels) {
      if (item.equals(lab)) {
        ret = true;
        break;
      }
    }
    for (String item : dietLabels) {
      if (item.equals(lab)) {
        ret = true;
        break;
      }
    }
    return ret;
  }
}
