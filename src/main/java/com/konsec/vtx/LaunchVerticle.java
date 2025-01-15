package com.konsec.vtx;

import io.grpc.ServerInterceptors;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

public class LaunchVerticle extends AbstractVerticle {
    private VertxServer server;
    private VertxServer server2;

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        System.out.println("Starting grpc server");
        HelloWorldService service = new HelloWorldService();
        SessionContextInterceptor interceptor = new SessionContextInterceptor();
        var interceptedService = ServerInterceptors.intercept(service, interceptor);
        VertxServerBuilder vBuilder = VertxServerBuilder.forPort(vertx, 53531)
                .addService(interceptedService);
        server = vBuilder.build();
        server.start();
        System.out.println("grpc Server started");

        VertxServerBuilder vBuilder2 = VertxServerBuilder.forPort(vertx, 53532)
                .addService(service);
        server2 = vBuilder2.build();
        server2.start();
        System.out.println("grpc Server 2 started");


    }

}
