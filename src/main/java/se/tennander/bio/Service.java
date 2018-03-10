package se.tennander.bio;

import io.reactivex.Flowable;
import spark.Request;
import spark.Response;

/**
 * A Service is a class with methods annotated with {@link Get} and/or {@link Post}.
 *
 * <p><b>Synchronous calls</b><br>
 *  For synchronous calls the annotated method signature needs to be:
 *  <pre>
 *    Object methodName({@link Request} request, {@link Response} response) {
 *    }
 *  </pre>
 *  These methods will be called as callbacks during the handling of the http request
 *  and should thus be kept fast and short. To handle longer transactions
 *  the recommended approach is to use both a synchronous callback and a Asynchronous Flowable.
 *
 * <p><b>Asynchronous calls</b><br>
 *  For asyncrounus calls the annotated method signature should be:
 *  <pre>
 *    void methodName({@link Flowable}&lt;{@link Request}&gt; flowable) {
 *    }
 *  </pre>
 *  These methods will be called during the setup allowing each method to subscribe to the Flowable.
 *  If not synchronous callback is created a simple {@code 201 "Accepted"}
 *  will be sent as the response.
 */
public interface Service {
}
