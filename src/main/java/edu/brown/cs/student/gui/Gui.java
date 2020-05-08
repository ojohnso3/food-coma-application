package edu.brown.cs.student.gui;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
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
import edu.brown.cs.student.food.NutrientInfo;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.AccountException;
import edu.brown.cs.student.login.Accounts;
import edu.brown.cs.student.login.User;
import edu.brown.cs.student.login.UserCreationException;
import edu.brown.cs.student.recommendation.RecipeNode;
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
  private static final Gson GSON = new Gson();
  private Map<String, Recipe> recipesMap;
  private Set<String> clickedSet;
  private final Set<String> nutrients;
  private String prevQuery;
  private final Set<String> prevRestrictions;
  public Gui() {
    clickedSet  = new HashSet<>();
    nutrients = new HashSet<>();
    prevRestrictions = new HashSet<>();
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
    Spark.get("/score", new SetupHandler("score.ftl", "Your Score", new ArrayList<Double>()), freeMarker);

    Spark.post("/search", new SearchPostHandler());
    Spark.post("/logged", new LoginHandler());
    Spark.post("/signed", new SignupHandler());
    Spark.post("/saved", new SavedHandler());
    Spark.post("/survey_post", new SurveyHandler());
    Spark.post("/recipe/recipeuri", new RecipeHandler(this));
    Spark.post("/toggleNutrient", new NutrientHandler());
    Spark.post("/score", new ScoreHandler());

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

  /*
   * This class handles post requests from the Search page
   */
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
        Set<String> restrictions = new HashSet<>();
        prevRestrictions.clear();
        for (String healthKey : healthKeys) {
          if (healthInfo.get(healthKey).equals("true")) {
            restrictions.add(healthKey);
            prevRestrictions.add(healthKey);
          }
        }

        RecipeDatabase.loadDatabase("data/recipeDatabase.sqlite3");
        recipes = FieldParser.getRecipesDBandAPI(query, restrictions, paramsMap);
        simpleRecipeList = Gui.this.setUpRecipesList(recipes);

      } catch (FileNotFoundException e) {
        System.out.println("File Not Found Exception: " + e.getMessage());
      } catch( ClassNotFoundException e){
        System.out.println("Class Not Found Exception: " + e.getMessage());
      }catch( SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
      }
      Map<String, Object> variables = ImmutableMap.of("recipes", recipes, "simpleRecipeList",
              simpleRecipeList);
      return GSON.toJson(variables);
    }
  }

  /**
   * This method sets up a list of recipe URIs and maps to their URLs.
   * @param recipes The list of recipes to be added
   * @return A simple list of shortened URLs to recipe URIs
   */
  private HashMap<String, String[]> setUpRecipesList(Recipe[] recipes) {
    HashMap<String, String[]> simpleRecipeList = new HashMap<>();
    Pattern load = Pattern.compile("#recipe_(.+)");
    for (Recipe recipe : recipes) {
      recipesMap = new HashMap<>();
      recipesMap.put(recipe.getUri(), recipe);
      String[] fields = new String[2];
      fields[0] = recipe.getUrl();
      fields[1] = recipe.getUri();
      Matcher matchUri = load.matcher(recipe.getUri());
      if (matchUri.find()) {
        fields[1] = matchUri.group(1);
      } else {
        fields[1] = "error";
      }
      simpleRecipeList.put(recipe.getLabel(), fields);
    }
    return simpleRecipeList;
  }

  /**
   * Handles the functionality of printing out the results.
   */
  private static class SurveyHandler implements Route {
    private final Map<String, String> nutrientsMap;
    
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

      List<String> nutrients = new ArrayList<>();
      
      for (String nutrient: nutrientsMap.keySet()) {
        String currNu = map.value(nutrient);
        System.out.println("1: " + nutrient + "2: " + currNu);
        if (currNu.equals("true")) {
          nutrients.add(nutrientsMap.get(nutrient));
        }
      }
      
      String output;
      
      System.out.println("SIZE OF NUT " + nutrients.size());
      
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
        if (Accounts.checkSignUpValidity(user, pass1, pass2)) {
          User newUser = new User(user, pass1);
          System.out.println("NEW USER?: " + newUser);
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
        Map<String, Object> noUserVars = ImmutableMap.of("title", "User", "output",
                new HashMap<String, String>());
        return GSON.toJson(noUserVars);
      }
      List<Recipe> prevRecipes = currUser.getPreviousRecipes();

      System.out.println("SIZE " + prevRecipes.size());

      Map<String, String> output = new HashMap<>();

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
    private final Gui gui;
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
      Pattern load = Pattern.compile("localhost:.+/recipe/(.+)");
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
            System.out.println("InterruptedException when adding recipe to previous recipes: "
                    + e.getMessage());
          } catch (SQLException e) {
            System.out.println("SQLException when adding recipe to previous recipes: "
                    + e.getMessage());
          } catch (APIException e) {
            System.out.println("APIException when adding recipe to previous recipes: "
                    + e.getMessage());
          } catch (IOException e) {
            System.out.println("IOException when adding recipe to previous recipes: "
                    + e.getMessage());
          }
          System.out.println("The Recipe URI is " + recipeURI);
        }
      }
      Recipe currRecipe = null;
      try {
        System.out.println("ABOUT TO ENTER " + recipeURI);
        RecipeDatabase.loadDatabase("/data/recipeDatabase.sqlite3");
        currRecipe = RecipeDatabase.getRecipeFromURI(recipeURI);
      } catch (SQLException e){
        System.out.println("SQLException getting recipe from database: " + e.getMessage());
      } catch(InterruptedException e){
        System.out.println("InterruptedException getting recipe from database: " + e.getMessage());
      } catch(APIException e){
        System.out.println("API Exception getting recipe from database: " + e.getMessage());
      } catch(IOException e){
        System.out.println("IOException getting recipe from database: " + e.getMessage());
      } catch(ClassNotFoundException e) {
        System.out.println("ClassNotFoundException getting recipe from database: " + e.getMessage());
      }
      if (currRecipe == null) {
        try {
          System.out.println("RECIPE URI LOOKUP" + recipeURI);
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
        System.out.println("Exceeded Recipes Lookup");
      }

      assert currRecipe != null;
      String actualName = currRecipe.getLabel();
      List<String> ingredientsList = new ArrayList<>();
      for (int i = 0; i < currRecipe.getIngredients().size(); i++) {
        ingredientsList.add(currRecipe.getIngredients().get(i).getText());
      }
      List<Recipe> recommendations = new ArrayList<>();
      List<Double> foodComaWeights = new ArrayList<>();
      try {
        System.out.println("Prev Query: " + prevQuery);
        System.out.println("Prev REstrict: " + prevRestrictions);
        System.out.println("Recommender: " + recommender);
        System.out.println("User: " + currUser);
        recommendations = recommender.makeRecommendation(prevQuery, new HashMap<>(),
                prevRestrictions);
      } catch (RecommendationException e) {
        System.out.println("RecommendationException in getting recommendations on recipe page: " + e.getMessage());
      }

      Map<String, String[]> recipePageRecipes = new HashMap<>();
      Collection[] collections = new Collection[recommendations.size()];
      Set<String> keys = gui.recipesMap.keySet();
      int k = 0;
      List<Double> foodComaScores = recommender.getFoodComaScores();
      for (Recipe recp : recommendations) {
        String[] fields = new String[2];
        fields[0] = recp.getLabel();
        System.out.println("Weight inputted: " + foodComaScores.get(k).toString());
        fields[1] = foodComaScores.get(k).toString();
        Collection coll = new ArrayList();
        coll.add(recp.getCompactUri());
        coll.add(fields[0]);
        coll.add(fields[1]);
        collections[k] = coll;
        recipePageRecipes.put(recp.getCompactUri(), fields);
        k++;
      }
      Map<String, String> nutrientsMap = new HashMap<>();

      Map<String, double[]> map = currRecipe.getNutrientsMap();
//      for (String string : map.keySet()) {
//        System.out.println("KEY:::  " + string);
//      }

      Map<String, double[]> nuts = currRecipe.getNutrientsMap();
      Map<String, String[]> nutrientConversion = NutrientInfo.getNutrients();
//      String[] nutValues = new String[nuts.keySet().size()*2];
//      int i = 0;
//      int j = 1;
//      for (String itm : nuts.keySet()) {
//        // nutValues[i] = itm;
//        nutValues[i] = nutrientConversion.get(itm)[0];
//        nutValues[j] = Double.toString(nuts.get(itm)[1]);
//        System.out.println("NUT i " + nutValues[i]);
//        System.out.println("NUT j " + nutValues[j]);
//        i+=2;
//        j+=2;
//      }
      String[] nutValues = new String[nuts.keySet().size()*3];
      int i = 0;
      int j = 1;
      int l = 2;
      for (String itm : nuts.keySet()) {
        nutValues[i] = nutrientConversion.get(itm)[0];
        nutValues[j] = Double.toString(nuts.get(itm)[1]);
        nutValues[l] = nutrientConversion.get(itm)[1];
        System.out.println("NUT i " + nutValues[i]);
        System.out.println("NUT j " + nutValues[j]);
        System.out.println("NUT l " + nutValues[l]);
        i+=3;
        j+=3;
        l+=3;
      }
      List<Map.Entry<String, String[]>> recipeListSet = new LinkedList<>(recipePageRecipes.entrySet());
      Collections.sort(recipeListSet, new CompRecipes());
      Map<String, String[]> newMap = new HashMap<>();
      for(Map.Entry<String, String[]> itm : recipeListSet){
        newMap.put(itm.getKey(), itm.getValue());
      }
      ImmutableMap<String, Object> variables = ImmutableMap.<String, Object>builder()
              .put("recipeList", recipePageRecipes)
              .put("title", " " + actualName)
              .put("ingredients", ingredientsList)
              .put("image", currRecipe.getImage())
              .put("URL", currRecipe.getUrl())
              .put("Nutrients", nutValues)
              .put("sortedArray", collections)
              .build();
      return GSON.toJson(variables);
    }
  }
  private class CompRecipes implements Comparator<Map.Entry<String, String[]>>{
    @Override
    public int compare(Map.Entry<String, String[]> t1, Map.Entry<String, String[]> t2) {
      return Double.compare(Double.parseDouble(t1.getValue()[1]), Double.parseDouble(t2.getValue()[1]));
    }
  }


  private Collection[] recpPageToArray(Map<String, String[]> recipes){
    Collection[] coll = new Collection[recipes.keySet().size()];
    int i = 0;
    for(String uri : recipes.keySet()){
      Collection collection = new ArrayList();
      collection.add(uri);
      collection.add(recipes.get(uri)[0]);
      collection.add(recipes.get(uri)[1]);
      coll[i] = collection;
      i++;
    }
    return coll;
  }

  private class NutrientHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String nutrient = qm.value("nut");
      if (nutrients.contains(nutrient)) {
        nutrients.remove(nutrient);
      } else {
        nutrients.add(nutrient);
      }
      return null;
    }
  }
  
  private class ScoreHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
      QueryParamsMap map = request.queryMap();
      String username = map.value("user");

      User currUser = null;
      try {
        currUser = Accounts.getUser(username);
      } catch (AccountException e) {
        System.out.println("AccountException error: " + e.getMessage());
      }
      if (currUser == null) {
        Map<String, Object> noUserVars = ImmutableMap.of("title", "User", "output",
                new HashMap<String, String>());
        return GSON.toJson(noUserVars);
      }
      
      RecipeNode target = currUser.getRecommender().getTargetNode();
      List<Double> weights = target.getCoords();
      List<String> nutrients = NutrientInfo.getMainNutrients();
      nutrients.addAll(NutrientInfo.getSecondaryNutrients());
      
      assert(nutrients.size() == weights.size());
      
      
      List<String> weightsAsStrings = new ArrayList<String>();

      for (int i = 0; i < nutrients.size(); i++) {
        weightsAsStrings.add(nutrients.get(i));
        weightsAsStrings.add(Double.toString(weights.get(i)));
      }
      
      assert(weightsAsStrings.size() == weights.size() * 2);

      Map<String, Object> variables = ImmutableMap.of("title", "User", "output", weightsAsStrings);
  
      return GSON.toJson(variables);
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
