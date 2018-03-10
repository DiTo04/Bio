package se.tennander.bio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import io.reactivex.Flowable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.Route;

@RunWith(MockitoJUnitRunner.class)
public class HttpConnectorTest {

  private static final String URI_PREFIX = "prefix";
  private static final String GET_URI = "uri/path";
  private static final String SYNCHRONOUS_URI = "syncrounus/path";
  private static final String REACTIVE_URI = "reactive/uri";
  private static final String ERROR_MESSAGE = "I'm being evil!";
  private HttpConnector target;

  @Mock
  private HttpService httpService;
  @Captor
  private ArgumentCaptor<Route> routeCaptor;
  @Spy
  private TestService testService;
  @Spy
  private TestServiceWithSameUri testServiceWithSameUri;
  @Mock
  private Request request;
  @Mock
  private Response response;

  @Test
  public void singleGetSparkMethodShouldConnect() throws Exception {
    // Given
    target = new HttpConnector(testService, httpService, URI_PREFIX);
    // When
    target.connect();
    // Then
    verify(httpService).createGetEndpoint(
        eq(URI_PREFIX + "/" + SYNCHRONOUS_URI), routeCaptor.capture());
    routeCaptor.getValue().handle(request, response);
    verify(testService).simpleSynchronousGetRequest(request, response);
  }

  @Test
  public void singlePostSparkMethodShouldConnect() throws Exception {
    // Given
    target = new HttpConnector(testService, httpService, URI_PREFIX);
    // When
    target.connect();
    // Then
    verify(httpService).createPostEndpoint(
        eq(URI_PREFIX + "/" + SYNCHRONOUS_URI), routeCaptor.capture());
    routeCaptor.getValue().handle(request, response);
    verify(testService).simpleSynchronousPostRequest(request, response);
  }

  @Test
  public void singleReactiveMethodShouldConnect() throws Exception {
    // Given
    target = new HttpConnector(testService, httpService, URI_PREFIX);
    // When
    target.connect();
    // Then
    verify(httpService).createGetEndpoint(
        eq(URI_PREFIX + "/" + REACTIVE_URI), routeCaptor.capture());
    routeCaptor.getValue().handle(request, response);
    verify(testService).simpleReactiveGetRequest(any());
  }

  @Test
  public void asyncMethodOnSharedUriShouldConnect() throws Exception {
    // Given
    target = new HttpConnector(testServiceWithSameUri, httpService, URI_PREFIX);
    // When
    target.connect();
    // Then
    verify(httpService).createGetEndpoint(
        eq(URI_PREFIX + "/" + GET_URI), routeCaptor.capture());
    routeCaptor.getValue().handle(request, response);
    verify(testServiceWithSameUri).reactiveGetRequestOnSameUri(any());
  }

  @Test
  public void syncMethodOnSharedUriShouldConnect() throws Exception {
    // Given
    target = new HttpConnector(testServiceWithSameUri, httpService, URI_PREFIX);
    // When
    target.connect();
    // Then
    verify(httpService).createGetEndpoint(
        eq(URI_PREFIX + "/" + GET_URI), routeCaptor.capture());
    routeCaptor.getValue().handle(request, response);
    verify(testServiceWithSameUri).synchronousGetRequestOnSameUri(request, response);
  }

  @Test(expected = BioException.class)
  public void exceptionShouldBePropegated() throws Exception {
    // Given
    target = new HttpConnector(new EvilService(), httpService, URI_PREFIX);
    // When
    target.connect();
    // Then: Exception should be thrown.
  }

  private static class TestService implements Service {

    @Post(SYNCHRONOUS_URI)
    public String simpleSynchronousPostRequest(Request req, Response res) {
      return "Done";
    }

    @Get(SYNCHRONOUS_URI)
    public String simpleSynchronousGetRequest(Request req, Response res) {
      return "Done";
    }

    @Get(REACTIVE_URI)
    public void simpleReactiveGetRequest(Flowable<Request> flowable) {
      // Pass
    }
  }

  private static class TestServiceWithSameUri implements Service {
    @Get(GET_URI)
    public String synchronousGetRequestOnSameUri(Request req, Response res) {
      return "Done!";
    }

    @Get(GET_URI)
    public void reactiveGetRequestOnSameUri(Flowable<Request> flowable) {
      // Pass
    }
  }

  private static class EvilService implements Service {
    @Get(GET_URI)
    private void asyncCall(Flowable<Request> flowable) throws Exception {
      throw new Exception(ERROR_MESSAGE);
    }
  }
}