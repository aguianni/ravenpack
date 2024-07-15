package com.example.ravenpack.application;

import com.example.ravenpack.application.port.out.ScoringServicePort;
import com.example.ravenpack.application.port.out.TranslationServicePort;
import com.example.ravenpack.infrastructure.out.AsyncScoringService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AsyncScoringServiceTest {

    @Mock
    private ScoringServicePort scoringService;

    @Mock
    private TranslationServicePort translationService;


    @Test
    public void testOK() throws ExecutionException, InterruptedException {
        AsyncScoringService asyncScoringService = new AsyncScoringService(scoringService, translationService);

        when(translationService.run("hello")).thenReturn("hello");
        when(scoringService.run("hello")).thenReturn(1.0f);

        Float result = asyncScoringService.run("hello").get();
        Assert.assertEquals(1.0f, result.floatValue(), 0);

        verify(translationService, times(1)).run(any(String.class));
        verify(scoringService, times(1)).run(any(String.class));
    }


    @Test
    public void testFailScoringService() throws ExecutionException, InterruptedException {
        AsyncScoringService asyncScoringService = new AsyncScoringService(scoringService, translationService);

        when(translationService.run("bad")).thenReturn("bad");
        when(scoringService.run("bad")).thenThrow(new RuntimeException("error"));

        assertThrows(RuntimeException.class, () -> {
            asyncScoringService.run("bad");
        });

        verify(translationService, times(1)).run("bad");
        verify(scoringService, times(1)).run("bad");
    }

    @Test
    public void testFailTranslateService() throws ExecutionException, InterruptedException {
        AsyncScoringService asyncScoringService = new AsyncScoringService(scoringService, translationService);

        when(translationService.run("bad")).thenThrow(new RuntimeException("error"));
        when(scoringService.run("bad")).thenReturn(1.0f);

        assertThrows(RuntimeException.class, () -> {
            asyncScoringService.run("bad");
        });

        verify(translationService, times(1)).run("bad");
        verify(scoringService, times(0)).run("bad");
    }
}
