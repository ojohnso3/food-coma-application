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
  private String username;
  private List<Recipe> previousRecipes;
  private List<Ingredient> dietaryRestrictions;
  private List<String> nutrients;

  /**
   * Constructors for adding a new User.
   * @param user name
   * @param password password
   * @throws AccountException on write failure, just propagate message to handle.
   */
  public User(String user, String password) throws AccountException {
    this.username = user;
    this.previousRecipes = new ArrayList<>();
    this.dietaryRestrictions = new ArrayList<>();
    Accounts.writeLoginInfo(user, password); // write the login info to our csv
  }
  // testing constructor
  public User(String username, String password, String path) throws AccountException {
    this.username = username;
    this.previousRecipes = new ArrayList<>();
    this.dietaryRestrictions = new ArrayList<>();
    // write the login info to any csv (for testing)
    Accounts.writeLoginInfo(username, password, path);
  }

  /**
   * Constructor for recreating a User from data files.
   * @param username - name
   */
  public User(String username) {
    this.username = username;
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
   * @param ingredient
   */
  public void addToRestrictions(Ingredient ingredient) {
    dietaryRestrictions.add(ingredient);
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