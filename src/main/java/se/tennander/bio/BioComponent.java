package se.tennander.bio;

import dagger.Component;
import javax.inject.Singleton;

/**
 * This component encapsulates the Bio framework allowing developers
 * to get http calls routed to method calls.
 * Developers creates {@link Service}s that they then can
 * initialize with the {@link ServiceInitializer}.
 * A service can handle both synchronous callbacks and Reactive Asynchronous calls.
 *
 * <p>Use the Dagger created {@link DaggerBioComponent}
 * to construct an implementation of this interface.</p>
 */
@Singleton
@Component(modules = ServiceModule.class)
public interface BioComponent {
  ServiceInitializer getServiceInitializer();
}
