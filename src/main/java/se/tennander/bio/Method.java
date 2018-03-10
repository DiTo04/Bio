package se.tennander.bio;

import io.reactivex.flowables.ConnectableFlowable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

class Method {
  private final Service service;
  private final java.lang.reflect.Method method;

  Method(Service service, java.lang.reflect.Method method) {
    this.service = service;
    this.method = method;
  }

  boolean isApiCall() {
    return isPost() || isGet();
  }

  String getUri() {
    if (isGet()) {
      return method.getAnnotation(Get.class).value();
    } else if (isPost()) {
      return method.getAnnotation(Post.class).value();
    } else {
      throw new BioException("Getting Uri on nether Get or Post method!");
    }
  }

  boolean isPost() {
    return method.isAnnotationPresent(Post.class);
  }

  boolean isGet() {
    return method.isAnnotationPresent(Get.class);
  }

  boolean isAsync() {
    return method.getParameterTypes().length == 1;
  }

  void subscribe(ConnectableFlowable<Request> requests)
      throws InvocationTargetException, IllegalAccessException {
    method.setAccessible(true);
    method.invoke(service, requests);
  }

  String getName() {
    return method.getName();
  }

  boolean isSync() {
    List<Class<?>> params = Arrays.asList(method.getParameterTypes());
    boolean isSync = Object.class.isAssignableFrom(method.getReturnType());
    isSync &= params.size() == 2;
    isSync &= params.contains(Request.class) && params.contains(Response.class);
    return isSync;
  }

  Route getCallBack() {
    method.setAccessible(true);
    return ((request, response) -> method.invoke(service, request, response));
  }
}
