package edu.brown.cs.student.login;

import edu.brown.cs.student.food.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Class comment.
 *
 */
public class User {
  private String username;
  private List<Recipe> previousRecipes;
  private List<Ingredient> dietaryRestrictions;
  
  public User(String user, String pass) throws LoginException {
    username = user;
    previousRecipes = new ArrayList<>();
    dietaryRestrictions = new ArrayList<>();

    PrintWriter writer = null;
    try {
      writer = new PrintWriter(Accounts.getLoginInfoFile());
    } catch (FileNotFoundException e) {
      throw new LoginException("login info file corrupted");
    }
    //
    String salt = "";
    // Hash a password for the first time
//    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
    writer.write(user + "," + pass + "," + salt);
  }
  
  // TODO: figure out how to encode/store passwords

  
  /**
   * Comment.
   * @return
   */
  public String getUsername() {
    return username;
  }
  
  /**
   * Comment.
   * @param unencoded
   */
  public void setPassword(String unencoded) {
    // encode
  }
  
  /**
   * Comment.
   * @return
   */
  public String getPassword() {
    return null;
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