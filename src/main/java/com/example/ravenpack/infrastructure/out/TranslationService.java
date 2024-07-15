package com.example.ravenpack.infrastructure.out;

import com.example.ravenpack.application.port.out.TranslationServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TranslationService implements TranslationServicePort {

    Logger logger = LoggerFactory.getLogger(TranslationService.class);
    public String run(String in) {

        logger.info("Run starts: " + Thread.currentThread().getName() + " for: " + in);

        /*
        WebClient webClient = WebClient.builder()
            .baseUrl("https://api.raven.com")
            ...
        */
        try {
            Thread.sleep(200);
        }catch (InterruptedException e){
            throw new RuntimeException(e.getMessage());
        }
        return "some translated word";
    }
}
