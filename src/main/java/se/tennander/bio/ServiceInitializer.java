package se.tennander.bio;

import java.util.Set;

/**
 * The {@link ServiceInitializer} connects the applications {@link Service}s
 * to the corresponding http endpoints.
 */
public interface ServiceInitializer {
  /**
   * Initializes all services in the set.
   * The method will return. But the thread this is called on will be held alive
   * until an exit request is received.
   * @param services The services to be activated.
   */
  void setUpServices(Set<Service> services) throws BioException;
}
