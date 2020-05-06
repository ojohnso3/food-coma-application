package edu.brown.cs.student.food;

/**
 *
 * Ingredient class for storing query info.
 *
 */
public class Ingredient {
  private final double weight;
  private final String text;

  public Ingredient(String text, double weight) {
    this.text = text;
    this.weight = weight;
  }

  public String getText() {
    return text;
  }
  public double getWeight() {
    return weight;
  }
}
