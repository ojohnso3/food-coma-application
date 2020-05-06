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
  private List<Double> coordinates;
  private RecipeNode leftNode;
  private RecipeNode rightNode;
  private String id;

  public RecipeNode(Recipe r) {
    this.recipe = r;
    this.id = r.getUri();
  }

  public RecipeNode(List<Double> coordinates) {
    this.coordinates = coordinates;
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
    return this.coordinates;
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

  public void setCoords(List<Double> coords) {
    this.coordinates = coords;
  }
}
