
package edu.brown.cs.student.recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.database.FieldParser;
import edu.brown.cs.student.database.RecipeDatabase;
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
  private static final int DIM = 6;


  public Recommender() {

  }

  /**
   * Function to initialize the KDTree to be used when recommending recipes to users.
   */
  private void initRecipeTree(String input) throws InterruptedException, APIException, IOException {
    List<Recipe> recipesList = Arrays.asList(FieldParser.getRecipesFromQuery(input));
    List<RecipeNode> nodesList = this.convertRecipesToRecipeNodes(recipesList);
    this.recipeTree.initializeTree(nodesList);
  }

  /**
   * Comment.
   * @param user Id of current user
   * @param input Search input of user
   * @return List of recommended recipes
   */
  public List<Recipe> makeRecommendation(User user, String input) throws
      RecommendationException, InterruptedException, IOException, APIException {
    this.recipeTree = new KDTree<>(DIM);
    this.initRecipeTree(input);
    List<Recipe> recs;
    List<Recipe> userHistory = user.getPreviousRecipes();
    try {
      recs = this.getRecommendedRecipes(userHistory);
    } catch (RecommendationException e) {
      throw new RecommendationException(e.getMessage());
    }

    for (Recipe r : recs) { //maybe only want to save recent history?????????????????????????????????
      user.addToPreviousRecipes(r); // figure out order and backwards?
    }
    return recs;
  }

  /**
   * Function to find the coordinates of a RecipeNode based on the user's nutritional preferences.
   * @param r - the RecipeNode to find the coordinates of.
   * @return
   */
  private List<Double> getRecipeNodeCoords(RecipeNode r) {
    return null;
  }

  /**
   * Function to find convert Recipes into RecipeNodes.
   * @param recipes - a list of Recipes to convert to nodes.
   * @return - a list of the converted RecipeNodes.
   */
  private List<RecipeNode> convertRecipesToRecipeNodes(List<Recipe> recipes) {
    List<RecipeNode> nodes = new ArrayList<RecipeNode>();
    for (Recipe recipe: recipes) {
      nodes.add(new RecipeNode(recipe, DIM));
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
    for (int i = 0; i < DIM; i++) {
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
      targetCoords.add(sum / DIM);
    }


    return new RecipeNode(targetCoords, DIM);
  }


  private List<Recipe> getRecommendedRecipes(List<Recipe> userHistory) throws RecommendationException {
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
