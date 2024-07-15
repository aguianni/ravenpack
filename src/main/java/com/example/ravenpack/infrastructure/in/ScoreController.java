package com.example.ravenpack.infrastructure.in;

import com.example.ravenpack.application.adapter.UserFlagGenerator;
import com.example.ravenpack.infrastructure.out.AsyncScoringService;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("score")
public class ScoreController {

    Logger logger = LoggerFactory.getLogger(ScoreController.class);

    private UserFlagGenerator userFlagGenerator;

    public ScoreController(UserFlagGenerator userFlagGenerator) {
        this.userFlagGenerator = userFlagGenerator;
    }

    @PostMapping
    public ResponseEntity<String> generateFlags(@RequestBody String csv) throws CsvValidationException, IOException, ExecutionException, InterruptedException {
        logger.info("GenerateFlags starts");
        String output = userFlagGenerator.run(csv);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
