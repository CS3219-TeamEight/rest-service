package com.rankr.models;

import com.rankr.entities.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {
    Session findByApiKey(String apiKey);
}
