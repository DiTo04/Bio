package se.tennander.bio;

import static java.util.stream.Collectors.toList;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.flowables.ConnectableFlowable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import spark.Request;
import spark.Route;

class HttpConnector {

  private final Service service;
  private final HttpService httpService;
  private final String uriPrefix;
  private Collection<List<Method>> methodsSortedOnUri;

  @AutoFactory
  HttpConnector(
      Service service,
      @Provided HttpService httpService,
      @Provided @UriPrefix String uriPrefix) {
    this.service = service;
    this.httpService = httpService;
    this.uriPrefix = uriPrefix;
    methodsSortedOnUri = new HashSet<>();
  }

  void connect() throws FrameWorkException {
    parseForUris();
    for (List<Method> methodsForUri : methodsSortedOnUri) {
      connectUri(methodsForUri);
    }
  }

  private void parseForUris() {
    HashMap<String, List<Method>> uriToMethod = new HashMap<>();
    Stream.of(service.getClass().getDeclaredMethods())
        .forEach(method -> {
          Method newMethod = new Method(service, method);
          addToMap(newMethod, uriToMethod);
        });
    this.methodsSortedOnUri = uriToMethod.values();
  }

  private void addToMap(Method method, HashMap<String, List<Method>> uriToMethod) {
    if (!method.isApiCall()) {
      return;
    }
    String uri = method.getUri();
    List<Method> methodsForUri = uriToMethod.computeIfAbsent(uri, key -> new ArrayList<>());
    methodsForUri.add(method);
  }

  private void connectUri(List<Method> methods) throws FrameWorkException {
    List<Method> getMethods = methods.stream().filter(Method::isGet).collect(toList());
    connectSameUriAndCallType(getMethods);
    List<Method> postMethods = methods.stream().filter(Method::isPost).collect(toList());
    connectSameUriAndCallType(postMethods);
  }

  private void connectSameUriAndCallType(List<Method> getMethods) {

    Optional<Method> asyncGet = getMethods.stream()
        .filter(Method::isAsync)
        .findAny();
    Optional<Method> syncGet = getMethods.stream()
        .filter(Method::isSync)
        .findAny();

    if (asyncGet.isPresent() && syncGet.isPresent()) {
      connectTwoMethods(asyncGet.get(), syncGet.get());
    } else {
      asyncGet.ifPresent(this::connectAloneMethod);
      syncGet.ifPresent(this::connectAloneMethod);
    }

  }

  private void connectTwoMethods(Method asyncMethod, Method syncMethod) {
    createAsynchronousCallback(asyncMethod, syncMethod.getCallBack());
  }

  private void connectAloneMethod(Method method) {
    if (method.isAsync()) {
      Route acceptRoute = (req, res) -> {
        res.status(201);
        return "Accepted";
      };
      createAsynchronousCallback(method, acceptRoute);
    } else if (method.isSync()) {
      createHttpCallback(method);
    }
  }

  private void createAsynchronousCallback(Method method, Route responseRoute) {
    ConnectableFlowable<Request> requests = createFlowableFromHttpEndpoint(method, responseRoute);
    requests.connect();
    tryToCallMethod(method, requests);
  }

  private void tryToCallMethod(
      Method method, ConnectableFlowable<Request> requests) throws FrameWorkException {
    try {
      method.subscribe(requests);
    } catch (InvocationTargetException | IllegalAccessException e) {
      String reason = Optional.ofNullable(e.getCause())
          .map(Throwable::getMessage)
          .orElse(e.getMessage());
      throw new FrameWorkException(
          "Could not connect to method "
              + method.getName() + ". Because " + reason);
    }
  }

  private ConnectableFlowable<Request> createFlowableFromHttpEndpoint(
      Method method, Route responseRoute) {
    Flowable<Request> flowable = Flowable.create(
        emitter ->
            connectEmitterToMethod(method, emitter, responseRoute),
        BackpressureStrategy.BUFFER);
    return flowable.publish();
  }

  private void connectEmitterToMethod(
      Method method, FlowableEmitter<Request> emitter, Route methodRoute) {
    Route route = (req, res) -> {
      if (!emitter.isCancelled()) {
        emitter.onNext(req);
      }
      return methodRoute.handle(req, res);
    };
    setUpEndpoint(method, route);
  }

  private void setUpEndpoint(Method method, Route route) {
    String path = uriPrefix + "/" + method.getUri();
    if (method.isGet()) {
      httpService.createGetEndpoint(path, route);
    } else if (method.isPost()) {
      httpService.createPostEndpoint(path, route);
    }
  }

  private void createHttpCallback(Method method) {
    Route route = method.getCallBack();
    setUpEndpoint(method, route);
  }
}
