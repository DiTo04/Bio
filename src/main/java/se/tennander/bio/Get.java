package se.tennander.bio;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signals that the service wants to accept
 * {@code GET} requests on the given URI.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Get {
  /**
   * The URI that should be associated with this call.
   * @return The uri.
   */
  String value();
}