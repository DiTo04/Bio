package se.tennander.bio;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import spark.Spark;


@Singleton
class FrameworkImpl implements Framework {

  private final int port;
  private final HttpConnectorFactory sparkConnectorFactory;

  /**
   * Injectable constructor.
   * @param port The port used by the framework to listen to requests.
   * @param httpConnectorFactory The connector used to parse Services.
   */
  @Inject
  FrameworkImpl(
      @Port int port, HttpConnectorFactory httpConnectorFactory) {
    this.port = port;
    this.sparkConnectorFactory = httpConnectorFactory;
  }

  @Override
  public void setUpServices(Set<Service> services) throws FrameWorkException {
    Spark.port(port);
    for (Service service : services) {
      HttpConnector httpConnector = sparkConnectorFactory.create(service);
      httpConnector.connect();
    }
    Spark.init();
    Spark.awaitInitialization();
  }
}
