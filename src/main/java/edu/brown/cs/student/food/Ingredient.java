package edu.brown.cs.student.food;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * Class comment.
 *
 */
public class Ingredient {

  private String foodId;
  private float quantity;
  private Measure measure;
  private double weight;
  private Food food;
  private String text;

  
  public Ingredient(String text, double weight) {
    this.text = text;
    this.weight = weight;
  }

  public String getText() {
    return text;
  }
  
}