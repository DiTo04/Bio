package se.tennander.bio;

import java.lang.annotation.Documented;
import javax.inject.Qualifier;

/**
 * {@link Qualifier} specifying that the injectable class
 * is configurable to use a specified port.
 */
@Qualifier
@Documented
@interface Port {
}
