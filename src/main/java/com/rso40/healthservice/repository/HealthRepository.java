package com.rso40.healthservice.repository;

import com.rso40.healthservice.model.HealthRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HealthRepository extends  MongoRepository<HealthRepo, String> {
}
