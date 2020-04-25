package edu.brown.cs.student.login;

import edu.brown.cs.student.food.Ingredient;
import edu.brown.cs.student.food.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for a User, storing username, hashed password, and salt used for hashing to raw password
 * in csv upon construction and holding in its fields the user's username, saved recipes, and
 * dietary restrictions.
 */
public class User {
  /**
   * username - the unique username of the user.
   * previousRecipes - a list of Recipe objects the user has previously accessed.
   * dietaryRestrictions - a list of health labels.
   * nutrients - a list of nutrient codes that the user has specified.
   */
  private String username;
  private List<Recipe> previousRecipes;
  private List<String> dietaryRestrictions;
  private List<String> nutrients;

  /**
   * constructors adds login info to db.
   * @param user name
   * @param pass password
   * @throws AccountException on write failure, just propagate message to handle.
   */
  public User(String user, String pass) throws AccountException {
    username = user;
    previousRecipes = new ArrayList<>();
    dietaryRestrictions = new ArrayList<>();
    Accounts.writeLoginInfo(user, pass); // write the login info to our csv
  }

  public User(String user, String pass, String path) throws AccountException {
    username = user;
    previousRecipes = new ArrayList<>();
    dietaryRestrictions = new ArrayList<>();
    Accounts.writeLoginInfo(user, pass, path); // write the login info to any csv (for testing)
  }

  /**
   * username getter.
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Function to return the previousRecipes field.
   * @return - previousRecipes.
   */
  public List<Recipe> getPreviousRecipes() {
    return previousRecipes;
  }


  /**
   * Function to add to the previousRecipes field.
   * @param recipe
   */
  public void addToPreviousRecipes(Recipe recipe) {
    previousRecipes.add(recipe);
  }

  
  /**
   * Comment.
   * @param allRestrictions
   */
  public void setRestrictions(String allRestrictions) {
    // parse and add to list
  }
  
  /**
   * Comment.
   * @param label - the diet or health label to be added to dietaryRestrictions.
   */
  public void addToRestrictions(String label) {
    dietaryRestrictions.add(label);
  }

  /**
   * Function to get the dietary restrictions of a user.
   * @return - the dietaryRestrictions field.
   */
  public List<String> getDietaryRestrictions() {
    //returning defensive copy.
    return new ArrayList<>(this.dietaryRestrictions);
  }

  /**
   * getter.
   * @return nutrients
   */
  public List<String> getNutrients() {
    return this.nutrients;
  }

  /**
   * setter.
   * @param n - nutrients
   */
  public void setNutrients(List<String> n) {
    this.nutrients = n;
  }
}