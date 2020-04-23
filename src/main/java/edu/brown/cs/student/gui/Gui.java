package edu.brown.cs.student.gui;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.food.Ingredient;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.User;
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
//  private static FieldParser fieldParser;
//  private static NutrientInfo nutrientInfo;
  private static final Gson GSON = new Gson();
  
  public Gui() {
//    fieldParser = fp;
//    nutrientInfo = nut;
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
    Spark.get("/search", new SearchHandler(), freeMarker);
    Spark.get("/home", new SetupHandler("home.ftl", "foodCOMA Home"), freeMarker);
    Spark.get("/about", new SetupHandler("about.ftl", "About"), freeMarker);
    Spark.get("/login", new SetupHandler("login.ftl", "Login"), freeMarker);
    Spark.get("/recipe/:recipeuri", new SetupHandler("recipe.ftl", "Recipe Detail"), freeMarker);
    Spark.get("/signup", new SetupHandler("signup.ftl", "Signup"), freeMarker);
    Spark.get("/survey", new SetupHandler("survey.ftl", "New User Survey"), freeMarker);
    
    Spark.post("/logged", new LoginHandler());
    Spark.post("/signed", new SignupHandler());
    // more routes (post too!)

    Spark.get("/results", new SubmitHandler(), freeMarker);
    Spark.post("/recipe/:recipeuri", new RecipeHandler(this));
//    Spark.get("/recipe/:recipeuri", new RecipeHandler());

  }
  
  // handlers for gui that interact w/html and javascript
  
  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class SearchHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      List<Recipe> recipeList = new ArrayList<Recipe>();
      Map<String, Object> variables = ImmutableMap.of("title",
          "Recipe Search", "recipeList", recipeList);
      return new ModelAndView(variables, "search.ftl");
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
      String valFromMap = qm.value("x1");


      Recipe tempRecp = new Recipe("9999");
      Recipe tempRecpO = new Recipe("9999");
      Recipe tempRecpT = new Recipe("9990");

      recipeList.add(tempRecp);
      recipeList.add(tempRecpO);
      recipeList.add(tempRecpT);

      // replace default with new String output
      Map<String, Object> variables = ImmutableMap.of("title", "foodCOMA Query", "recipeList", recipeList);
      return new ModelAndView(variables, "search.ftl");
    }
  }

  private static class RecipeHandler implements Route{
    Gui gui;
    RecipeHandler(Gui g){
      gui = g;
    }
    @Override
    public String handle(Request req, Response res){
      QueryParamsMap qm = req.queryMap();
      String url = qm.value("url");
      System.out.println("The URL is " + url);
      Pattern load = Pattern.compile("http:\\/\\/localhost:.+\\/recipe\\/(.+)");
      String recipeURI = null;
      if(url != null){
        Matcher matchURL = load.matcher(url);
        if(matchURL.find()){
          recipeURI = matchURL.group(1);
          System.out.println("The Recipe URI is " + recipeURI);
        }
      }

      List<Ingredient> sampleIngredients = new ArrayList<Ingredient>();
      Ingredient tofu = new Ingredient("Tofu",200);
      Ingredient sauce = new Ingredient("Sauce",100);
      sampleIngredients.add(tofu);
      sampleIngredients.add(sauce);
      Map<String, double[]> nutrientList = new HashMap<String, double[]>();
      nutrientList.put("Sugar", new double[2]);
      nutrientList.put("Salt", new double[2]);
      String[] dietLabelList = new String[2];
      dietLabelList[0] = "Diet Label 1";
      dietLabelList[1] = "Diet Label 2";
      String[] healthLabelList = new String[2];
      healthLabelList[0] = "Health label 0";
      healthLabelList[1] = "Health label 1";

      Recipe recpOne = new Recipe("0100", "Tofu 1", "image","source", "url", 0.0,9.9,55.55,10.00,sampleIngredients, nutrientList,
              dietLabelList, healthLabelList);

      Recipe recpTwo = new Recipe("0200", "Tofu 2", "image","source", "url", 0.0,9.9,55.55,10.00,sampleIngredients, nutrientList,
              dietLabelList, healthLabelList);
      Recipe recpThree = new Recipe("0300", "Tofu 3", "image","source", "url", 0.0,9.9,55.55,10.00,sampleIngredients, nutrientList,
              dietLabelList, healthLabelList);

//      Recipe[] recpFour = FieldParser.parseJSON();
//      System.out.println(recpFour);
      Map<String,String> recipes = new HashMap<String, String>();
      recipes.put(recpOne.getUri(), recpOne.getLabel());
      recipes.put(recpTwo.getUri(), recpTwo.getLabel());
      recipes.put(recpThree.getUri(), recpThree.getLabel());
//      recipes.put(recpFour.getUri(), recpFour.getLabel());

      HashMap<String, String> map = new HashMap<String, String>();
      Set<String> keys = recipes.keySet();

      Map<String, Object> variables = ImmutableMap.of("recipeList", recipes, "title", " " + gui.getRecipeTitle(recipeURI));
      return GSON.toJson(variables);

    }
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class SetupHandler implements TemplateViewRoute {
    private String page;
    private String title;
    
    public SetupHandler(String p, String t) {
      page = p;
      title = t;
    }
    
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          title, "output", "");
      return new ModelAndView(variables, page);
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
      String username = map.value("text1");
      String password = map.value("text2");
      
      String output = "Failed Login: Please try again.";
      try {
        output = Accounts.checkLogin(username, password);
      } catch (AccountException e) {
        e.printStackTrace();
      }
            
      Map<String, Object> variables = ImmutableMap.of("title",
          "Login", "output", output);

      return GSON.toJson(variables);
      
    }
    
  }
  
  /**
   * Handles the functionality of printing out the result of the Stars algorithms.
   *
   */
  private static class SignupHandler implements Route {

    SignupHandler() {
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap map = req.queryMap();
      String user = map.value("user");
      String pass1 = map.value("pass1");
      String pass2 = map.value("pass2");
      String birth = map.value("birth");
      
      String output = "Failed Sign-up: Please try again.";
      if(checkSignUpValidity(user, pass1, pass2)) {
        try {
          new User(user, pass1); // other info too?
          output = "Successful Sign-up!";
        } catch (AccountException e) {
          e.printStackTrace(); // error
        }
      }
      
      Map<String, Object> variables = ImmutableMap.of("title",
          "Login", "output", output);

      return GSON.toJson(variables);
      
    }
    
    private boolean checkSignUpValidity(String user, String pass1, String pass2) {
      // check if user already exists
      // check if passwords are same
      if(!userExists(user) && comparePasswords(pass1, pass2)) {
        return true;
      } else {
        return false;
      }
      // informative error messages ??
    }
    
    private boolean userExists(String user) {
      if (!user.equals(user)) {
        return true;
      } else {
        return false;
      }
    }
    
    private boolean comparePasswords(String pass1, String pass2) {
      if (pass1.equals(pass2)) {
        return true;
      } else {
        return false;
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

  public static String getRecipeTitle(String uri){
    System.out.println("URI inputted to getTitle is " + uri);
    HashMap<String, String> map = new HashMap<String, String>();
    map.put(null, "null name :)");
    map.put("0100", "Tofu");
    map.put("0200", "Other Tofu");
    map.put("0300", "We Only Eat Tofu");
    System.out.println("Name output is " + map.get(uri));

    return map.get(uri);
  }

}