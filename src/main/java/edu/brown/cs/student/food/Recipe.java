package edu.brown.cs.student.food;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;

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