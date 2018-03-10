package se.tennander.bio;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import spark.Spark;


@Singleton
class ServiceInitializerImpl implements ServiceInitializer {

  private final HttpService httpService;
  private final HttpConnectorFactory sparkConnectorFactory;

  /**
   * Injectable constructor.
   * @param httpService The service used to connect to http endpoints.
   * @param httpConnectorFactory The connector used to parse Services.
   */
  @Inject
  ServiceInitializerImpl(
      HttpService httpService, HttpConnectorFactory httpConnectorFactory) {
    this.httpService = httpService;
    this.sparkConnectorFactory = httpConnectorFactory;
  }

  @Override
  public void setUpServices(Set<Service> services) throws BioException {
    httpService.setUp();
    for (Service service : services) {
      HttpConnector httpConnector = sparkConnectorFactory.create(service);
      httpConnector.connect();
    }
    httpService.init();
    Spark.awaitInitialization();
  }
}
