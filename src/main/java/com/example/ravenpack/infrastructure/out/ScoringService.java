package com.example.ravenpack.infrastructure.out;

import com.example.ravenpack.application.port.out.ScoringServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScoringService implements ScoringServicePort {

    Logger logger = LoggerFactory.getLogger(ScoringService.class);

    public Float run(String in) {

        logger.info("Run starts: " + Thread.currentThread().getName() + " for: " + in);
        /*
        WebClient webClient = WebClient.builder()
            .baseUrl("https://api.raven.com")
            ...
        */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e){
            throw new RuntimeException(e.getMessage());
        }
        Random rd = new Random(); // creating Random object
        return rd.nextFloat();
    }
}
