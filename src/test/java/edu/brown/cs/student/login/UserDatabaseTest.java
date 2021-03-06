package edu.brown.cs.student.login;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Class that contains functions to test the methods in UserDatabase.
 */
public class UserDatabaseTest {

  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/login/test.csv";
  private User user1;
  private User user2;

  /**
   * Function to setup objects to test the methods in UserDatabase.
   */
  @Test
  public void testAll() {
    try {
      UserDatabase.loadDatabase("data/userDatabase.sqlite3");
      RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
      NutrientInfo.createNutrientsList();
      String user1name = "3000";
      String user2name = "2000";
      this.user1 = new User(user1name, "password", PATH_CSV);
      this.user2 = new User(user2name, "password", PATH_CSV);

      this.testInsertUser();
      this.testCheckUsername();
      this.testInsertToRestriction();
      this.testInsertToPrevRecipe();
      this.testInsertToNutrient();

      UserDatabase.deleteUser("3000");
      UserDatabase.deleteUser("3000");
      UserDatabase.deleteUser("2000");
    } catch (AccountException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  /**
   * Function to test the checkUsername method in UserDatabase.
   */
  public void testCheckUsername() {
    try {
      assertTrue(UserDatabase.checkUsername(this.user1.getUsername()));
      assertTrue(UserDatabase.checkUsername(this.user2.getUsername()));
      assertFalse(UserDatabase.checkUsername("aslkjfa;afljksda"));
      assertFalse(UserDatabase.checkUsername(""));
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * Function to test the insertUser method in UserDatabase.
   */
  public void testInsertUser() {
    try {
      UserDatabase.insertUser(this.user1);
      User user1Test = UserDatabase.getUser(this.user1.getUsername());
      assertEquals(this.user1, user1Test);
      UserDatabase.insertUser(this.user2);
      User user2Test = UserDatabase.getUser(this.user2.getUsername());
      assertEquals(this.user2, user2Test);

      assertThrows(AccountException.class, () -> {UserDatabase.insertUser(this.user2);});
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (AccountException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    }

  }

  /**
   * Function to test the insertToRestriction method in UserDatabase.
   */
  public void testInsertToRestriction() {
    try {
      UserDatabase.insertToRestriction(this.user1.getUsername(), "peanut-free");
      User user1Test = UserDatabase.getUser(this.user1.getUsername());
      assertEquals("peanut-free", user1Test.getDietaryRestrictions().get(0));

      UserDatabase.insertToRestriction(this.user2.getUsername(), "a");
      User user2Test = UserDatabase.getUser(this.user2.getUsername());
      assertEquals("a", user2Test.getDietaryRestrictions().get(0));

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (AccountException e) {
      e.printStackTrace();
    }
  }

  /**
   * Function to test the insertToPrevRecipe method in UserDatabase.
   */
  public void testInsertToPrevRecipe() {
    try {
      Recipe r1 = RecipeDatabase.getRecipeFromURI(
          "http://www.edamam.com/ontologies/edamam.owl#recipe_5050a95f9bf2b5949752340d2febc1be");
      UserDatabase.insertToPrevRecipe(this.user1.getUsername(), r1.getUri());
      User user1Test = UserDatabase.getUser(this.user1.getUsername());
      assertEquals(r1, user1Test.getPreviousRecipes().get(0));

      Recipe r2 = RecipeDatabase.getRecipeFromURI(
          "http://www.edamam.com/ontologies/edamam.owl#recipe_60bf703e6215cac8498a172f792321d7");
      UserDatabase.insertToPrevRecipe(this.user2.getUsername(), r2.getUri());
      User user2Test = UserDatabase.getUser(this.user2.getUsername());
      assertEquals(r2, user2Test.getPreviousRecipes().get(0));

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (AccountException e) {
      e.printStackTrace();
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  /**
   * Function to test the insertToNutrient method in UserDatabase.
   */
  public void testInsertToNutrient() {
    try {
      UserDatabase.insertToNutrients(this.user1.getUsername(), "peanut-free");
      User user1Test = UserDatabase.getUser(this.user1.getUsername());
      assertEquals("peanut-free", user1Test.getNutrients().get(0));

      UserDatabase.insertToNutrients(this.user2.getUsername(), "a");
      User user2Test = UserDatabase.getUser(this.user2.getUsername());
      assertEquals("a", user2Test.getNutrients().get(0));

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (APIException e) {
      e.printStackTrace();
    } catch (AccountException e) {
      e.printStackTrace();
    }
  }
}
