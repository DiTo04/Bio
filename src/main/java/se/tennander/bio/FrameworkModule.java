package se.tennander.bio;

import dagger.Module;
import dagger.Provides;

/**
 * This module encapsulats all the needed setup for Framework.
 */
@Module
public class FrameworkModule {
  private final int port;
  private final String uriPrefix;

  /**
   * Initialize the framework module.
   * @param port The port the Server should be active on.
   * @param uriPrefix THe prefix that the uri should have.
   */
  public FrameworkModule(int port, String uriPrefix) {
    this.port = port;
    this.uriPrefix = uriPrefix;
  }

  @Provides
  Framework providesFramework(FrameworkImpl impl) {
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
