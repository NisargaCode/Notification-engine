// src/main/java/com/sasken/NotificationEngine/EmailService.java

package com.sasken.NotificationEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sasken.NotificationEngine.model.Training; // Import Training model
import java.time.format.DateTimeFormatter; // For formatting LocalDateTime
import java.time.LocalDateTime; // Make sure LocalDateTime is imported

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        // IMPORTANT: Configure your sending email in application.properties/yml, not hardcode here for production
        message.setFrom("your-configured-email@example.com"); // <-- Use the email configured in application.properties
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Mail sent successfully to: " + toEmail + " with subject: " + subject);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
            // Log the exception properly in a real application
        }
    }

    /**
     * Sends a notification email for a specific training.
     * @param training The training object.
     * @param type The type of notification (e.g., "Initial", "1 Hour Reminder", "2 Days Reminder").
     */
    public void sendTrainingNotification(Training training, String type) {
        String subject = String.format("Training Alert: %s - %s", type, training.getTitle());

        // Format LocalDateTime to a user-friendly string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' hh:mm a");
        String formattedDueDate = training.getDueDate().format(formatter);

        String body = String.format(
                "Dear User,\n\n" +
                        "This is a %s for your upcoming training:\n\n" +
                        "Training Title: %s\n" +
                        "Scheduled For: %s\n\n" +
                        "Please ensure you are prepared.\n\n" +
                        "Thank you,\n" +
                        "Sasken Notification Engine Team",
                type, training.getTitle(), formattedDueDate
        );

        sendSimpleEmail(training.getUserEmail(), subject, body);
    }
}