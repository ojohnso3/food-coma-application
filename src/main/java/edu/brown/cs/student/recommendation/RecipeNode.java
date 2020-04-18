package edu.brown.cs.student.recommendation;

import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.kdtree.KDNode;

import java.util.List;

/**
 * 
 * Class comment.
 *
 */
public class RecipeNode implements KDNode<RecipeNode> {
  private Recipe recipe;
  private double[] coordinates;
  private RecipeNode leftNode;
  private RecipeNode rightNode; 
  
  
  public RecipeNode(Recipe r) {
    recipe = r;
    this.generateCoordsFromRecipe();
    
  }
  
  private void generateCoordsFromRecipe() {
    coordinates = new double[6];
    // TODO: set values based on recipe !!
  }
  
  /**
   * Comment.
   * @return Recipe object
   */
  public Recipe getRecipe() {
    return recipe;
  }


  @Override
  public String getId() {
    return null;
  }

  @Override
  public List<Double> getCoords() {
    return null;
  }

  @Override
  public RecipeNode getLeftChild() {
    return null;
  }

  @Override
  public void setLeftChild(RecipeNode lc) {

  }

  @Override
  public RecipeNode getRightChild() {
    return null;
  }

  @Override
  public void setRightChild(RecipeNode rc) {

  }
}