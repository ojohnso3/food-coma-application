package edu.brown.cs.student.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.kdtree.KDTree;

/**
 * 
 * Class comment.
 *
 */
public class Recommender {
  private HashMap<String, KDTree<RecipeNode>> userTrees;
  private HashMap<String, List<Recipe>> userRecs;

  
  public Recommender() {
    userTrees = new HashMap<String, KDTree<RecipeNode>>();
    userRecs = new HashMap<String, List<Recipe>>();
  }
  
  /**
   * Comment.
   * @param userID Id of current user
   * @param input Search input of user
   * @return List of recommended recipes
   */
  public List<Recipe> makeRecommendation(String userID, String input) {
    KDTree<RecipeNode> kdtree = this.getKDTree(userID);
    List<Recipe> recs = this.getRecommendedRecipes(kdtree);
    
    if (!userRecs.containsKey(userID)) {
      userRecs.put(userID, new ArrayList<Recipe>());
    }
    for (Recipe r : recs) {
      userRecs.get(userID).add(r); // figure out order and backwards?
    }
    return recs;
  }
  
  private KDTree<RecipeNode> getKDTree(String userID) {
    KDTree<RecipeNode> kdtree;
    if (userTrees.containsKey(userID)) { // TODO: deal with the storage of user-specific kdtrees
      kdtree = userTrees.get(userID);
    } else {
      Recipe rootRecipe = new Recipe("ROOT"); // fake node
      RecipeNode rootNode = new RecipeNode(rootRecipe);
      int dims = 6; // make dims based on user or universal?
      kdtree = new KDTree<RecipeNode>(dims, rootNode);
      List<RecipeNode> nodes = null; //RecipeDatabase.getRecipeSubset(); // TODO: make method in RecipeDatabase class
      nodes.addAll(this.convertRecipesToRecipeNodes(userRecs.get(userID))); // adds user history to nodes list
      kdtree.buildTree(0, nodes);
      userTrees.put(userID, kdtree);
    }
    return kdtree;
  }
  
  private List<RecipeNode> convertRecipesToRecipeNodes(List<Recipe> recipes) {
    List<RecipeNode> nodes = new ArrayList<RecipeNode>();
    for (Recipe recipe: recipes) {
      nodes.add(new RecipeNode(recipe));
    }
    return nodes;
  }

  private List<Recipe> getRecommendedRecipes(KDTree<RecipeNode> kdtree) {
    List<Recipe> recs = new ArrayList<Recipe>();

    // bulk of algorithm
    Recipe targetRecipe = new Recipe("TARGET"); // MAKE TARGET ACCURRATE TO SEARCH REQUIREMENTS
    RecipeNode targetNode = new RecipeNode(targetRecipe);
    int quantity = 10;
    MinMaxPriorityQueue<RecipeNode> recipeOutput = kdtree.findKNearest(quantity, targetNode, false);

    while (!recipeOutput.isEmpty()) {
      RecipeNode node = recipeOutput.pollFirst();
      recs.add(node.getRecipe());
    }
    return recs;
  }



}