
package edu.brown.cs.student.recommendation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  private static final int TREE_INIT_SIZE = 50;
  private static final int REC_QUANTITY = 10;
  private int dim;
  private User user;


  public Recommender(User user) {
    this.user = user;
    this.dim = user.getNutrients().size();
  }

  /**
   * Function to initialize the KDTree to be used when recommending recipes to users.
   */
  private void initRecipeTree(String input)
      throws InterruptedException, APIException, IOException, SQLException {
    List<Recipe> recipesList = Arrays.asList(FieldParser.getRecipesFromQuery(input));
    // normalize the coordinates of every node
    List<RecipeNode> nodes = convertRecipesToRecipeNodes(recipesList);
    this.recipeTree.normalize(nodes);
//    normalize((nodes));
    // also normalize user history?
    this.recipeTree.initializeTree(nodes);
  }

  /**
   * Function to normalize the coordinates of a list of RecipeNode.
   * @param nodes - all RecipeNodes in the tree.
   */
  private void normalize(List<RecipeNode> nodes) {
    // create lists, maxes, mins for each considered nutrient
    List<List<Double>> nutrientsLists = new ArrayList<>();
    List<Double> maxes = new ArrayList<>();
    List<Double> mins = new ArrayList<>();

    for (int i = 0; i < this.dim; i++) {
      nutrientsLists.add(new ArrayList<>());
      maxes.add(Double.NEGATIVE_INFINITY);
      mins.add(Double.POSITIVE_INFINITY);
    }

    // add the each nodes' nutrients to its list, check for max/min
    for (RecipeNode node : nodes) {
      List<Double> coords = node.getCoords();
      for (int i = 0; i < this.dim; i++) {
        double coord = coords.get(i);
        if (coord > maxes.get(i)) {
          maxes.set(i, coord);
        } // check min-ness
        if (coord < mins.get(i)) {
          mins.set(i, coord);
        }
        // add the nutrient to its list
        nutrientsLists.get(i).add(coord);
      }
    }

    // normalize all nutrients for each type
    int sz = nutrientsLists.size();
    for (int i = 0; i < sz; i++) {
      List<Double> nuts = nutrientsLists.get(i);
      for (int j = 0; j < this.dim; j++) {
        double n = nuts.get(j);
        double normalized = (n - mins.get(j)) / (maxes.get(j) - mins.get(j));
        // replace coords with their new values
        nodes.get(i).getCoords().set(j, normalized);
      }
    }
  }

  /**
   * Comment.
   * @param input Search input of user
   * @return List of recommended recipes
   * //TODO: don't add every recipe to user's history.
   */
  public List<Recipe> makeRecommendation(String input) throws
      RecommendationException, InterruptedException, IOException, APIException, SQLException {
    try {
      this.recipeTree = new KDTree<>(dim);
    } catch (KDTreeException e) {
      throw new RecommendationException(e.getMessage());
    }
    this.initRecipeTree(input);
    List<Recipe> recs;
    List<Recipe> userHistory = this.user.getPreviousRecipes();
    try {
      recs = this.getRecommendedRecipes(userHistory);
    } catch (RecommendationException e) {
      throw new RecommendationException(e.getMessage());
    }

    for (Recipe r : recs) { //maybe only want to save recent history??????????????????????????????
      this.user.addToPreviousRecipes(r); // figure out order and backwards?
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
  private RecipeNode getTargetNode(List<Recipe> userHistory) {
    List<RecipeNode> prevRecipeNodes = this.convertRecipesToRecipeNodes(userHistory);
    List<Double> coordsSum = new ArrayList<>();
    for (int i = 0; i < this.dim; i++) {
      coordsSum.add(0.);
    }

    //find the midpoint of all coordinates of prevRecipeNodes
    for (RecipeNode r: prevRecipeNodes) {
      List<Double> coords = r.getCoords();
      for (int i = 0; i < coordsSum.size(); i++) {
        double currSum = coordsSum.get(i);
        currSum += coords.get(i);
        coordsSum.set(i, currSum);
      }
    }

    List<Double> targetCoords = new ArrayList<>();
    for (double sum : coordsSum) {
      targetCoords.add(sum / this.dim);
    }

    return new RecipeNode(targetCoords);
  }


  private List<Recipe> getRecommendedRecipes(List<Recipe> userHistory)
      throws RecommendationException {
    List<Recipe> recs = new ArrayList<>();

    RecipeNode target = this.getTargetNode(userHistory);

    // search for the nearest/most relevant recipes
    List<RecipeNode> recipeNodes;
    try {
      recipeNodes = recipeTree.nearestSearch(target, REC_QUANTITY);
    } catch (KDTreeException e) {
      throw new RecommendationException(e.getMessage());
    }

    // get the actual recipes from the nodes to compile the recs list
    for (RecipeNode node : recipeNodes) {
      recs.add(node.getRecipe());
    }

    return recs;
  }



}
