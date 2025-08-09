package com.sasken.NotificationEngine.repository;

import com.sasken.NotificationEngine.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    // Your existing method for search:
    List<Training> findByTitleContainingIgnoreCaseOrTrainerNameContainingIgnoreCaseOrUserEmailContainingIgnoreCase(
            String title, String trainerName, String userEmail);

    // ⭐ NEW: For "Upcoming" trainings (dueDate in the future) ⭐
    List<Training> findByDueDateAfter(LocalDateTime now);

    // ⭐ NEW: For "Ongoing" trainings (dueDate between a start and end, e.g., last 24 hours to now) ⭐
    // This is a flexible method, its usage in controller defines "ongoing"
    List<Training> findByDueDateBetween(LocalDateTime start, LocalDateTime end);

    // ⭐ NEW: For "Overdue" trainings (dueDate in the past AND 30-min notification sent) ⭐
    // This assumes that if the 30-min notification was sent, all pre-notifications were also attempted.
    List<Training> findByDueDateBeforeAndNotifiedThirtyMinutesBeforeTrue(LocalDateTime now);

    // You could also consider adding a custom query if the logic becomes very complex for "Ongoing" or "Overdue"
    // @Query("SELECT t FROM Training t WHERE t.dueDate <= :now AND (NOT t.notifiedThirtyMinutesBefore OR NOT t.notifiedOneHourBefore ...)")
    // List<Training> findOngoingTrainings(@Param("now") LocalDateTime now);
}