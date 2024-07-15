package com.example.ravenpack.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface AsyncScoringServicePort {

    CompletableFuture<Float> run(String in);
}
