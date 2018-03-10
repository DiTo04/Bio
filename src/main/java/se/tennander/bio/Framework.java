package se.tennander.bio;

import java.util.Set;

/**
 * The framework in BIO is responsible to listen for api requests
 * and delegate them to the corresponding {@link Service}.
 */
public interface Framework {
  /**
   *  Makes the framework set up all services in the set. The method will return.
   * @param services The services to be activated.
   */
  void setUpServices(Set<Service> services) throws FrameWorkException;
}
