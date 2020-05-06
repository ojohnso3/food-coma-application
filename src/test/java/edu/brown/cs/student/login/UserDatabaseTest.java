//package edu.brown.cs.student.login;
//
//import edu.brown.cs.student.database.APIException;
//import edu.brown.cs.student.database.RecipeDatabase;
//import edu.brown.cs.student.food.NutrientInfo;
<<<<<<< HEAD
//import edu.brown.cs.student.food.Recipe;
=======
>>>>>>> 6d1cc864cf38d0c632a2796e03489e62aa4ccc50
//import org.junit.Test;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Random;
//
//import static org.junit.Assert.*;
//
///**
// * Class that contains functions to test the methods in UserDatabase.
// */
//public class UserDatabaseTest {
//
//  private static final String PATH_CSV = "src/test/java/edu/brown/cs/student/login/test.csv";
//  private User user1;
//  private User user2;
//
//  /**
//   * Function to setup objects to test the methods in UserDatabase.
//   */
//  @Test
//  public void testAll() {
//    try {
//      UserDatabase.loadDatabase("data/userDatabase.sqlite3");
//      RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
//      NutrientInfo.createNutrientsList();
<<<<<<< HEAD
//      String user1name = "1000";
//      String user2name = "2000";
=======
//      Random rand = new Random();
//      String user1name = rand.nextInt(1000) + "";
//      String user2name = rand.nextInt(1000) + "";
>>>>>>> 6d1cc864cf38d0c632a2796e03489e62aa4ccc50
//      this.user1 = new User(user1name, "password", PATH_CSV);
//      this.user2 = new User(user2name, "password", PATH_CSV);
//
//      this.testInsertUser();
//      this.testCheckUsername();
//      this.testInsertToRestriction();
//      this.testInsertToPrevRecipe();
//      this.testInsertToNutrient();
//
//      UserDatabase.deleteUser(user1.getUsername());
//      UserDatabase.deleteUser(user2.getUsername());
//    } catch (AccountException e) {
//      e.printStackTrace();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }
//
//  }
//
//  /**
//   * Function to test the checkUsername method in UserDatabase.
//   */
//  public void testCheckUsername() {
//    try {
//      assertTrue(UserDatabase.checkUsername(this.user1.getUsername()));
//      assertTrue(UserDatabase.checkUsername(this.user2.getUsername()));
//      assertFalse(UserDatabase.checkUsername("aslkjfa;afljksda"));
//      assertFalse(UserDatabase.checkUsername(""));
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//  }
//
//  /**
//   * Function to test the insertUser method in UserDatabase.
//   */
//  public void testInsertUser() {
//    try {
//      UserDatabase.insertUser(this.user1);
//      User user1Test = UserDatabase.getUser(this.user1.getUsername());
//      assertEquals(this.user1, user1Test);
//      UserDatabase.insertUser(this.user2);
//      User user2Test = UserDatabase.getUser(this.user2.getUsername());
//      assertEquals(this.user2, user2Test);
//
//      assertThrows(AccountException.class, () -> {UserDatabase.insertUser(this.user2);});
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } catch (AccountException e) {
//      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } catch (APIException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (NullPointerException npe) {
//      npe.printStackTrace();
//    }
//
//  }
//
//  /**
//   * Function to test the insertToRestriction method in UserDatabase.
//   */
//  public void testInsertToRestriction() {
//    try {
//      UserDatabase.insertToRestriction(this.user1.getUsername(), "peanut-free");
//      User user1Test = UserDatabase.getUser(this.user1.getUsername());
//      assertEquals("peanut-free", user1Test.getDietaryRestrictions().get(0));
//
//      UserDatabase.insertToRestriction(this.user2.getUsername(), "a");
//      User user2Test = UserDatabase.getUser(this.user2.getUsername());
//      assertEquals("a", user2Test.getDietaryRestrictions().get(0));
//
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } catch (APIException e) {
//      e.printStackTrace();
//    } catch (AccountException e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * Function to test the insertToPrevRecipe method in UserDatabase.
//   */
//  public void testInsertToPrevRecipe() {
<<<<<<< HEAD
//    try {
//      Recipe r1 = RecipeDatabase.getRecipeFromURI("684");
//      UserDatabase.insertToPrevRecipe(this.user1.getUsername(), r1.getUri());
//      User user1Test = UserDatabase.getUser(this.user1.getUsername());
//      assertEquals(r1, user1Test.getPreviousRecipes().get(0));
//
//      Recipe r2 = RecipeDatabase.getRecipeFromURI("295");
//      UserDatabase.insertToPrevRecipe(this.user2.getUsername(), r2.getUri());
//      User user2Test = UserDatabase.getUser(this.user2.getUsername());
//      assertEquals(r2, user2Test.getPreviousRecipes().get(0));
//
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } catch (APIException e) {
//      e.printStackTrace();
//    } catch (AccountException e) {
//      e.printStackTrace();
//    } catch (IndexOutOfBoundsException e) {
//      e.printStackTrace();
//    }
=======
////    try {
////      UserDatabase.insertToPrevRecipe(this.user1.getUsername(), "987");
////      User user1Test = UserDatabase.getUser(this.user1.getUsername());
////      assertEquals("987", user1Test.getPreviousRecipes().get(1));
////
////      UserDatabase.insertToPrevRecipe(this.user2.getUsername(), "http://edamam.api.com/Ontology#752");
////      User user2Test = UserDatabase.getUser(this.user2.getUsername());
////      assertEquals("http://edamam.api.com/Ontology#752", user2Test.getPreviousRecipes().get(0));
////
////    } catch (SQLException e) {
////      e.printStackTrace();
////    } catch (IOException e) {
////      e.printStackTrace();
////    } catch (InterruptedException e) {
////      e.printStackTrace();
////    } catch (APIException e) {
////      e.printStackTrace();
////    } catch (AccountException e) {
////      e.printStackTrace();
////    }
>>>>>>> 6d1cc864cf38d0c632a2796e03489e62aa4ccc50
//  }
//
//  /**
//   * Function to test the insertToNutrient method in UserDatabase.
//   */
//  public void testInsertToNutrient() {
//    try {
//      UserDatabase.insertToNutrients(this.user1.getUsername(), "peanut-free");
//      User user1Test = UserDatabase.getUser(this.user1.getUsername());
//      assertEquals("peanut-free", user1Test.getNutrients().get(0));
//
//      UserDatabase.insertToNutrients(this.user2.getUsername(), "a");
//      User user2Test = UserDatabase.getUser(this.user2.getUsername());
//      assertEquals("a", user2Test.getNutrients().get(0));
//
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } catch (APIException e) {
//      e.printStackTrace();
//    } catch (AccountException e) {
//      e.printStackTrace();
//    }
//  }
//}
