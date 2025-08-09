package com.sasken.NotificationEngine.config;

import com.sasken.NotificationEngine.model.Training;
import com.sasken.NotificationEngine.repository.TrainingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final TrainingRepository trainingRepository;

    public DataLoader(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        trainingRepository.save(new Training("Java Basics", "nisargakn726@gmail.com", LocalDateTime.now().plusMinutes(1),"Trainer A"));
        trainingRepository.save(new Training("Spring Boot", "nisanan@gmail.com", LocalDateTime.now().plusHours(2),"Trainer B"));
        trainingRepository.save(new Training("DevOps", "nisargakn726@gmail.com", LocalDateTime.now().plusDays(1),"Trainer c"));
    }
}