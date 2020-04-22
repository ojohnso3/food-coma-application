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
    Accounts.writeLoginInfo(user, pass, path); // write the login info to our csv
  }

  /**
   * Comment.
   * @return
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
   * Comment.
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
}