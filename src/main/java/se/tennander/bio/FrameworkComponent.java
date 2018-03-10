package se.tennander.bio;

import dagger.Component;
import javax.inject.Singleton;

/**
 * This component encapsulates a framework allowing developers
 * to get http calls routed to method calls.
 * The service handles both synchronous callbacks and Reactive Asynchronous calls.
 */
@Singleton
@Component(modules = FrameworkModule.class)
public interface FrameworkComponent {
  Framework getFramework();
}
