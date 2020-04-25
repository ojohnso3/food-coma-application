package edu.brown.cs.student.recommendation;


import edu.brown.cs.student.database.APIException;
import edu.brown.cs.student.food.Recipe;
import edu.brown.cs.student.login.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

  /**
   * Function to get the results of a given query with the given params.
   * @param paramsMap - Map from param name to param option.
   * Available params: ingr (max num ingredients), diet (diet labels - only one allowed), health
   * (health labels), calories (range of calories), time (range of time),
   * excluded (food name). See here for more specific constraints:
   * https://developer.edamam.com/edamam-docs-recipe-api.
   * @return - a list of recipes to be recommended to the user.
   * @throws RecommendationException - thrown when the Recommender finds an error.
   * @throws InterruptedException - thrown when we can't connect to the API.
   * @throws APIException - thrown when the API returns an error code.
   * @throws IOException - thrown when we can't connect ot the API.
   * @throws SQLException - thrown when we can't query the database.
   */
  public List<Recipe> getResults(Map<String, String> paramsMap) throws RecommendationException,
      InterruptedException, APIException, IOException, SQLException {
    Recommender recommender = new Recommender(this.user);
    return recommender.makeRecommendation(input, paramsMap);
  }
  

}