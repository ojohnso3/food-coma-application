package edu.brown.cs.student.gui;

import com.google.gson.Gson;
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
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * 
 * Class comment.
 *
 */
public class Gui {
  
  private static final Gson GSON = new Gson();
  
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
//    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/foodCOMA", new FrontHandler(), freeMarker);
    Spark.get("/setup", new SetupHandler(), freeMarker);
    Spark.post("/login", new LoginHandler());
    // more routes (post too!)
    Spark.get("/results", new SubmitHandler(), freeMarker);
    Spark.get("/recipe/:recipeuri", new RecipeHandler(), freeMarker);

  }
  
  // handlers for gui that interact w/html and javascript
  
  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      List<Recipe> recipeList = new ArrayList<Recipe>();
      Recipe tempRecp = new Recipe("0000");
      Recipe tempRecpO = new Recipe("0001");
      Recipe tempRecpT = new Recipe("0002");

      recipeList.add(tempRecp);
      recipeList.add(tempRecpO);
      recipeList.add(tempRecpT);

      Map<String, Object> variables = ImmutableMap.of("title",
          "foodCOMA Query", "recipeList", recipeList);
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
      Recipe tempRecpO = new Recipe("0001");
      Recipe tempRecpT = new Recipe("0002");

      recipeList.add(tempRecp);
      recipeList.add(tempRecpO);
      recipeList.add(tempRecpT);

      // replace default with new String output
      Map<String, Object> variables = ImmutableMap.of("title", "foodCOMA Query", "recipeList", recipeList);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private static class RecipeHandler implements TemplateViewRoute{
    RecipeHandler(){
    }
    @Override
    public ModelAndView handle(Request req, Response res){
      Map<String, Object> variables = ImmutableMap.of("title","Recipe", "recipeList", new ArrayList<Recipe>());
      return new ModelAndView(variables, "recipe.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class SetupHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Login", "output", "Login Validity:");
      return new ModelAndView(variables, "login.ftl");
    }
  }
  
  /**
   * Handles the functionality of printing out the result of the Stars algorithms.
   *
   */
  private static class LoginHandler implements Route {

    LoginHandler() {
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap map = req.queryMap();
      String input1 = map.value("text1");
      String input2 = map.value("text2");
      System.out.println(input1);
      System.out.println(input2);
      
      boolean valid = checkUser(input1, input2);
      String output = checkValid(valid);
      
        //TODO: create an immutable map using the suggestions
      Map<String, Object> variables = ImmutableMap.of("title",
          "Login", "output", output);

        //TODO: return a Json of the suggestions (HINT: use the GSON instance)
      System.out.println("HREEHEHRHEHEH" + GSON.toJson(variables));
      return GSON.toJson(variables);
      
    }
    
    public boolean checkUser(String username, String password) {
      if (username.equals(password)) {
        return true;
      } else {
        return false;
      }
    }
    
    private String checkValid(boolean check) {
      if (check) {
        return "Valid username!";
      } else {
        return "Invalid username. Try again.";
      }
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