package com.konsec.vtx;

import com.google.common.base.Strings;
import examples.HelloReply;
import examples.HelloRequest;
import examples.VertxGreeterGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

public class Client {
    private final ManagedChannel channel;

    public static void main(String[] args) {
        System.out.println("Calling grpc");

        var session = Strings.nullToEmpty(args[1]);
        System.out.println("Session:"+session);
        VertxOptions options = new VertxOptions();
        Vertx v = Vertx.vertx(options);

        Consumer<Vertx> runner = vertx2 -> {
            vertx2.runOnContext(vx -> {
                Client c = new Client("s3", 53531);
                c.doCall(null);

            });

            vertx2.runOnContext(vx -> {
                Client c = new Client("", 53532);
                c.doCall(vertx2);

            });

        };
        runner.accept(v);

    }

    private Client(String session, int port) {
        ManagedChannelBuilder builder = NettyChannelBuilder
                .forAddress("localhost", port)
                .usePlaintext();
        if (!Strings.isNullOrEmpty(session)) {
            Metadata extraHeaders = new Metadata();
            extraHeaders.put(
                    SessionContextInterceptor.SESSION_ID_METADATA_KEY, session);

            ClientInterceptor clientInterceptor = MetadataUtils
                    .newAttachHeadersInterceptor(extraHeaders);
            builder.intercept(clientInterceptor);
        }

        channel = builder.build();
    }

    private void doCall(Vertx vertx) {
        VertxGreeterGrpc.GreeterVertxStub stub = examples.VertxGreeterGrpc.newVertxStub(channel);

        examples.HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
        System.out.println("Doing call");
        Future<HelloReply> future = stub.sayHello(request);
        future
                .onSuccess(helloReply -> {
                            System.out.println("Got the server response: " + helloReply.getMessage());
                            if (vertx != null) {
                                vertx.close();
                            }


                        }
                       )
                .onFailure(err -> System.out.println("Could not reach server " + err));
    }

}
