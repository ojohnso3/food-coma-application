package edu.brown.cs.student.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

import edu.brown.cs.student.database.RecipeDatabase;
import edu.brown.cs.student.food.Recipe;

/**
 * 
 * Class comment.
 *
 */
public class Recommender {
  private HashMap<String, KDTree<RecipeNode>> userTrees;
  
  public Recommender() {
    userTrees = new HashMap<String, KDTree<RecipeNode>>();
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
    return recs;
  }
  
  private KDTree<RecipeNode> getKDTree(String userID) {
    KDTree<RecipeNode> kdtree;
    if (userTrees.containsKey(userID)) {
      kdtree = userTrees.get(userID);
    } else {
      Recipe rootRecipe = new Recipe("ROOT"); // fake node
      RecipeNode rootNode = new RecipeNode(rootRecipe);
      int dims = 6; // make dims based on user or universal?
      kdtree = new KDTree<RecipeNode>(dims, rootNode);
      List<RecipeNode> nodes = RecipeDatabase.getRecipeSubset();
      kdtree.buildTree(0, nodes);
      userTrees.put(userID, kdtree);
    }
    return kdtree;
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