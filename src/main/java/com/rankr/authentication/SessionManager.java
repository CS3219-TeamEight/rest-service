package com.rankr.authentication;

import com.rankr.entities.Session;
import com.rankr.models.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.sasl.AuthenticationException;
import java.util.UUID;

public class SessionManager {
    private static SessionManager sessionManager = new SessionManager();

    @Autowired private SessionRepository repo;

    private SessionManager() {
    }

    public static SessionManager getManager() {
        return sessionManager;
    }

    public String createSession(String jobID) {
        String apiKey = UUID.randomUUID().toString();
        repo.save(new Session(jobID, apiKey));
        return apiKey;
    }

    public boolean authenticateSession(String apiKey) throws AuthenticationException {
        return repo.findByApiKey(apiKey) != null;
    }
}
