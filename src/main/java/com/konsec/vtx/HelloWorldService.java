package com.konsec.vtx;

import examples.HelloReply;
import examples.HelloRequest;
import io.vertx.core.Future;

public class HelloWorldService extends examples.VertxGreeterGrpc.GreeterVertxImplBase {
    @Override
    public Future<HelloReply> sayHello(HelloRequest request) {
        String sess = SessionContextInterceptor.get("sessionId");
        System.out.println("Got request, sessionId: " + sess);
        return Future.succeededFuture(
                HelloReply.newBuilder()
                        .setMessage(sess)
                        .build());
    }
}
