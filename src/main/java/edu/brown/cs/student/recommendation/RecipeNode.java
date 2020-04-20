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
  private int dim;
  private String id;
  
  
  public RecipeNode(Recipe r, int dim) {
    this.recipe = r;
    this.dim = dim;
    this.id = r.getUri();
    this.generateCoordsFromRecipe();
    
  }
  
  private void generateCoordsFromRecipe() {
    coordinates = new double[dim];
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
    return id;
  }

  @Override
  public List<Double> getCoords() {
    return null;
  }

  @Override
  public RecipeNode getLeftChild() {
    return this.leftNode;
  }

  @Override
  public void setLeftChild(RecipeNode lc) {
    this.leftNode = lc;
  }

  @Override
  public RecipeNode getRightChild() {
    return this.rightNode;
  }

  @Override
  public void setRightChild(RecipeNode rc) {
    this.rightNode = rc;
  }
}