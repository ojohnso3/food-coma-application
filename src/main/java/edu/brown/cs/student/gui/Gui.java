package edu.brown.cs.student.gui;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.User;
import edu.brown.cs.student.login.UserCreationException;
import edu.brown.cs.student.recommendation.RecommendationException;
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
 * GUI classes.
 *
 */
public class Gui {
//  private static FieldParser fieldParser;
//  private static NutrientInfo nutrientInfo;
  private static final Gson GSON = new Gson();
  public Map<String, Recipe> recipesMap;
  public Set<String> clickedSet;
  private final Set<String> nutrients;
  private String prevQuery;
  private final ArrayList<String> prevRestrictions;
  public Gui() {
    clickedSet  = new HashSet<>();
    nutrients = new HashSet<>();
    prevRestrictions = new ArrayList<>();
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
   * Starts running webpage.
   * @param port - port to run server on
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
    Spark.get("/recipe/:recipeuri", new SetupHandler("recipe.ftl", "Recipe Detail",
            ""), freeMarker);
    Spark.get("/signup", new SetupHandler("signup.ftl", "Signup", ""), freeMarker);
    Spark.get("/survey", new SetupHandler("survey.ftl", "New User Survey", ""),
            freeMarker);
    Spark.get("/search", new SetupHandler("search.ftl", "Recipe Search",
            new ArrayList<Recipe>()), freeMarker);
    Spark.get("/user", new SetupHandler("user.ftl", "User Profile",
            new ArrayList<Recipe>()), freeMarker);

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
    private final String page;
    private final String title;
    private final Object output;

    SetupHandler(String p, String t, Object o) {
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
  
  private class SearchPostHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      HashMap<String, String> healthInfo = new HashMap<>();
      HashMap<String, String> dietInfo = new HashMap<>();
      String query = qm.value("prefs");
      prevQuery = query;
      String username = qm.value("username");
      String balanced = qm.value("bal");
      healthInfo.put("vegan", qm.value("vg"));
      healthInfo.put("vegetarian", qm.value("veg"));
      healthInfo.put("sugar-conscious", qm.value("sug"));
      healthInfo.put("peanut-free", qm.value("pf"));
      healthInfo.put("tree-nut-free", qm.value("tf"));
      healthInfo.put("alcohol-free", qm.value("af"));

      Set<String> healthKeys = healthInfo.keySet();
      Set<String> dietKeys = dietInfo.keySet();

      for (String dietKey : dietKeys) {
      }

      try {
        User currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        e.printStackTrace();
        System.out.println("AccountException when getting user from searchPostHandler: " + e.getMessage());
      }
      Map<String, String[]> paramsMap = new HashMap<>();
      // Recommender recommender = currUser.getRecommender(); // use object!
      Recipe[] recipes = new Recipe[0];
      Map<String, String[]> simpleRecipeList = new HashMap<String, String[]>();
      try {
        NutrientInfo.createNutrientsList();
        List<String> restrictions = new ArrayList<>();
        prevRestrictions.clear();
        for (String healthKey : healthKeys) {
//          System.out.println("HEALTH KEY IS:" + healthKey + "equals: "
//          + healthInfo.get(healthKey));
          if (healthInfo.get(healthKey).equals("true")) {
            restrictions.add(healthKey);
            prevRestrictions.add(healthKey);
//            System.out.println("ADDED: " + healthKey);
//            paramsMap.put(healthKey, healthInfo.get(healthKey));

          }
        }

        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
        //TODO: Make sure that health labels etc are what the user has requested
//        if(RecipeDatabase.checkQueryInDatabase(query)){
//          List<String> uris = RecipeDatabase.getQueryURIListFromDatabase(query);
//          System.out.println("uris size is " + uris.size());
//          recipes = new Recipe[uris.size()];
//          for(int i = 0; i < uris.size(); i++){
//            recipes[i] = RecipeDatabase.getRecipeFromURI(uris.get(i));
//          }
//          simpleRecipeList = Gui.this.setUpRecipesList(recipes);
//        } else if(!RecipeDatabase.checkQueryInDatabase(query)){
//          System.out.println("MAKING API CALL");
//
//          recipes = FieldParser.getRecipesFromQuery(query, restrictions, paramsMap);
//          String[] recipesForDb = new String[recipes.length];
//          for(int i = 0; i < recipes.length; i++){
//            recipesForDb[i] = recipes[i].getUri();
//          }
//          RecipeDatabase.insertQuery(query, recipesForDb);
//          simpleRecipeList = Gui.this.setUpRecipesList(recipes);
//
//        }
//
//      } catch (IOException e) {
//        System.out.println("IOException getting recipes from query: " + e.getMessage());
//      } catch (InterruptedException e) {
//        System.out.println("InterruptedException getting recipes from query");
//      } catch (APIException e) {
//        System.out.println("API Exception getting recipes from query. Message:  " + e.getMessage());
//      } catch (ClassNotFoundException e) {
//        System.out.println("Database not found when loading during search");
//      } catch (SQLException e){
//        System.out.println("SQLException in getting recipes from database: " + e.getMessage());
//      }
//
//      if(recipes.length==0){
//        List<String> sim = RecipeDatabase.getSimilar(query);
//        if(sim.size() > 0){
//          recipes = new Recipe[sim.size()];
//          System.out.println("SIMILAR HAS BEEN CALLED. SIZE OF SIMILAR IS: " + sim.size());
//          for(int i = 0; i < sim.size(); i ++){
//            try {
//              recipes[i] = RecipeDatabase.getRecipeFromURI(sim.get(i));
//            } catch (SQLException e) {
//              System.out.println("SQLException getting similar recipes from query. Message:"
//              + e.getMessage());
//            } catch (InterruptedException e) {
//              System.out.println("InterruptedException getting similar recipes from query. Message:"
//              + e.getMessage());
//            } catch (APIException e) {
//              System.out.println("API Exception getting similar recipes from query. Message:"
//              + e.getMessage());
//            } catch (IOException e) {
//              System.out.println("IOException getting similar recipes from query. Message:"
//              + e.getMessage());
//            }
//          }
//          simpleRecipeList = Gui.this.setUpRecipesList(recipes);
//        }
//      }
        recipes = FieldParser.getRecipesDBandAPI(query, recipes, restrictions, paramsMap);
        simpleRecipeList = Gui.this.setUpRecipesList(recipes);

      } catch (FileNotFoundException ignored) {

      } catch (SQLException e) {

      } catch (ClassNotFoundException e) {

      }


      Map<String, Object> variables = ImmutableMap.of("recipes", recipes, "simpleRecipeList", simpleRecipeList);
      return GSON.toJson(variables);


    }
  }

