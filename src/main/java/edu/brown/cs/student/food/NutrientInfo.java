package edu.brown.cs.student.food;

public class NutrientInfo {
  private String uri;
  private String label;
  private double quantity;
  private String units;

  public NutrientInfo(String label, double quantity, String units) {
    this.label = label;
    this.quantity = quantity;
    this.units = units;
  }

}
