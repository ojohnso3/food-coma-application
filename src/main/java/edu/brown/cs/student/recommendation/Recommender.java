
package edu.brown.cs.student.recommendation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.kdtree.KDTreeException;
import edu.brown.cs.student.login.User;

/**
 * This class contains the code to compile recipe recommendations based on user input and history.
 */
public class Recommender {
  private KDTree<RecipeNode> recipeTree;
  private static final int REC_QUANTITY = 10;
  private final int dim;
  private final User user;

  /**
   * constructor; should be called on initial survey or on user recreation.
   * @param user - user
   */
  public Recommender(User user) {
    this.user = user;
    this.dim = user.getNutrients().size();
  }

  /**
   * Function to initialize the KDTree to be used when recommending recipes to users.
   * @param input - the input given by the user to query.
   * @param paramsMap - the parameters given by the user to the query.
   */
  private void initRecipeTree(String input, Map<String, String[]> paramsMap)
      throws InterruptedException, APIException, IOException, SQLException {
    List<Recipe> recipesList = Arrays.asList(FieldParser.getRecipesFromQuery(input,
        this.user.getDietaryRestrictions(), paramsMap));
    // normalize the coordinates of every node
    List<RecipeNode> nodes = convertRecipesToRecipeNodes(recipesList);
    this.recipeTree.normalizeAxes(nodes); //weight special axes higher
    // put them in the tree
    this.recipeTree.initializeTree(nodes);
  }

  /**
   * Comment.
   * @param input Search input of user
   * @param paramsMap - parameters for the query the user has entered.
   * @return List of recommended recipes
   * //TODO: don't add every recipe to user's history.
   */
  public List<Recipe> makeRecommendation(String input, Map<String, String[]> paramsMap) throws
      RecommendationException, InterruptedException, IOException, APIException, SQLException {
    try {
      this.recipeTree = new KDTree<>(dim);
    } catch (KDTreeException e) {
      throw new RecommendationException(e.getMessage());
    }
    this.initRecipeTree(input, paramsMap);
    List<Recipe> recs;
    List<Recipe> userHistory = this.user.getPreviousRecipes();
    recs = this.getRecommendedRecipes(userHistory);
    // TODO: correct place/way to handle this?
    if (recs.isEmpty()) {
      throw new RecommendationException("no recipes found for " + input);
    }
    return recs;
  }

  /**
   * Function to find the coordinates of a RecipeNode based on the user's nutritional preferences.
   * @param r - the RecipeNode to find the coordinates of.
   */
  private void addRecipeNodeCoords(RecipeNode r) {
    List<String> nutrientPreferences = user.getNutrients();
    List<Double> coords = new ArrayList<>();
    for (String code : nutrientPreferences) {
      coords.add(r.getRecipe().getNutrientVals(code)[0]);
    }

    r.setCoords(coords);
  }

  /**
   * Function to find convert Recipes into RecipeNodes.
   * @param recipes - a list of Recipes to convert to nodes.
   * @return - a list of the converted RecipeNodes.
   */
  private List<RecipeNode> convertRecipesToRecipeNodes(List<Recipe> recipes) {
    List<RecipeNode> nodes = new ArrayList<>();
    for (Recipe recipe: recipes) {
      RecipeNode r = new RecipeNode(recipe);
      this.addRecipeNodeCoords(r);
      nodes.add(r);
    }
    return nodes;
  }

  /**
   * Function to get the coordinates of the starting RecipeNode for nearest neighbor search.
   * @param userHistory - the previous recipes that the user has accessed.
   * @return - a RecipeNode at the coordinates determined by the query and the user's history.
   */
  private RecipeNode getTargetNode(List<Recipe> userHistory) throws RecommendationException {
    // get nodes for prev recipes and normalize
    List<RecipeNode> prevRecipeNodes = this.convertRecipesToRecipeNodes(userHistory);
    this.recipeTree.normalizeAxes(prevRecipeNodes);
    // prepare target
    List<Double> coords = new ArrayList<>();
    for (int i = 0; i < this.dim; i++) {
      coords.add(Double.NaN);
    }
    RecipeNode target = new RecipeNode(coords);
    // set the coords to be the midpoint
    try {
      recipeTree.makeAverageNode(target, prevRecipeNodes);
    } catch (KDTreeException e) {
      throw new RecommendationException(e.getMessage());
    }
    return target;
  }

  private List<Recipe> getRecommendedRecipes(List<Recipe> userHistory)
      throws RecommendationException {
    List<Recipe> recs = new ArrayList<>();
    // create a target node using the average position of all previous recipes
    RecipeNode target = this.getTargetNode(userHistory);
    try {
      // add the target nodes' coordinates each node in tree to make the origin the target point
      this.recipeTree.translateTree(target.getCoords());

      // search for the nearest/most relevant recipes
      List<RecipeNode> recipeNodes = recipeTree.nearestSearch(target, REC_QUANTITY);

      // get the actual recipes from the nodes to compile the recs list
      for (RecipeNode node : recipeNodes) {
        recs.add(node.getRecipe());
      }
    } catch (KDTreeException e) {
      throw new RecommendationException(e.getMessage());
    }

    return recs;
  }



}
