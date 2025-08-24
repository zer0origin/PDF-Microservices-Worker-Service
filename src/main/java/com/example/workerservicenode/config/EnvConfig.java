package com.example.workerservicenode.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import util.Env;

@Service
public class EnvConfig {
    public static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    public static final int PREFETCH_COUNT = Env.getEnvOrDefault("PREFETCH_COUNT", Integer::parseInt, 10);
    public static final int CONSUMER_THREAD_COUNT = Env.getEnvOrDefault("CONSUMER_THREAD_COUNT", Integer::parseInt, 5);
    public static final int CONSUMER_THREAD_COUNT_MAX = Env.getEnvOrDefault("CONSUMER_THREAD_COUNT_MAX", Integer::parseInt, 10);
    public static int STEP_SIZE_FOR_EXTRACTION = Env.getEnvOrDefault("STEP_SIZE_FOR_EXTRACTION", Integer::parseInt, 5); //PROBABLY ALLOW THE INCOMING REQUEST TO OVERRIDE THIS FOR SAID REQUEST.
    public static int USE_X_FOR_LINES = Env.getEnvOrDefault("USE_X_FOR_LINES", Integer::parseInt, 0); //Defaults to off.

    static{
        logger.info("Initialising Configuration...");
        logger.info("PREFETCH_COUNT: " + PREFETCH_COUNT);
        logger.info("CONSUMER_THREAD_COUNT: " + CONSUMER_THREAD_COUNT);
        logger.info("CONSUMER_THREAD_COUNT_MAX: " + CONSUMER_THREAD_COUNT_MAX);
        logger.info("STEP_SIZE_FOR_EXTRACTION: " + STEP_SIZE_FOR_EXTRACTION);
        logger.info("USE_X_FOR_LINES: " + USE_X_FOR_LINES);
    }
}
