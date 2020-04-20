package edu.brown.cs.student.recommendation;


import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.User;

import java.util.List;

/**
 * This class will read in inputs from the user, and link to the Recommender class
 * which will be able to find specific recipes to return to the user.
 */
public class InputMatcher {
  String input;
  User user;
  
  public InputMatcher(String input, User user) {
    this.input = input;
    this.user = user;
  }
  
  public List<Recipe> getResults() throws RecommendationException {
    Recommender recommender = new Recommender();
    return recommender.makeRecommendation(user, input);
  }
  

}