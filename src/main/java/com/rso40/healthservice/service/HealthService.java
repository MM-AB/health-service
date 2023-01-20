package com.rso40.healthservice.service;


import com.rso40.healthservice.model.HealthRepo;
import com.rso40.healthservice.repository.HealthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HealthService {

    @Autowired
    private HealthRepository healthRepository;

    Object target;
    Logger logger = LoggerFactory.getLogger(HealthService.class);

    @Async
    public void saveHealth(HealthRepo healthRepo){
        long start = System.currentTimeMillis();
        logger.info("Current thread: "+Thread.currentThread().getName());
        healthRepository.save(healthRepo);
        long end = System.currentTimeMillis();
        logger.info("Total time {}"+ (end-start));
    }

    public List<HealthRepo> getAllHealthLogs() {
        //logger.info("Getting logs: "+Thread.currentThread().getName());
        List<HealthRepo> healthRepo = healthRepository.findAll();

        return healthRepo;
    }

}
