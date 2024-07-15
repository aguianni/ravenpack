package com.example.ravenpack.infrastructure.out;

import com.example.ravenpack.application.port.out.AsyncScoringServicePort;
import com.example.ravenpack.application.port.out.ScoringServicePort;
import com.example.ravenpack.application.port.out.TranslationServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncScoringService implements AsyncScoringServicePort {

    Logger logger = LoggerFactory.getLogger(AsyncScoringService.class);

    private ScoringServicePort scoringService;
    private TranslationServicePort translationService;

    public AsyncScoringService(ScoringServicePort scoringService, TranslationServicePort translationService) {
        this.scoringService = scoringService;
        this.translationService = translationService;
    }

    @Async
    @Cacheable("scoreCache")
    public CompletableFuture<Float> run(String in){
        logger.info("Run starts with: " + in);
        return CompletableFuture.completedFuture(scoringService.run(translationService.run(in)));
    }
}