  /**
   * This method sets up a list of recipe URIs and maps to their URLs.
   * @param recipes The list of recipes to be added
   * @return A simple list of shortened URLs to recipe URIs
   */
  private HashMap<String, String[]> setUpRecipesList(Recipe[] recipes){
    HashMap<String, String[]> simpleRecipeList = new HashMap<String, String[]>();
    Pattern load = Pattern.compile("#recipe_(.+)");

    //HOW LONG DO WE WANT THE RESULTS?? 10??????? .LENGTH????
    for(int i = 0; i < recipes.length; i++){
      recipesMap = new HashMap<String, Recipe>();
      recipesMap.put(recipes[i].getUri(), recipes[i]);
      String[] fields = new String[2];
      fields[0] = recipes[i].getUrl();
      fields[1] = recipes[i].getUri();
      Matcher matchUri = load.matcher(recipes[i].getUri());

      if(matchUri.find()){
        fields[1] = matchUri.group(1);
      } else {
        fields[1] = "error";
      }
      simpleRecipeList.put(recipes[i].getLabel(), fields);

    }
    return simpleRecipeList;
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
        try {
          currUser.setNutrients(nutrients);
        } catch (SQLException e) {
          e.getMessage();
        }
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
          User newUser = new User(user, pass1);
          System.out.println("NEW USER?: "+ newUser);
          newUser.setRecommender(new Recommender(newUser));
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
      if (currUser == null) {
        Map<String, Object> noUserVars = ImmutableMap.of("title", "User", "output", new HashMap<String, String>());
        return GSON.toJson(noUserVars);
      }
      List<Recipe> prevRecipes = currUser.getPreviousRecipes();

      System.out.println("SIZE " + prevRecipes.size());

      Map<String,String> output = new HashMap<String, String>();

      for (Recipe r : prevRecipes) {
        output.put(r.getCompactUri(), r.getLabel());
      }
      
      System.out.println("MAP SIZE " + output.size());
//      output.put("URI1", "NAME1");
//      output.put("URI2", "NAME2");
//      output.put("URI3", "NAME3");

      Map<String, Object> variables = ImmutableMap.of("title", "User", "output", output);
  
      return GSON.toJson(variables);
    }
  }
  
  private class RecipeHandler implements Route {
    private Gui gui;
    RecipeHandler(Gui g) {
      gui = g;
    }
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String url = qm.value("url");
      String username = qm.value("username");

      User currUser = null;
      try {
        currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException getting User in RecipeHandler: " + e.getMessage());
      }
      assert currUser != null;
      Recommender recommender = currUser.getRecommender();
//
      // TODO: use Recommender object below!
      
      Pattern load = Pattern.compile("localhost:.+\\/recipe\\/(.+)");
      String recipeURI = null;
      if (url != null) {
        Matcher matchURL = load.matcher(url);
        if (matchURL.find()) {
          recipeURI = matchURL.group(1);
          String fullUri = "http://www.edamam.com/ontologies/edamam.owl#recipe_" + recipeURI;
          clickedSet.add(fullUri);
          try {
            currUser.addToPreviousRecipesByURI(fullUri);
            System.out.println("ADDED PREV");
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
      if (currRecipe == null) {
        try {
          currRecipe = RecipeDatabase.getRecipeFromURI("http://www.edamam.com/ontologies/edamam.owl#recipe_" + recipeURI);
        } catch (IOException e) {
          System.out.println("IOException in getting recipe from API");
        } catch (InterruptedException e) {
          System.out.println("InterruptedException in getting recipe from API");
        } catch (APIException | SQLException e) {
          System.out.println("APIException in getting recipe from API");
          System.out.println("Error message: " + e.getMessage());
        }
      }
      if (currRecipe == null) {
        System.out.println("EXCEEDED RECIPES LOOKUP!!");
      }

      String actualName = currRecipe.getLabel();
      List<String> ingredientsList = new ArrayList<String>();
      for (int i = 0; i < currRecipe.getIngredients().size(); i++) {
        ingredientsList.add(currRecipe.getIngredients().get(i).getText());
      }
//      HashMap<String, String> map = new HashMap<String, String>();
//      Set<String> keys = recipes.keySet();
      List<Recipe> recommendations = new ArrayList<Recipe>();
      try {
        System.out.println("Prev Query: " + prevQuery);
        System.out.println("Prev REstrict: " + prevRestrictions);
        System.out.println("Recommender: " + recommender);
        System.out.println("User: " + currUser);
        recommendations = recommender.makeRecommendation(prevQuery, new HashMap<>(),
                prevRestrictions);
      } catch (RecommendationException | InterruptedException | IOException | APIException
              | SQLException e) {
        e.printStackTrace();
      }

      Map<String, String[]> recipePageRecipes = new HashMap<String, String[]>();
      Set<String> keys = gui.recipesMap.keySet();

      for (Recipe recp : recommendations) {
//        if(key == currRecipe.getCompactUri()){
//          continue;
//        }
//        String[] fields = new String[gui.recipesMap.get(key).getIngredients().size() + 1];
//        Recipe currRec = gui.recipesMap.get(key);
//        fields[0] = currRec.getLabel();
        String[] fields = new String[1];
        fields[0] = recp.getLabel();
        recipePageRecipes.put(recp.getCompactUri(), fields);
      }
      Map<String, String> nutrientsMap = new HashMap<String, String>();

      Map<String, double[]> map = currRecipe.getNutrientsMap();
      for (String string : map.keySet()) {
//        System.out.println("KEY:::  " + string);
      }
     Map<String, double[]> nuts = currRecipe.getNutrientsMap();
      String[] nutValues = new String[nuts.keySet().size()];
      int i = 0;
//      int j = 1;
      for (String itm : nuts.keySet()) {
        nutValues[i] = itm;
//        nutValues[j] = nuts.get(itm)[1];
        i++;
//        i+=2;
//        j+=2;
      }
     ImmutableMap<String,Object> variables = ImmutableMap.<String, Object>builder()
              .put("recipeList", recipePageRecipes)
              .put("title", " " + actualName)
              .put("ingredients", ingredientsList)
              .put("image", currRecipe.getImage())
              .put("URL", currRecipe.getUrl())
              .put("Nutrients", nutValues)
              .build();
      return GSON.toJson(variables);
    }
  }

  private class NutrientHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
      QueryParamsMap qm = request.queryMap();
      String nutrient = qm.value("nut");
      if (nutrients.contains(nutrient)) {
        nutrients.remove(nutrient);
      } else {
        nutrients.add(nutrient);
      }
//      System.out.println(nutrient);
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

}
