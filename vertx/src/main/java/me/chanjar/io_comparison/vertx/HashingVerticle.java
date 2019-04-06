package me.chanjar.io_comparison.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.codec.digest.DigestUtils;

public class HashingVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/hasher").handler(this::handleHash);

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  private void handleHash(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    String pathToFile = getFileBasedOnId(id);

    HttpServerResponse response = routingContext.response();
    // TODO 这里的set time属于作弊
    vertx.setTimer(1000, timer -> {
      vertx.fileSystem().readFile(pathToFile, handler -> {
        if (handler.succeeded()) {
          byte[] bytes = handler.result().getBytes();
          String hash = DigestUtils.sha512Hex(bytes);
          response.putHeader("content-type", "text/plain").end(hash);
        }
      });
    });
  }

  private String getFileBasedOnId(String id) {
    return "/Users/qianjia/tmp/reactor-test/img-" + id + ".png";
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    int availableProcessors = Runtime.getRuntime().availableProcessors();
    DeploymentOptions options = new DeploymentOptions()
        .setWorker(true)
        .setInstances(availableProcessors) // matches the worker pool size below
        .setWorkerPoolName("hashing-pool")
        .setWorkerPoolSize(availableProcessors);

    vertx.deployVerticle(HashingVerticle.class, options);
  }
}
