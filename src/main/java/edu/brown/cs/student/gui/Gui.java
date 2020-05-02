package edu.brown.cs.student.gui;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.Ingredient;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.User;
import edu.brown.cs.student.login.UserCreationException;
import edu.brown.cs.student.recommendation.Recommender;
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
  public Map<String, Recipe> recipesMap;
  public Set<String> clickedSet;
  private Set<String> nutrients;
  public Gui() {
    recipesMap = new HashMap<String, Recipe>();
    clickedSet  = new HashSet<String>();
    nutrients = new HashSet<String>();
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
    Spark.get("/home", new SetupHandler("home.ftl", "foodCOMA Home", ""), freeMarker);
    Spark.get("/about", new SetupHandler("about.ftl", "About", ""), freeMarker);
    Spark.get("/login", new SetupHandler("login.ftl", "Login", ""), freeMarker);
    Spark.get("/recipe/:recipeuri", new SetupHandler("recipe.ftl", "Recipe Detail", ""), freeMarker);
    Spark.get("/signup", new SetupHandler("signup.ftl", "Signup", ""), freeMarker);
    Spark.get("/survey", new SetupHandler("survey.ftl", "New User Survey", ""), freeMarker);
    Spark.get("/search", new SetupHandler("search.ftl", "Recipe Search", new ArrayList<Recipe>()), freeMarker);
    Spark.get("/user", new SetupHandler("user.ftl", "User Profile", new ArrayList<Recipe>()), freeMarker);

    Spark.post("/search", new SearchPostHandler());
    Spark.post("/logged", new LoginHandler());
    Spark.post("/signed", new SignupHandler());
    Spark.post("/saved", new SavedHandler());
    Spark.post("/survey_post", new SurveyHandler());
    Spark.post("/recipe/recipeuri", new RecipeHandler(this));
    Spark.post("/toggleNutrient", new NutrientHandler());



    // OLD Routes
//    Spark.get("/results", new SubmitHandler(), freeMarker);
//    Spark.get("/recipe/:recipeuri", new RecipeHandler());

  }

  // handlers for gui that interact w/html and javascript

  /**
   * Handle GET requests.
   *
   */
  private static class SetupHandler implements TemplateViewRoute {
    private String page;
    private String title;
    private Object output;
    
    public SetupHandler(String p, String t, Object o) {
      page = p;
      title = t;
      output = o;
    }
    
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          title, "output", output);
      return new ModelAndView(variables, page);
    }
  }
  
  
  private class SearchPostHandler implements Route{
    @Override
    public String handle(Request req, Response res){
      QueryParamsMap qm = req.queryMap();
      HashMap<String, String> healthInfo = new HashMap<String, String>();
      HashMap<String, String> dietInfo = new HashMap<String, String>();
      String query = qm.value("prefs");
      String username = qm.value("username");
      String balanced = qm.value("bal");
      healthInfo.put("vegan",qm.value("vg"));
      healthInfo.put("vegetarian",qm.value("veg"));
      healthInfo.put("sugar-conscious",qm.value("sug"));
      healthInfo.put("peanut-free",qm.value("pf"));
      healthInfo.put("tree-nut-free",qm.value("tf"));
      healthInfo.put("alcohol-free",qm.value("af"));

      Set<String> healthKeys = healthInfo.keySet();
      Set<String> dietKeys = dietInfo.keySet();


      for(String dietKey : dietKeys){

      }

      try {
        User currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException when getting user from searchPostHandler: " + e.getMessage());
      }
      Map<String, String[]> paramsMap = new HashMap<>();
      // Recommender recommender = currUser.getRecommender(); // use object!
      Recipe[] recipes = new Recipe[0];
      Map<String, String[]> simpleRecipeList = new HashMap<String, String[]>();
      try {
        NutrientInfo.createNutrientsList();
        List<String> restrictions = new ArrayList<>();
        for(String healthKey : healthKeys){
          System.out.println("HEALTH KEY IS:" + healthKey + "equals: " + healthInfo.get(healthKey));
          if(healthInfo.get(healthKey).equals("true")){
            restrictions.add(healthKey);
            System.out.println("ADDED: " + healthKey);
//            paramsMap.put(healthKey, healthInfo.get(healthKey));

          }
        }

        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
        recipes = FieldParser.getRecipesFromQuery(query, restrictions, paramsMap);
        String[] recipesForDb = new String[recipes.length];
        for(int i = 0; i < recipes.length; i++){
          recipesForDb[i] = recipes[i].getUri();
        }
        RecipeDatabase.insertQuery(query, recipesForDb);
        simpleRecipeList = new HashMap<String, String[]>();
        Pattern load = Pattern.compile("#recipe_(.+)");
        recipesMap.clear();
        for(int i = 0; i < recipes.length; i++){
          recipesMap.put(recipes[i].getUri(), recipes[i]);
          System.out.println(recipes[i].getLabel());
          String[] fields = new String[2];
          fields[0] = recipes[i].getUrl();
          fields[1] = recipes[i].getUri();
          System.out.println("Full URI: " + fields[1]);
          Matcher matchUri = load.matcher(recipes[i].getUri());
          if(matchUri.find()){
            fields[1] = matchUri.group(1);
            System.out.println("URI Found: " + matchUri.group(1));
          } else {
            fields[1] = "error";
          }
          simpleRecipeList.put(recipes[i].getLabel(), fields);
        }
      } catch (IOException e) {
        System.out.println("IOException getting recipes from query: " + e.getMessage());
      } catch (InterruptedException e) {
        System.out.println("InterruptedException getting recipes from query");
      } catch (APIException | SQLException e) {
        System.out.println("API Exception getting recipes from query. Message:  " + e.getMessage());
      } catch (ClassNotFoundException e) {
        System.out.println("Database not found when loading during search");
      }

      Map<String, Object> variables = ImmutableMap.of("recipes",recipes, "simpleRecipeList", simpleRecipeList);
      return GSON.toJson(variables);
    }

  }
  
  /**
   * Handles the functionality of printing out the result of the Stars algorithms.
   *
   */
  private static class SurveyHandler implements Route {
    private Map<String, String> nutrientsMap;
    
    SurveyHandler() {
      NutrientInfo.createNutrientsList();
      nutrientsMap = NutrientInfo.getNutrientsMap();
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap map = req.queryMap();
      String username = map.value("username");
      String feedback = map.value("feedback");
      
      System.out.println("USER " + username);
      System.out.println("FEEDBACK " + feedback);
      User currUser = null;
      try {
        currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException error: " + e.getMessage());
      }

      List<String> nutrients = new ArrayList<String>();
      
      for (String nutrient: nutrientsMap.keySet()) {
        String currNu = map.value(nutrient);
        if (currNu.equals("true")) {
          nutrients.add(nutrientsMap.get(nutrient));
        }
      }
      
      String output = "Invalid Survey: Please try again.";
      
      if (nutrients.size() > 2) {
        System.out.println(nutrients);
        System.out.println(currUser);
        currUser.setNutrients(nutrients);
        output = "Valid Survey!";
      } else {
        output = "Invalid Survey: Please select at least three nutrients.";
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
  private static class LoginHandler implements Route {

    LoginHandler() {
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap map = req.queryMap();
      String username = map.value("text1");
      String password = map.value("text2");

      boolean valid = false;
      try {
        valid = Accounts.checkLogin(username, password);
      } catch (AccountException e) {
        e.printStackTrace(); // TODO: error message
      }
      
      String output = "Failed Login: Please try again.";
      if (valid) {
        output = "Valid Login!";
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
//      String birth = map.value("birth");
      
      String output = "Failed Sign-up: Please try again.";
      try {
        System.out.println("CHECK " + Accounts.checkSignUpValidity(user, pass1, pass2));
        if(Accounts.checkSignUpValidity(user, pass1, pass2)) {
          new User(user, pass1);
          output = "Successful Sign-up!";
        }
      } catch (UserCreationException e1) {
        System.out.println(e1.getMessage());
        output = e1.getMessage();
      } catch (AccountException e) {
        System.out.println(e.getMessage());
      }
      
      Map<String, Object> variables = ImmutableMap.of("title",
          "Login", "output", output);

      return GSON.toJson(variables);

    }
  }
  
  /**
   * Handles the functionality of getting saved Recipes.
   *
   */
  private static class SavedHandler implements Route {

    SavedHandler() {
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap map = req.queryMap();
      String username = map.value("user");

      System.out.println(username);
      User currUser = null;
      try {
        currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException error: " + e.getMessage());
      }
      if(currUser == null){
        Map<String, Object> noUserVars = ImmutableMap.of("title", "User", "output", new HashMap<String, String>());
        return GSON.toJson(noUserVars);
      }
      List<Recipe> prevRecipes = currUser.getPreviousRecipes();

      System.out.println("SIZE " + prevRecipes.size());

      Map<String,String> output = new HashMap<String, String>();

      for (Recipe r : prevRecipes) {
        output.put(r.getUri(), r.getLabel());
      }
      
      System.out.println("MAP SIZE " + output.size());
//      output.put("URI1", "NAME1");
//      output.put("URI2", "NAME2");
//      output.put("URI3", "NAME3");

      Map<String, Object> variables = ImmutableMap.of("title", "User", "output", output);
  
      return GSON.toJson(variables);

    }
    
  }
  

  
  private class RecipeHandler implements Route{
    private Gui gui;
    
    RecipeHandler(Gui g){
      gui = g;
    }
    @Override
    public String handle(Request req, Response res){
      QueryParamsMap qm = req.queryMap();
      String url = qm.value("url");
      String username = qm.value("username");

      User currUser = null;
      try {
        currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException getting User in RecipeHandler: " + e.getMessage());
      }
      Recommender recommender = currUser.getRecommender();
      
      // TODO: use Recommender object below!
      
      Pattern load = Pattern.compile("localhost:.+\\/recipe\\/(.+)");
      String recipeURI = null;
      if(url != null){
        Matcher matchURL = load.matcher(url);
        if(matchURL.find()){
          recipeURI = matchURL.group(1);
          String fullUri = "http://www.edamam.com/ontologies/edamam.owl#recipe_" + recipeURI;
          clickedSet.add(fullUri);
          try {
            currUser.addToPreviousRecipesByURI(fullUri);
          } catch (InterruptedException e) {
            System.out.println("InterruptedException when adding recipe to previous recipes: " + e.getMessage());
          } catch (SQLException e) {
            System.out.println("SQLException when adding recipe to previous recipes: " + e.getMessage());
          } catch (APIException e) {
            System.out.println("APIException when adding recipe to previous recipes: " + e.getMessage());
          } catch (IOException e) {
            System.out.println("IOException when adding recipe to previous recipes: " + e.getMessage());
          }
          System.out.println("The Recipe URI is " + recipeURI);
        }
      }
      Recipe currRecipe = null;
      try {
        System.out.println("ABOUT TO ENTER " + recipeURI);
        RecipeDatabase.loadDatabase("/data/recipeDatabase.sqlite3");
        currRecipe = RecipeDatabase.getRecipeFromURI(recipeURI);
      } catch (SQLException | InterruptedException | APIException | IOException | ClassNotFoundException e) {
        System.out.println("SQLException getting recipe from database");
      }
      if(currRecipe == null){
        try {
          currRecipe = FieldParser.getRecipeFromURI("http://www.edamam.com/ontologies/edamam.owl#recipe_" + recipeURI);

        } catch (IOException e) {
          System.out.println("IOException in getting recipe from API");
        } catch (InterruptedException e) {
          System.out.println("InterruptedException in getting recipe from API");
        } catch (APIException e) {
          System.out.println("APIException in getting recipe from API");
          System.out.println("Error message: " + e.getMessage());
        }
      }
      String actualName = currRecipe.getLabel();
      System.out.println("ACTUAL NAME: " + actualName);
      List<String> ingredientsList = new ArrayList<String>();
      for(int i = 0; i < currRecipe.getIngredients().size(); i ++){
        ingredientsList.add(currRecipe.getIngredients().get(i).getText());
      }
//      HashMap<String, String> map = new HashMap<String, String>();
//      Set<String> keys = recipes.keySet();

      Map<String, String[]> recipePageRecipes = new HashMap<String, String[]>();
      Set<String> keys = gui.recipesMap.keySet();
      for(String key : keys){
        if(key == currRecipe.getCompactUri()){
          continue;
        }
        String[] fields = new String[gui.recipesMap.get(key).getIngredients().size() + 1];
        Recipe currRec = gui.recipesMap.get(key);
        fields[0] = currRec.getLabel();
        recipePageRecipes.put(currRec.getCompactUri(), fields);
      }
      System.out.println("Recipe URL is:" + currRecipe.getUrl());
      Map<String, Object> variables = ImmutableMap.of("recipeList", recipePageRecipes, "title", " " + actualName, 
          "ingredients", ingredientsList, "image", currRecipe.getImage(), "URL", currRecipe.getUrl());
      return GSON.toJson(variables);
    }
  }

  private class NutrientHandler implements Route{

    @Override
    public Object handle(Request request, Response response) throws Exception {
      QueryParamsMap qm = request.queryMap();
      String nutrient = qm.value("nut");
      if(nutrients.contains(nutrient)){
        nutrients.remove(nutrient);
      } else {
        nutrients.add(nutrient);
      }
      System.out.println(nutrient);
      return null;
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