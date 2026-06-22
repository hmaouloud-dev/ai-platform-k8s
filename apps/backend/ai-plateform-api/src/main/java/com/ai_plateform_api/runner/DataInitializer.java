package com.ai_plateform_api.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("Application started - DataInitializer running...");
        // Seed initial data here
        log.info("DataInitializer completed.");
    }
}