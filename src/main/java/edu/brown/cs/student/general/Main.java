package edu.brown.cs.student.general;

//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.gui.Gui;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

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

  private String[] args;

  private Main(String[] inputArgs) {
    args = inputArgs;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      Gui gui = new Gui();
      gui.runSparkServer((int) options.valueOf("port"));
    } else {
      try {
        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      NutrientInfo.createNutrientsList();
      try {
        Recipe[] recipes = FieldParser.getRecipesFromQuery("chicken");
        for (int i = 0; i < recipes.length; i++) {
          System.out.println(recipes[i].getUri());
        }
      } catch (IOException | InterruptedException | APIException | SQLException ie) {
        ie.printStackTrace();
      }

    }

    // TODO: add functionality here

  }
  
}
