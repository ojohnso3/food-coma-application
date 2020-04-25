package edu.brown.cs.student.recommendation;


import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This class will read in inputs from the user, and link to the Recommender class
 * which will be able to find specific recipes to return to the user.
 */
public class InputMatcher {
  private String input;
  private User user;

  public InputMatcher(String input, User user) {
    this.input = input;
    this.user = user;
  }

  public List<Recipe> getResults() throws RecommendationException, InterruptedException,
          APIException, IOException, SQLException {
    Recommender recommender = new Recommender(this.user);
    return recommender.makeRecommendation(input);
  }
  

}