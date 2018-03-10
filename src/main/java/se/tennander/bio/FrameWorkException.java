package se.tennander.bio;


/**
 * {@link Exception} thrown by the framework if anything goes wrong.
 */
public class FrameWorkException extends RuntimeException {

  /**
   * Takes a message and passes it to the underlying {@link RuntimeException}.
   * @param message The message about this exception.
   */
  public FrameWorkException(String message) {
    super(message);
  }
}
