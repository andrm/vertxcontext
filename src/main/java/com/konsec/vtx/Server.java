package com.konsec.vtx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

public class Server {

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        Vertx v = Vertx.vertx(options);
        LaunchVerticle base = new LaunchVerticle();
        Consumer<Vertx> runner = vertx2 -> {
            System.out.println("Deploy start");
            vertx2.deployVerticle(base, ar -> {
                if (ar.succeeded()) {
                    System.out.println("LaunchVerticle started");
                } else {
                    System.err.println("Failed to start LaunchVerticle:"+ar.toString());
                }
            });
            System.out.println("Done...");
        };
        System.out.println("Starting server");
        runner.accept(v);
        System.out.println("Server started");

    }


}
