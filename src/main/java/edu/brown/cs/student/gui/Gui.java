package edu.brown.cs.student.gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.food.Recipe;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * 
 * Class comment.
 *
 */
public class Gui {
  
  public Gui() {
  }
  
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }
  
  /**
   * Add comment.
   * @param port
   */
  public void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/foodCOMA", new FrontHandler(), freeMarker);
    // more routes (post too!)
    Spark.get("/recipe", new SubmitHandler(), freeMarker);

  }
  
  // handlers for gui that interact w/html and javascript
  
  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "foodCOMA Query", "recipeList", new ArrayList<Recipe>());
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handles the functionality of printing out the result of the Stars algorithms.
   *
   */
  private static class SubmitHandler implements TemplateViewRoute {

    SubmitHandler() {
    }

    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text");
      List<Recipe> recipeList = new ArrayList<Recipe>();

      Recipe tempRecp = new Recipe("0000");
      tempRecp.setName("Temporary Recipe");
      recipeList.add(tempRecp);
      // replace default with new String output
      Map<String, Object> variables = ImmutableMap.of("title", "foodCOMA Query", "recipeList", recipeList);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
  

}