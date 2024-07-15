package com.example.ravenpack.application;

import com.example.ravenpack.application.adapter.UserFlagGenerator;
import com.example.ravenpack.application.port.out.AsyncScoringServicePort;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserFlagGeneratorTest {

    @Mock
    private AsyncScoringServicePort  asyncScoringService;


    @Test
    public void testOK() {
        UserFlagGenerator userFlagGenerator = new UserFlagGenerator(asyncScoringService);

        when(asyncScoringService.run("hello")).thenReturn(CompletableFuture.completedFuture(1.0f));
        when(asyncScoringService.run("bye")).thenReturn(CompletableFuture.completedFuture(0.5f));
        when(asyncScoringService.run("nice")).thenReturn(CompletableFuture.completedFuture(0.4f));
        when(asyncScoringService.run("bad")).thenReturn(CompletableFuture.completedFuture(0.0f));
        when(asyncScoringService.run("no")).thenReturn(CompletableFuture.completedFuture(0.0f));

        Assert.assertEquals(TestUtils.getOutCSV(), userFlagGenerator.run(TestUtils.getCSV()));
        verify(asyncScoringService, times(7)).run(any(String.class));
    }

    @Test
    public void testFail() {

        UserFlagGenerator userFlagGenerator = new UserFlagGenerator(asyncScoringService);

        when(asyncScoringService.run("bad")).thenThrow(new RuntimeException("error"));

        assertThrows(RuntimeException.class, () -> {
            userFlagGenerator.run(TestUtils.getCSV());
        });

        verify(asyncScoringService, times(6)).run(any(String.class));
    }
/*
    @Test
    public void testOK1() throws CsvValidationException, IOException, ExecutionException, InterruptedException {
        UserFlagGenerator userFlagGenerator = new UserFlagGenerator(asyncScoringService);

        CompletableFuture<String> futureHello = CompletableFuture.completedFuture("hello");
        CompletableFuture<String> futureBye = CompletableFuture.completedFuture("bye");
        CompletableFuture<String> futureNice = CompletableFuture.completedFuture("nice");
        CompletableFuture<String> futureBad = CompletableFuture.completedFuture("bad");
        CompletableFuture<String> futureNo = CompletableFuture.completedFuture("no");

        when(translationService.run("hello")).thenReturn(futureHello);
        when(translationService.run("bye")).thenReturn(futureBye);
        when(translationService.run("nice")).thenReturn(futureNice);
        when(translationService.run("bad")).thenReturn(futureBad);
        when(translationService.run("no")).thenReturn(futureNo);


        when(scoringService.run(futureHello)).thenReturn(CompletableFuture.completedFuture(1.0f));
        when(scoringService.run(futureBye)).thenReturn(CompletableFuture.completedFuture(0.5f));
        when(scoringService.run(futureNice)).thenReturn(CompletableFuture.completedFuture(0.4f));
        when(scoringService.run(futureBad)).thenReturn(CompletableFuture.completedFuture(0.0f));
        when(scoringService.run(futureNo)).thenReturn(CompletableFuture.completedFuture(0.0f));

        Assert.assertEquals(TestUtils.getOutCSV(), userFlagGenerator.run(TestUtils.getCSV()));

        verify(translationService, times(7)).run(any(String.class));
        verify(scoringService, times(7)).run(any(CompletableFuture.class));
    }

    @Test
    public void testFail() throws CsvValidationException, IOException, ExecutionException, InterruptedException {
        UserFlagGenerator userFlagGenerator = new UserFlagGenerator(scoringService, translationService);

        CompletableFuture<String> futureHello = CompletableFuture.completedFuture("hello");
        CompletableFuture<String> futureBye = CompletableFuture.completedFuture("bye");
        CompletableFuture<String> futureNice = CompletableFuture.completedFuture("nice");
        CompletableFuture<String> futureBad = CompletableFuture.completedFuture("bad");
        CompletableFuture<String> futureNo = CompletableFuture.completedFuture("no");

        when(translationService.run("hello")).thenReturn(futureHello);
        when(translationService.run("bye")).thenReturn(futureBye);
        when(translationService.run("nice")).thenReturn(futureNice);
        when(translationService.run("bad")).thenReturn(futureBad);
        when(translationService.run("no")).thenReturn(futureNo);


        when(scoringService.run(futureHello)).thenReturn(CompletableFuture.completedFuture(1.0f));
        when(scoringService.run(futureBye)).thenReturn(CompletableFuture.completedFuture(0.5f));
        when(scoringService.run(futureNice)).thenReturn(CompletableFuture.completedFuture(0.4f));
        when(scoringService.run(futureBad)).thenReturn(CompletableFuture.completedFuture(0.0f));
        when(scoringService.run(futureNo)).thenReturn(CompletableFuture.completedFuture(0.0f));

        Assert.assertEquals(TestUtils.getOutCSV(), userFlagGenerator.run(TestUtils.getCSV()));

        verify(translationService, times(7)).run(any(String.class));
        verify(scoringService, times(7)).run(any(CompletableFuture.class));
    }


 */
}
