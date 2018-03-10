package se.tennander.bio;

import javax.inject.Inject;
import javax.inject.Singleton;
import spark.Route;
import spark.Spark;

@Singleton
class HttpService {

  private final int port;

  @Inject
  HttpService(@Port int port) {
    this.port = port;
  }

  void createGetEndpoint(String path, Route route) {
    Spark.get(path, route);
  }

  void createPostEndpoint(String path, Route route) {
    Spark.post(path, route);
  }

  void setUp() {
    Spark.port(port);
  }

  void init() {
    Spark.init();
  }
}
