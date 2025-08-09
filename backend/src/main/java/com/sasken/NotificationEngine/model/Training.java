package com.sasken.NotificationEngine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column; // Import this for @Column annotation

import java.time.LocalDateTime;

// REMOVE Lombok annotations if you are manually providing getters/setters
// If you *want* to use Lombok, ensure it's set up correctly in your IDE and pom.xml,
// and then you can remove all manual getters/setters and rely on @Data, @NoArgsConstructor, @AllArgsConstructor.
// For now, I'll assume you prefer manual for clarity, or if Lombok isn't working.

@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String userEmail;
    private LocalDateTime dueDate;
    private String trainerName;

    // ⭐ CRITICAL FIX: Rename these fields to match what JPA expects from method names ⭐
    @Column(name = "notified_two_days_before") // Add @Column for clear DB column names
    private boolean notifiedTwoDaysBefore = false; // Renamed from notified2DaysBefore

    @Column(name = "notified_one_day_before")
    private boolean notifiedOneDayBefore = false; // Renamed from notified1DayBefore

    @Column(name = "notified_one_hour_before")
    private boolean notifiedOneHourBefore = false; // Renamed from notified1HourBefore

    @Column(name = "notified_thirty_minutes_before")
    private boolean notifiedThirtyMinutesBefore = false; // Renamed from notified30MinutesBefore

    // Default constructor required for JPA
    public Training() {}

    // Custom constructor
    // Update constructor if you use it to initialize these new fields
    public Training(String title, String userEmail, LocalDateTime dueDate, String trainerName) {
        this.title = title;
        this.userEmail = userEmail;
        this.dueDate = dueDate;
        this.trainerName = trainerName;
        // The boolean fields are already initialized to false by default, no need to add here
    }

    // Getters and Setters (ensure these match the NEW field names)
    public Long getId() { return id; }
    // No setter for ID if it's auto-generated

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    // ⭐ Ensure these getters/setters match the NEW field names exactly ⭐
    public boolean isNotifiedTwoDaysBefore() { return notifiedTwoDaysBefore; } // Matches new field name
    public void setNotifiedTwoDaysBefore(boolean notifiedTwoDaysBefore) { this.notifiedTwoDaysBefore = notifiedTwoDaysBefore; }

    public boolean isNotifiedOneDayBefore() { return notifiedOneDayBefore; } // Matches new field name
    public void setNotifiedOneDayBefore(boolean notifiedOneDayBefore) { this.notifiedOneDayBefore = notifiedOneDayBefore; }

    public boolean isNotifiedOneHourBefore() { return notifiedOneHourBefore; } // Matches new field name
    public void setNotifiedOneHourBefore(boolean notifiedOneHourBefore) { this.notifiedOneHourBefore = notifiedOneHourBefore; }

    public boolean isNotifiedThirtyMinutesBefore() { return notifiedThirtyMinutesBefore; } // Matches new field name
    public void setNotifiedThirtyMinutesBefore(boolean notifiedThirtyMinutesBefore) { this.notifiedThirtyMinutesBefore = notifiedThirtyMinutesBefore; }

    public String getTrainerName(){
        return trainerName;
    }
    public void setTrainerName(String trainerName){
        this.trainerName=trainerName;
    }

    // You might also want to add a toString() method for better logging, especially if not using Lombok
    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", trainerName='" + trainerName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", dueDate=" + dueDate +
                ", notifiedTwoDaysBefore=" + notifiedTwoDaysBefore +
                ", notifiedOneDayBefore=" + notifiedOneDayBefore +
                ", notifiedOneHourBefore=" + notifiedOneHourBefore +
                ", notifiedThirtyMinutesBefore=" + notifiedThirtyMinutesBefore +
                '}';
    }
}