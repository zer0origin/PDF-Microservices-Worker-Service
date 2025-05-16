package com.example.workerservicenode;

import org.springframework.boot.SpringApplication;

public class TestWorkerServiceNodeApplication {

    public static void main(String[] args) {
        SpringApplication.from(WorkerServiceNodeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
