package com.sasken.NotificationEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nisargakn369@gmail.com");

        // --- START OF MODIFICATION FOR MULTIPLE RECIPIENTS ---
        // 1. Split the 'toEmail' string by common delimiters (comma, semicolon, or spaces)
        // 2. Filter out any empty strings that might result from splitting
        // 3. Trim whitespace from each email address
        // 4. Convert the list back to a String array
        String[] recipients = Arrays.stream(toEmail.split("[,;\\s]+")) // Splits by comma, semicolon, OR one or more spaces
                .filter(email -> !email.trim().isEmpty()) // Remove empty strings
                .map(String::trim) // Trim leading/trailing spaces from each email
                .collect(Collectors.toList())
                .toArray(new String[0]); // Convert to array

        if (recipients.length == 0) {
            System.err.println("No valid email recipients found in string: '" + toEmail + "'. Email not sent.");
            return; // Exit if no valid recipients are found
        }

        message.setTo(recipients); // Set multiple recipients using the array
        // --- END OF MODIFICATION FOR MULTIPLE RECIPIENTS ---

        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Mail sent successfully to: " + String.join(", ", recipients)); // Log recipients for confirmation
        } catch (Exception e) {
            System.err.println("Failed to send mail to: " + String.join(", ", recipients) + ". Error: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}