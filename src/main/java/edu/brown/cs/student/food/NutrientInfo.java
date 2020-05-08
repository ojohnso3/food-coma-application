package edu.brown.cs.student.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NutrientInfo {
  private static Map<String, String[]> nutrients;
  private static Map<String, String> allNutrients;
  private static List<String> nutrientCodes;
  private static List<String> mainNutrients; //carbs, sugars, fats, kcal, sugara, protein
  private static List<String> secondaryNutrients;
  //calcium, cholesterol, monof, polyf, tranf, iron, fiber, potas, satf, b12

  private NutrientInfo() { }

  public static Map<String, String[]> getNutrients() {
    return nutrients;
  }

  public static List<String> getNutrientCodes() {
    return nutrientCodes;
  }

  public static List<String> getMainNutrients() {
    return mainNutrients;
  }

  public static List<String> getSecondaryNutrients() {
    return secondaryNutrients;
  }

  /**
   * @return nutrients - hashmap of nutrient codes to an array of nutrient names and units.
   */
  public static Map<String, String> getNutrientsMap() {
    return allNutrients;
  }

  /**
   * This function sets up the hashmap of nutrients that recipes include.
   * This hashmap will be used in recipe parsing.
   */
  public static void createNutrientsList() {
    nutrients = new HashMap<>();
    allNutrients = new HashMap<>();
    nutrientCodes = new ArrayList<>();
    mainNutrients = new ArrayList<>();
    secondaryNutrients = new ArrayList<>();

    setMainNutrients();
    setSecondaryNutrients();
//    setOtherNutrients();
  }

  private static void setMainNutrients() {
    String[] energy = new String[2];
    energy[0] = "Energy";
    energy[1] = "kcal";
    nutrients.put("ENERC_KCAL", energy);
    allNutrients.put("Energy", "ENERC_KCAL");
    nutrientCodes.add("ENERC_KCAL");
    mainNutrients.add("ENERC_KCAL");

    String[] protein = new String[2];
    protein[0] = "Protein";
    protein[1] = "g";
    nutrients.put("PROCNT", protein);
    allNutrients.put("Protein", "PROCNT");
    nutrientCodes.add("PROCNT");
    mainNutrients.add("PROCNT");

    String[] carbs = new String[2];
    carbs[0] = "Carbs";
    carbs[1] = "g";
    nutrients.put("CHOCDF", carbs);
    allNutrients.put("Carbs", "CHOCDF");
    nutrientCodes.add("CHOCDF");
    mainNutrients.add("CHOCDF");

    String[] sugars = new String[2];
    sugars[0] = "Sugars";
    sugars[1] = "g";
    nutrients.put("SUGAR", sugars);
    allNutrients.put("Sugars", "SUGAR");
    nutrientCodes.add("SUGAR");
    mainNutrients.add("SUGAR");

    String[] fat = new String[2];
    fat[0] = "Fat";
    fat[1] = "g";
    nutrients.put("FAT", fat);
    allNutrients.put("Fat", "FAT");
    nutrientCodes.add("FAT");
    mainNutrients.add("CHOCDF");
  }

  private static void setSecondaryNutrients() {
    String[] calcium = new String[2];
    calcium[0] = "Calcium";
    calcium[1] = "mg";
    nutrients.put("CA", calcium);
    allNutrients.put("Calcium", "CA");
    nutrientCodes.add("CA");
    secondaryNutrients.add("CA");

    String[] cholesterol = new String[2];
    cholesterol[0] = "Cholesterol";
    cholesterol[1] = "mg";
    nutrients.put("CHOLE", cholesterol);
    allNutrients.put("Cholesterol", "CHOLE");
    nutrientCodes.add("CHOLE");
    secondaryNutrients.add("CHOLE");

    String[] monounsaturated = new String[2];
    monounsaturated[0] = "Monounstaturated";
    monounsaturated[1] = "g";
    nutrients.put("FAMS", monounsaturated);
    allNutrients.put("Monounsaturated", "FAMS");
    nutrientCodes.add("FAMS");
    secondaryNutrients.add("FAMS");

    String[] polyunsaturated = new String[2];
    polyunsaturated[0] = "Polyunsaturated";
    polyunsaturated[1] = "g";
    nutrients.put("FAPU", polyunsaturated);
    allNutrients.put("Polyunsaturated", "FAPU");
    nutrientCodes.add("FAPU");
    secondaryNutrients.add("FAPU");

    String[] trans = new String[2];
    trans[0] = "Trans";
    trans[1] = "g";
    nutrients.put("FATRN", trans);
    allNutrients.put("Trans", "FATRN");
    nutrientCodes.add("FATRN");
    secondaryNutrients.add("FATRN");

    String[] iron = new String[2];
    iron[0] = "Iron";
    iron[1] = "mg";
    nutrients.put("FE", iron);
    allNutrients.put("Iron", "FE");
    nutrientCodes.add("FE");
    secondaryNutrients.add("FE");

    String[] fiber = new String[2];
    fiber[0] = "Fiber";
    fiber[1] = "g";
    nutrients.put("FIBTG", fiber);
    allNutrients.put("Fiber", "FIBTG");
    nutrientCodes.add("FIBTG");
    secondaryNutrients.add("FIBTG");

    String[] folate = new String[2];
    folate[0] = "Folate (Equivalent)";
    folate[1] = "aeg";
    nutrients.put("FOLDFE", folate);
//    nutrientCodes.add("FOLDFE");
    // NO

    String[] potassium = new String[2];
    potassium[0] = "Potassium";
    potassium[1] = "mg";
    nutrients.put("K", potassium);
    allNutrients.put("Potassium", "K");
    nutrientCodes.add("K");
    secondaryNutrients.add("K");

    String[] sodium = new String[2];
    sodium[0] = "Sodium";
    sodium[1] = "mg";
    nutrients.put("NA", sodium);
    allNutrients.put("Sodium", "NA");
    nutrientCodes.add("NA");
    secondaryNutrients.add("NA");

    String[] sugara = new String[2];
    sugara[0] = "Sugars, added";
    sugara[1] = "g";
    nutrients.put("SUGAR.added", sugara);
    allNutrients.put("Sugarsadded", "SUGAR.added");
    nutrientCodes.add("SUGAR.added");
    secondaryNutrients.add("SUGAR.added");

    String[] saturated = new String[2];
    saturated[0] = "Saturated";
    saturated[1] = "g";
    nutrients.put("FASAT", saturated);
    allNutrients.put("Saturated", "FASAT");
    nutrientCodes.add("FASAT");
    secondaryNutrients.add("FASAT");

    String[] vitaminb12 = new String[2];
    vitaminb12[0] = "Vitamin B12";
    vitaminb12[1] = "aeg";
    nutrients.put("VITB12", vitaminb12);
    allNutrients.put("VitaminB12", "VITB12");
    nutrientCodes.add("VITB12");
    secondaryNutrients.add("VITB12");

    String[] vitamind = new String[2];
    vitamind[0] = "Vitamin D";
    vitamind[1] = "aeg";
    nutrients.put("VITD", vitamind);
    allNutrients.put("VitaminD", "VITD");
    nutrientCodes.add("VITD");
    secondaryNutrients.add("VITD");
  }
}
