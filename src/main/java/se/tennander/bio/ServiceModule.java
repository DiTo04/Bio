package se.tennander.bio;

import dagger.Module;
import dagger.Provides;

/**
 * This module encapsulats all the needed setup before initializing the services.
 */
@Module
public class ServiceModule {
  private final int port;
  private final String uriPrefix;

  /**
   * Initialize the framework module.
   * @param port The port the Server should be active on.
   * @param uriPrefix THe prefix that the uri should have.
   */
  public ServiceModule(int port, String uriPrefix) {
    this.port = port;
    this.uriPrefix = uriPrefix;
  }

  @Provides
  ServiceInitializer providesFramework(ServiceInitializerImpl impl) {
    return impl;
  }

  @Provides
  @Port int providesPort() {
    return port;
  }

  @Provides
  @UriPrefix String providesUriPrefix() {
    return uriPrefix;
  }
}
