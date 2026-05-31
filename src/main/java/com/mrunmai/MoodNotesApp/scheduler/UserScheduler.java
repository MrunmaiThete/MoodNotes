package com.mrunmai.MoodNotesApp.scheduler;

import com.mrunmai.MoodNotesApp.entity.MoodNotesEntry;
import com.mrunmai.MoodNotesApp.entity.User;
import com.mrunmai.MoodNotesApp.enums.Sentiment;
import com.mrunmai.MoodNotesApp.repository.UserRepository;
import com.mrunmai.MoodNotesApp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 9 * * SUN")  // Every Sunday at 9 AM
    public void sendWeeklySentimentReport() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            try {
                List<MoodNotesEntry> moodNotesEntries = user.getMoodNotesEntries();
                if (moodNotesEntries == null || moodNotesEntries.isEmpty()) continue;

                List<Sentiment> sentiments = moodNotesEntries.stream()
                        .filter(x -> x.getDate() != null &&
                                x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                        .map(MoodNotesEntry::getSentiment)
                        .filter(s -> s != null)
                        .collect(Collectors.toList());

                if (sentiments.isEmpty()) continue;

                Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
                for (Sentiment sentiment : sentiments) {
                    sentimentCounts.put(sentiment,
                            sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }

                Sentiment mostFrequentSentiment = sentimentCounts.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .get()
                        .getKey();

                emailService.sendEmail(
                        user.getEmail(),
                        "Your Weekly Mood Summary - MoodNotes",
                        "Hi " + user.getUserName() + ",\n\n" +
                                "Your most frequent sentiment this week: " + mostFrequentSentiment + "\n\n" +
                                "Keep journaling! 📓\n- MoodNotes Team"
                );

            } catch (Exception e) {
                log.error("Error sending weekly sentiment for user: {}", user.getUserName(), e);
            }
        }
    }
}