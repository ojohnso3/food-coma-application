package edu.brown.cs.student.food;

/**
 *
 * Ingredient class for storing query info.
 *
 */
public class Ingredient {
  private final double weight;
  private final String text;

  /**
   * constructor.
   * @param text t
   * @param weight w
   */
  public Ingredient(String text, double weight) {
    this.text = text;
    this.weight = weight;
  }

  /**
   * getter.
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * getter.
   * @return weight
   */
  public double getWeight() {
    return weight;
  }
}
