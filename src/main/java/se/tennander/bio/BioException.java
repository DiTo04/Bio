package se.tennander.bio;


/**
 * {@link Exception} thrown by the Bio framework if anything goes wrong.
 */
public class BioException extends RuntimeException {

  /**
   * Takes a message and passes it to the underlying {@link RuntimeException}.
   * @param message The message about this exception.
   */
  public BioException(String message) {
    super(message);
  }
}
