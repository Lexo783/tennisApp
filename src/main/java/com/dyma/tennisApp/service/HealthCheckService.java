package com.dyma.tennisApp.service;

import com.dyma.tennisApp.ApplicationStatus;
import com.dyma.tennisApp.HealthCheck;
import com.dyma.tennisApp.repository.HealthCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    @Autowired
    private HealthCheckRepository healthCheckRepository;

    public HealthCheck healthCheck() {
        Long activeSession = healthCheckRepository.countApplicationConnections();

        if (activeSession > 0) {
            return new HealthCheck(ApplicationStatus.OK, "Bienvenue to my first spring application");
        } else {
            return new HealthCheck(ApplicationStatus.KO, "Aille pas de connexion active");
        }
    }
}
