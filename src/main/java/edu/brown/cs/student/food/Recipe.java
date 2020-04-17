package edu.brown.cs.student.food;


import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

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
  private int yield;
  private float calories;
  private float totalWeight;
  private Ingredient[] ingredients;
  private NutrientInfo[] totalNutrients;
  private NutrientInfo[] totalDaily;
  private String[] dietLabels;
  private String[] healthLabels;

  enum Diet {
    balanced,
    @SerializedName("high-fiber") highFiber,
    @SerializedName("high-protein") highProtein,
    @SerializedName("low-carb") lowCarb,
    @SerializedName("low-fat") lowFat,
    @SerializedName("low-sodium") lowSodium
  }

  enum Health {
    @SerializedName("alcohol-free") alcoholFree,
    @SerializedName("celery-free") celeryFree,
    @SerializedName("crustacean-free") crustaceanFree,
    @SerializedName("dairy-free") dairyFree,
    @SerializedName("egg-free") eggFree,
    @SerializedName("fish-free") fishFree,
    @SerializedName("fodmap-free") fodmapFree,
    @SerializedName("gluten-free") glutenFree,
    @SerializedName("keto-friendly") ketoFriendly,
    kosher,
    @SerializedName("low-potassium") lowPotassium,
    @SerializedName("lupine-free") lupineFree,
    @SerializedName("mustard-free") mustardFree,
    @SerializedName("low-fat-abs") lowFatAbs,
    @SerializedName("no-oil-added") noOilAdded,
    @SerializedName("low-sugar") lowSugar,
    paleo,
    @SerializedName("peanut-free") peanutFree,
    pescatarian,
    @SerializedName("pork-free") porkFree,
    @SerializedName("red-meat-free") redMeatFree,
    @SerializedName("seasame-free") sesameFree,
    @SerializedName("shellfish-free") shellfishFree,
    @SerializedName("soy-free") soyFree,
    @SerializedName("sugar-conscious") sugarConscious,
    @SerializedName("tree-nut-free") treeNutFree,
    vegan,
    vegetarian,
    @SerializedName("wheat-free") wheatFree
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