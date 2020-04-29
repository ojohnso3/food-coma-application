package edu.brown.cs.student.food;

import java.util.HashMap;
import java.util.Map;

public final class NutrientInfo {

  /**
   * nutrients - hashmap of nutrient codes to an array of nutrient names and units.
   */
  public static Map<String, String[]> nutrients;
  public static Map<String, String> allNutrients;

  public static Map<String, String> getNutrientsMap() {
    return allNutrients;
  }
  
  /**
   * This function sets up the hashmap of nutrients that recipes include.
   * This hashmap will be used in recipe parsing.
   */
  public static void createNutrientsList() {
    nutrients = new HashMap<String, String[]>();
    
    allNutrients = new HashMap<String, String>();
    
    String[] calcium = new String[2];
    calcium[0] = "Calcium";
    calcium[1] = "mg";
    nutrients.put("CA", calcium);
    allNutrients.put("Calcium", "CA");

    String[] carbs = new String[2];
    calcium[0] = "Carbs";
    carbs[1] = "g";
    nutrients.put("CHOCDF", carbs);
    allNutrients.put("Carbs", "CHOCDF");

    String[] cholesterol = new String[2];
    cholesterol[0] = "Cholesterol";
    cholesterol[1] = "mg";
    nutrients.put("CHOLE", cholesterol);
    allNutrients.put("Cholesterol", "CHOLE");

    String[] monounsaturated = new String[2];
    monounsaturated[0] = "Monounstaturated";
    monounsaturated[1] = "g";
    nutrients.put("FAMS", monounsaturated);
    allNutrients.put("Monounsaturated", "FAMS");


    String[] polyunsaturated = new String[2];
    polyunsaturated[0] = "Polyunsaturated";
    polyunsaturated[1] = "g";
    nutrients.put("FAPU", polyunsaturated);
    allNutrients.put("Polyunsaturated", "FAPU");

    String[] sugars = new String[2];
    sugars[0] = "Sugars";
    sugars[1] = "g";
    nutrients.put("SUGAR", sugars);
    allNutrients.put("Sugars", "SUGAR");

    String[] fat = new String[2];
    fat[0] = "Fat";
    fat[1] = "g";
    nutrients.put("FAT", fat);
    allNutrients.put("Fat", "FAT");

    String[] trans = new String[2];
    trans[0] = "Trans";
    trans[1] = "g";
    nutrients.put("FATRN", trans);
    allNutrients.put("Trans", "FATRN");

    String[] iron = new String[2];
    iron[0] = "Iron";
    iron[1] = "mg";
    nutrients.put("FE", iron);
    allNutrients.put("Iron", "FE");

    String[] fiber = new String[2];
    fiber[0] = "Fiber";
    fiber[1] = "g";
    nutrients.put("FIBTG", fiber);
    allNutrients.put("Fiber", "FIBTG");

    String[] folate = new String[2];
    folate[0] = "Folate (Equivalent)";
    folate[1] = "aeg";
    nutrients.put("FOLDFE", folate);
    // NO

    String[] potassium = new String[2];
    potassium[0] = "Potassium";
    potassium[1] = "mg";
    nutrients.put("K", potassium);
    allNutrients.put("Potassium", "K");

    String[] magnesium = new String[2];
    magnesium[0] = "Magnesium";
    magnesium[1] = "mg";
    nutrients.put("MG", magnesium);
    allNutrients.put("Magnesium", "MG");

    String[] sodium = new String[2];
    sodium[0] = "Sodium";
    sodium[1] = "mg";
    nutrients.put("NA", sodium);
    allNutrients.put("Sodium", "NA");

    String[] vitaminb = new String[2];
    vitaminb[0] = "Vitamin B6";
    vitaminb[1] = "mg";
    nutrients.put("VITB6A", vitaminb);
    allNutrients.put("VitaminB6", "VITB6A");

    String[] energy = new String[2];
    energy[0] = "Energy";
    energy[1] = "kcal";
    nutrients.put("ENERC_KCAL", energy);
    allNutrients.put("Energy", "ENERC_KCAL");

    String[] niacin = new String[2];
    niacin[0] = "Niacin (B3)";
    niacin[1] = "mg";
    nutrients.put("NIA", niacin);
    // NO

    String[] phosphorus = new String[2];
    phosphorus[0] = "Phosphorus";
    phosphorus[1] = "mg";
    nutrients.put("P", phosphorus);
    // NO

    String[] protein = new String[2];
    protein[0] = "Protein";
    protein[1] = "g";
    nutrients.put("PROCNT", protein);
    allNutrients.put("Protein", "PROCNT");

    String[] riboflavin = new String[2];
    riboflavin[0] = "Riboflavin (B2)";
    riboflavin[1] = "mg";
    nutrients.put("RIBF", riboflavin);
    // NO

    String[] sugara = new String[2];
    sugara[0] = "Sugars, added";
    sugara[1] = "g";
    nutrients.put("SUGAR.added", sugara);
    allNutrients.put("Sugarsadded", "SUGAR.added");

    String[] saturated = new String[2];
    saturated[0] = "Saturated";
    saturated[1] = "g";
    nutrients.put("FASAT", saturated);
    allNutrients.put("Saturated", "FASAT");

    String[] vitamine = new String[2];
    vitamine[0] = "Vitamin E";
    vitamine[1] = "mg";
    nutrients.put("PROCNT", vitamine);
    allNutrients.put("VitaminE", "PROCNT");

    String[] vitamina = new String[2];
    vitamina[0] = "Vitamin A";
    vitamina[1] = "aeg";
    nutrients.put("VITA_RAE", vitamina);
    allNutrients.put("VitaminA", "VITA_RAE");

    String[] vitaminb12 = new String[2];
    vitaminb12[0] = "Vitamin B12";
    vitaminb12[1] = "aeg";
    nutrients.put("VITB12", vitaminb12);
    allNutrients.put("VitaminB12", "VITB12");

    String[] folatef = new String[2];
    folatef[0] = "Folate (food)";
    folatef[1] = "aeg";
    nutrients.put("FOLFD", folatef);
    // NO

    String[] vitaminc = new String[2];
    vitaminc[0] = "Vitamin C";
    vitaminc[1] = "mg";
    nutrients.put("VITC", vitaminc);
    allNutrients.put("VitaminC", "VITC");

    String[] vitamind = new String[2];
    vitamind[0] = "Vitamin D";
    vitamind[1] = "aeg";
    nutrients.put("VITD", vitamind);
    allNutrients.put("VitaminD", "VITD");

    String[] vitamink = new String[2];
    vitamink[0] = "VitaminK";
    vitamink[1] = "aeg";
    nutrients.put("VITK1", vitamink);
    allNutrients.put("VitaminK", "VITK1");

    String[] thiamin = new String[2];
    thiamin[0] = "Thiamin (B1)";
    thiamin[1] = "mg";
    nutrients.put("THIA", thiamin);
    // NO
  }
}
