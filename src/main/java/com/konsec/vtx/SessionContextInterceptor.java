package com.konsec.vtx;

import io.grpc.Metadata;
import io.vertx.grpc.ContextServerInterceptor;

public class SessionContextInterceptor extends ContextServerInterceptor {

    public static final Metadata.Key<String> SESSION_ID_METADATA_KEY =
            Metadata.Key.of("X-SESSION-ID", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public void bind(Metadata metadata) {
        var s = metadata.get(SESSION_ID_METADATA_KEY);
        System.out.println("Session:"+s);
        put("sessionId", s);
    }
}
