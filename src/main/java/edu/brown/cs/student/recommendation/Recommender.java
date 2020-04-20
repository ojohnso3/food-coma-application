package edu.brown.cs.student.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.kdtree.KDTreeException;

/**
 * This class contains the code to compile recipe recommendations based on user input and history.
 */
public class Recommender {
  private KDTree<RecipeNode> recipeTree;
  private HashMap<String, List<Recipe>> userRecs;
  private int dim;
  private static final int TREE_INIT_SIZE = 50;

  
  public Recommender(int dim) {
    this.dim = dim;
    this.recipeTree = new KDTree<>(dim);
    this.userRecs = new HashMap<>();
  }

  /**
   * Function to initialize the KDTree to be used when recommending recipes to users. (maybe change this!!!!!!!!!!!!!!!!!!!!)
   */
  public void initRecipeTree() {
    List<Recipe> recipesList = RecipeDatabase.getRecipeSubset(TREE_INIT_SIZE);
    //check for errors here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    List<RecipeNode> nodesList = this.convertRecipesToRecipeNodes(recipesList);
    this.recipeTree.initializeTree(nodesList);
  }
  
  /**
   * Comment.
   * @param userID Id of current user
   * @param input Search input of user
   * @return List of recommended recipes
   */
  public List<Recipe> makeRecommendation(String userID, String input) throws RecommendationException {
    List<Recipe> recs;
    List<Recipe> userHistory = this.userRecs.get(userID);
    try {
      recs = this.getRecommendedRecipes(input, userHistory);
    } catch (RecommendationException e) {
      throw new RecommendationException(e.getMessage());
    }

    if (!userRecs.containsKey(userID)) {
      userRecs.put(userID, new ArrayList<>());
    }
    for (Recipe r : recs) { //maybe only want to save recent history?????????????????????????????????
      userRecs.get(userID).add(r); // figure out order and backwards?
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
   * Function to find coordinates for Recipes and convert them to nodes.
   * @param recipes - a list of Recipes to convert to nodes.
   * @return - a list of the converted RecipeNodes.
   */
  private List<RecipeNode> convertRecipesToRecipeNodes(List<Recipe> recipes) { //need to add functionality to create coordinates for each node
    List<RecipeNode> nodes = new ArrayList<RecipeNode>();
    for (Recipe recipe: recipes) {
      nodes.add(new RecipeNode(recipe));
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
    int quantity = 10;

    // search for the nearest/most relevant recipes
    List<RecipeNode> recipeNodes;
    try {
      recipeNodes = recipeTree.nearestSearch(target, quantity);
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