package com.example.ravenpack.application.adapter;

import com.example.ravenpack.application.port.in.UserFlagGeneratorPort;
import com.example.ravenpack.application.port.out.AsyncScoringServicePort;
import com.example.ravenpack.infrastructure.out.AsyncScoringService;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserFlagGenerator implements UserFlagGeneratorPort {

    Logger logger = LoggerFactory.getLogger(UserFlagGenerator.class);

    private AsyncScoringServicePort asyncScoringService;

    private CSVParser csvParser;

    public UserFlagGenerator(AsyncScoringServicePort asyncScoringService) {
        this.asyncScoringService = asyncScoringService;

        csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
    }

    public String run(String csv) {
        logger.info("Run starts");
        Map<String, List<Float>> map = this.processCSV(csv);
        return this.outputCSV(map);
    }


    private Map<String, List<Float>> processCSV(String csv) {
        logger.info("ProcessCSV starts");
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(csv))
                .withSkipLines(1)
                .withCSVParser(csvParser)
                .build();
        Map<String, List<CompletableFuture<Float>>> futures = new ConcurrentHashMap<>();

        try {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                String userId = nextLine[0];
                String message = nextLine[1];
                validate(userId, message);
                if (!futures.containsKey(userId)) {
                    futures.put(userId, new ArrayList<>());
                }
                futures.get(userId).add(asyncScoringService.run(message));
            }
        } catch(IOException | CsvValidationException e){
            throw new RuntimeException(e.getMessage());
        }

        Map<String, List<Float>> map = new HashMap<>();
        futures.keySet().forEach(userId -> {
            map.put(userId, futures.get(userId).stream().map(scores->scores.join()).collect(Collectors.toList()));
        });
        return map;
    }

    private String outputCSV(Map<String, List<Float>> map){
        logger.info("ProcessCSV starts");
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter writer = new CSVWriter(stringWriter)) {
            writer.writeNext(new String[]{"user_id", "total_messages", "avg_score"});
            map.entrySet().stream().forEach(e -> {
                writer.writeNext(new String[]{e.getKey(), String.valueOf(e.getValue().size()), String.valueOf((float) e.getValue().stream().mapToDouble(Float::doubleValue).average().getAsDouble())});
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error writing CSV to String", e);
        }
        return stringWriter.toString();
    }

    private void validate(String userId, String message){
        if(userId.isEmpty() || message.isEmpty()) {
            logger.error("Bad csv format");
            throw new RuntimeException("Bad csv format");
        }
    }
}
