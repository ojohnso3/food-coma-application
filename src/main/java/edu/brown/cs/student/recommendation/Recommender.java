
package edu.brown.cs.student.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  //TODO: figure out which version of the algorithm we want to use!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  //Need to change findTargetNode and initRecipeTree based on this.

  public Recommender() {
    this.recipeTree = new KDTree<>(DIM);
    this.initRecipeTree();
  }

  /**
   * Function to initialize the KDTree to be used when recommending recipes to users. (maybe change this!!!!!!!!!!!!!!!!!!!!)
   */
  private void initRecipeTree() {
    List<Recipe> recipesList = RecipeDatabase.getRecipeSubset(TREE_INIT_SIZE);
    //check for errors here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    List<RecipeNode> nodesList = this.convertRecipesToRecipeNodes(recipesList);
    this.recipeTree.initializeTree(nodesList);
  }
  
  /**
   * Comment.
   * @param user Id of current user
   * @param input Search input of user
   * @return List of recommended recipes
   */
  public List<Recipe> makeRecommendation(User user, String input) throws RecommendationException {
    List<Recipe> recs;
    List<Recipe> userHistory = user.getPreviousRecipes();
    try {
      recs = this.getRecommendedRecipes(input, userHistory);
    } catch (RecommendationException e) {
      throw new RecommendationException(e.getMessage());
    }

    for (Recipe r : recs) { //maybe only want to save recent history?????????????????????????????????
      user.addToPreviousRecipes(r); // figure out order and backwards?
    }
    return recs;
  }
  
//  private KDTree<RecipeNode> getKDTree(String userID) {
//    KDTree<RecipeNode> kdtree;
//    if (userTrees.containsKey(userID)) { // TODO: deal with the storage of user-specific kdtrees
//      kdtree = userTrees.get(userID);
//    } else {
//      Recipe rootRecipe = new Recipe("ROOT"); // fake node
//      RecipeNode rootNode = new RecipeNode(rootRecipe);
//      int dims = 6; // make dims based on user or universal?
//      kdtree = new KDTree<>(dims);
//      List<RecipeNode> nodes = RecipeDatabase.getRecipeSubset(); // TODO: make method in RecipeDatabase class
//      nodes.addAll(this.convertRecipesToRecipeNodes(userRecs.get(userID))); // adds user history to nodes list
//      kdtree.initializeTree(nodes);
//      userTrees.put(userID, kdtree);
//    }
//    return kdtree;
//  }

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
   * @param input - the query that was inputted by the user.
   * @return - a RecipeNode at the coordinates determined by the query and the user's history.
   */
  private RecipeNode getTargetNode(String input, List<Recipe> userHistory) {
    return null;
  }


  private List<Recipe> getRecommendedRecipes(String input, List<Recipe> userHistory) throws RecommendationException {
    List<Recipe> recs = new ArrayList<>();

    RecipeNode target = this.getTargetNode(input, userHistory);

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
