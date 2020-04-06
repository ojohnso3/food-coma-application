package edu.brown.cs.student.general;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   * @throws IOException throws exception if necessary
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    
    // TODO: add functionality here
    
    
    
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    
    // TODO: add new spark routes here
    // SHOULD this method be in gui package ?
    
    // Setup previous Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);

    // Spark.post("/results", new SubmitHandler(), freeMarker);
  }


  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    /**
     * Creates map and returns an instance with model and view name.
     *
     * @param req The request
     * @param res The response
     * @return A model and view.
     */
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Stars: Query the database",
          "suggestions", "");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handle return requests on the Stars website.
   *
   */
  private static class SubmitHandler implements TemplateViewRoute {
    /**
     * This is the main functionality of the GUI.
     *
     * @param request  The request.
     * @param response The response.
     * @return A model and view.
     */
    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
      Map<String, Object> variables = null;
      
      request.queryMap();

      // TODO: add to this handler
      
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
