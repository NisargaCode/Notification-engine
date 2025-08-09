package com.sasken.NotificationEngine.controller;

import com.sasken.NotificationEngine.model.Training;
import com.sasken.NotificationEngine.repository.TrainingRepository;
import com.sasken.NotificationEngine.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainings")
@CrossOrigin(origins = "http://localhost:3000") // IMPORTANT: Ensure this matches your React app's port
public class TrainingController {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private NotificationService notificationservice;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        System.out.println("[Backend] Received DELETE request for ID: " + id);
        Optional<Training> trainingOptional = trainingRepository.findById(id);
        if (trainingOptional.isPresent()) {
            trainingRepository.deleteById(id);
            System.out.println("[Backend] Training with ID " + id + " deleted successfully.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println("[Backend] Training with ID " + id + " not found for deletion.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ⭐ MODIFIED: Combined filtering logic into one GET endpoint ⭐
    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String viewType) { // ⭐ NEW PARAMETER ⭐

        System.out.println("[Backend] Received GET request for trainings.");
        System.out.println("[Backend] Search Term: " + (searchTerm != null ? searchTerm : "None"));
        System.out.println("[Backend] View Type: " + (viewType != null ? viewType : "Dashboard/All"));

        List<Training> trainings;
        LocalDateTime now = LocalDateTime.now();

        // Step 1: Get base list based on viewType
        if (viewType != null) {
            switch (viewType.toLowerCase()) {
                case "upcoming":
                    // Upcoming: Due date is in the future
                    // Using findByDueDateAfter to get all trainings where dueDate is after now
                    trainings = trainingRepository.findByDueDateAfter(now);
                    System.out.println("[Backend] Fetched " + trainings.size() + " upcoming trainings.");
                    break;
                case "ongoing":
                    // ⭐ IMPORTANT: Define "Ongoing" clearly for your project ⭐
                    // Example "Ongoing" logic: Due date is within the last 24 hours AND not all notifications sent.
                    // This is just an example; you might need to adjust this definition.
                    // If you have a 'status' field in your Training model, use that.
                    LocalDateTime twentyFourHoursAgo = now.minusHours(24);
                    trainings = trainingRepository.findByDueDateBetween(twentyFourHoursAgo, now);
                    // Further refine if you only want truly "ongoing" where notifications are still relevant
                    // For example, filter out if all notifications have already been sent and no longer relevant
                    trainings = trainings.stream().filter(t ->
                                    !t.isNotifiedThirtyMinutesBefore() || // If the last notif hasn't gone, it might be ongoing
                                            (t.getDueDate().isAfter(twentyFourHoursAgo) && t.getDueDate().isBefore(now)) // Due within the last 24h
                            // You might need a more sophisticated 'status' property in Training.java to define ongoing
                            // For demo, this will fetch things recently due.
                    ).toList(); // Use .toList() for Java 16+ or .collect(Collectors.toList())
                    System.out.println("[Backend] Fetched " + trainings.size() + " ongoing trainings.");
                    break;
                case "overdue":
                    // ⭐ IMPORTANT: Define "Overdue" clearly for your project ⭐
                    // Example "Overdue" logic: Due date is in the past, and it's considered 'missed' or far past due.
                    // A simple definition is: Due date is in the past AND the 30-min notification was sent (meaning all attempts are done).
                    // Or, due date is sufficiently in the past (e.g., more than 24 hours ago).
                    trainings = trainingRepository.findByDueDateBeforeAndNotifiedThirtyMinutesBeforeTrue(now);
                    // Alternatively, if 'notificationStatus' is explicitly tracked in DB or implied:
                    // trainings = trainingRepository.findByStatus("MISSED");
                    System.out.println("[Backend] Fetched " + trainings.size() + " overdue trainings.");
                    break;
                case "dashboard": // "dashboard" implicitly means all trainings
                default:
                    trainings = trainingRepository.findAll();
                    System.out.println("[Backend] Fetched " + trainings.size() + " all trainings (dashboard view).");
                    break;
            }
        } else {
            // If viewType is null (i.e., not provided, default dashboard view)
            trainings = trainingRepository.findAll();
            System.out.println("[Backend] Fetched " + trainings.size() + " all trainings (default view).");
        }


        // Step 2: Apply searchTerm filter to the result from Step 1
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            trainings = trainings.stream()
                    .filter(training ->
                            training.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    training.getTrainerName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    training.getUserEmail().toLowerCase().contains(searchTerm.toLowerCase())
                    )
                    .toList(); // Use .toList() for Java 16+ or .collect(Collectors.toList())
            System.out.println("[Backend] Applied search term, new count: " + trainings.size());
        }

        System.out.println("[Backend] Returning " + trainings.size() + " trainings for GET request.");
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    // Removed the separate @GetMapping("/upcoming") as its logic is now merged above
    /*
    @GetMapping("/upcoming")
    public ResponseEntity<List<Training>> getUpcomingTrainings() {
        System.out.println("[Backend] Received GET request for upcoming trainings.");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(2); // Trainings due in the next 2 days
        List<Training> upcomingTrainings = trainingRepository.findByDueDateBetween(now, limit);
        System.out.println("[Backend] Returning " + upcomingTrainings.size() + " upcoming trainings.");
        return new ResponseEntity<>(upcomingTrainings, HttpStatus.OK); // 200 OK
    }
    */


    @PostMapping
    public ResponseEntity<Training> addTraining(@RequestBody Training training) {
        System.out.println("[Backend] Received POST request to add training: " + training);
        Training savedTraining = trainingRepository.save(training);
        System.out.println("[Backend] Training added successfully: " + savedTraining);
        return new ResponseEntity<>(savedTraining, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long id, @RequestBody Training updatedTraining) {
        System.out.println("[Backend] Received PUT request for ID: " + id);
        System.out.println("[Backend] Payload received for update: " + updatedTraining);
        return trainingRepository.findById(id)
                .map(existingTraining -> {
                    System.out.println("[Backend] Found existing training: " + existingTraining);

                    if (updatedTraining.getDueDate() != null && !updatedTraining.getDueDate().equals(existingTraining.getDueDate())) {
                        System.out.println("[Backend] Updating dueDate from " + existingTraining.getDueDate() + " to " + updatedTraining.getDueDate());
                        existingTraining.setDueDate(updatedTraining.getDueDate());
                    } else {
                        System.out.println("[Backend] DueDate not changed or was null in payload.");
                    }

                    System.out.println("[Backend] Resetting notification flags.");
                    existingTraining.setNotifiedTwoDaysBefore(false);
                    existingTraining.setNotifiedOneDayBefore(false);
                    existingTraining.setNotifiedOneHourBefore(false);
                    existingTraining.setNotifiedThirtyMinutesBefore(false);

                    if (updatedTraining.getTitle() != null && !updatedTraining.getTitle().isEmpty()) {
                        existingTraining.setTitle(updatedTraining.getTitle());
                    }
                    if (updatedTraining.getTrainerName() != null && !updatedTraining.getTrainerName().isEmpty()) {
                        existingTraining.setTrainerName(updatedTraining.getTrainerName());
                    }
                    if (updatedTraining.getUserEmail() != null && !updatedTraining.getUserEmail().isEmpty()) {
                        existingTraining.setUserEmail(updatedTraining.getUserEmail());
                    }

                    System.out.println("[Backend] Training object before saving: " + existingTraining);

                    Training savedTraining = trainingRepository.save(existingTraining);
                    System.out.println("[Backend] Training saved successfully: " + savedTraining);
                    return new ResponseEntity<>(savedTraining, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    System.out.println("[Backend] Training with ID " + id + " not found for update.");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PostMapping("/{id}/send-notification")
    public ResponseEntity<String> sendManualNotification(@PathVariable Long id) {
        try {
            System.out.println("[Backend] Received POST request to send manual notification for Training ID: " + id);
            this.notificationservice.sendManualNotification(id);
            System.out.println("[Backend] Manual notification process initiated successfully for Training ID: " + id);
            return ResponseEntity.ok("Notification sent successfully for training ID: " + id);
        } catch (RuntimeException e) {
            System.err.println("[Backend] Error sending manual notification for Training ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training not found: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Backend] Unexpected error sending manual notification for Training ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification: " + e.getMessage());
        }
    }
}