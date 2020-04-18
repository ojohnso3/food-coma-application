package edu.brown.cs.student.food;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NutrientInfo)) return false;
    NutrientInfo that = (NutrientInfo) o;
    return Objects.equals(this.label, that.label);
  }

  @Override
  public int hashCode() {
    return this.label.hashCode();
  }

}
