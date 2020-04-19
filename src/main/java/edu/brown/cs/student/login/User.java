package edu.brown.cs.student.login;

import edu.brown.cs.student.food.*;

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
  
  public User(String user, String pass) throws AccountException {
    username = user;
    previousRecipes = new ArrayList<>();
    dietaryRestrictions = new ArrayList<>();

    PrintWriter writer;
    try {
      writer = new PrintWriter(Accounts.getLoginInfoFile());
    } catch (FileNotFoundException e) {
      throw new AccountException("login info file corrupted");
    }
    //
    byte[] salt = Accounts.generateSalt();
    byte[] hash = Accounts.hashPasswordPBKDF2(pass, salt);
    writer.write(user + "," + hash + "," + salt);
  }
  

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