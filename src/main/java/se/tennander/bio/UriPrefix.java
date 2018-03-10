package se.tennander.bio;

import java.lang.annotation.Documented;
import javax.inject.Qualifier;

/**
 * {@link Qualifier} specifying what prefix the {@link ServiceInitializerImpl}
 *  should add to the URL for all it's services.
 */
@Qualifier
@Documented
@interface UriPrefix {
}
