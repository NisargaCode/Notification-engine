package com.sasken.NotificationEngine.service;

import com.sasken.NotificationEngine.model.Training;
import com.sasken.NotificationEngine.repository.TrainingRepository;
import com.sasken.NotificationEngine.EmailService; // Ensure this is the correct path to your EmailService
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // ADD THIS IMPORT

@Service
public class NotificationService {

    private final TrainingRepository trainingRepository;
    private final EmailService emailService;

    public NotificationService(TrainingRepository trainingRepository, EmailService emailService) {
        this.trainingRepository = trainingRepository;
        this.emailService = emailService;
    }

    // Runs every minute
    @Scheduled(fixedRate = 60000)
    public void sendNotifications() {
        List<Training> trainings = trainingRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Training training : trainings) {
            LocalDateTime dueDate = training.getDueDate();
            // Ensure dueDate is not null before proceeding
            if (dueDate == null) {
                System.out.println("Warning: Training ID " + training.getId() + " has a null due date. Skipping scheduled notification.");
                continue;
            }

            Duration duration = Duration.between(now, dueDate);

            long minutesLeft = duration.toMinutes();
            long hoursLeft = duration.toHours();
            long daysLeft = duration.toDays();

            String message = "Training: " + training.getTitle() + "\nScheduled on: " + dueDate;

            // Only send if due date is in the future
            if (dueDate.isAfter(now)) {
                if (daysLeft == 2 && !training.isNotifiedTwoDaysBefore()) {
                    emailService.sendSimpleEmail(training.getUserEmail(), "Training Reminder - 2 Days Left", message);
                    training.setNotifiedTwoDaysBefore(true);
                } else if (daysLeft == 1 && !training.isNotifiedOneDayBefore()) {
                    emailService.sendSimpleEmail(training.getUserEmail(), "Training Reminder - 1 Day Left", message);
                    training.setNotifiedOneDayBefore(true);
                } else if (hoursLeft == 1 && !training.isNotifiedOneHourBefore()) {
                    emailService.sendSimpleEmail(training.getUserEmail(), "Training Reminder - 1 Hour Left", message);
                    training.setNotifiedOneHourBefore(true);
                } else if (minutesLeft <= 30 && minutesLeft > 0 && !training.isNotifiedThirtyMinutesBefore()) { // Ensure minutesLeft is still positive
                    emailService.sendSimpleEmail(training.getUserEmail(), "Training Reminder - 30 Minutes Left", message);
                    training.setNotifiedThirtyMinutesBefore(true);
                }
            }


            // Save updated flags
            trainingRepository.save(training);
        }
    }

    // ADD THIS METHOD FOR MANUAL NOTIFICATION SENDING
    public void sendManualNotification(Long trainingId) {
        Optional<Training> optionalTraining = trainingRepository.findById(trainingId);
        if (optionalTraining.isPresent()) {
            Training training = optionalTraining.get();
            String subject = "Manual Notification: Your Training Reminder";
            String body = String.format("Dear %s,\n\nThis is a manual notification for your training: \"%s\" with trainer %s, scheduled for %s.",
                    training.getUserEmail(), training.getTitle(), training.getTrainerName(), training.getDueDate());
            emailService.sendSimpleEmail(training.getUserEmail(), subject, body); // Assuming sendSimpleEmail
            System.out.println("Manual notification sent for Training ID: " + trainingId + " to " + training.getUserEmail());

            // You might want to update a flag or log the manual send here if needed
            // e.g., training.setLastManualSendTime(LocalDateTime.now());
            // trainingRepository.save(training);
        } else {
            throw new RuntimeException("Training not found with ID: " + trainingId);
        }
    }
}