package edu.brown.cs.student.general;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.gui.Gui;
import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private final String[] args;

  private Main(String[] inputArgs) {
    args = inputArgs;
  }

  private void run() {
    // initialize users maps
    try {
      Accounts.initializeMap();
      NutrientInfo.createNutrientsList();
    } catch (AccountException | FileNotFoundException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      Gui gui = new Gui();
      gui.runSparkServer((int) options.valueOf("port"));
    } else {
//      try {
////        NutrientInfo.createNutrientsList();
////        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
////        RecipeDatabase.testDatabaseFile();
////        Recipe r = RecipeDatabase.getRecipeFromURI("1234");
////        UserDatabase.loadDatabase("data/userDatabase.sqlite3");
////        UserDatabase.testDatabaseFile();
//      } catch (FileNotFoundException e) {
//        e.printStackTrace();
//      } catch (ClassNotFoundException e) {
//        e.printStackTrace();
//      } catch (SQLException e) {
//        e.printStackTrace();
////      } catch (InterruptedException e) {
//////        e.printStackTrace();
//////      } catch (APIException e) {
//////        e.printStackTrace();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
      try {
        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
        NutrientInfo.createNutrientsList();
        List<String> dietaryRestrictions = new ArrayList<>();
        Map<String, String[]> paramsMap = new HashMap<>();
        paramsMap.put("x", new String[] {"x"});
        Recipe[] recipes = FieldParser.getRecipesFromQuery("pasta", dietaryRestrictions, paramsMap);
        for (Recipe recipe : recipes) {
          System.out.println(recipe.getUri());
        }
      } catch (IOException | InterruptedException | APIException | SQLException
              | ClassNotFoundException ie) {
        ie.printStackTrace();
      }
    }
    // TODO: add functionality here
  }
}
