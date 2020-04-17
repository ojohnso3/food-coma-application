package edu.brown.cs.student.recommendation;

import edu.brown.cs.student.food.Recipe;

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
  public void setLeft(RecipeNode left) {
    leftNode = left;
  }



  @Override
  public void setRight(RecipeNode right) {
    rightNode = right;
  }



  @Override
  public RecipeNode getLeft() {
    return leftNode;
  }



  @Override
  public RecipeNode getRight() {
    return rightNode;
  }



  @Override
  public double[] getCoords() {
    return coordinates;
  }



  @Override
  public void setCoords(double[] coords) {
    coordinates = coords;
    
  }



  @Override
  public double euclidDist(RecipeNode distToNode) {
    // TODO Auto-generated method stub
    return 0;
  }
  


}