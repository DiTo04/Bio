package se.tennander.bio;

import javax.inject.Inject;
import spark.Route;
import spark.Spark;

public class HttpService {

  @Inject
  HttpService() {
  }

  public void createGetEndpoint(String path, Route route) {
    Spark.get(path, route);
  }

  public void createPostEndpoint(String path, Route route) {
    Spark.post(path, route);
  }
}
