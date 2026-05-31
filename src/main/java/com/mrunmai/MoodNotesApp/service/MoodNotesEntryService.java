package com.mrunmai.MoodNotesApp.service;

import lombok.extern.slf4j.Slf4j;
import com.mrunmai.MoodNotesApp.entity.MoodNotesEntry;
import com.mrunmai.MoodNotesApp.entity.User;
import com.mrunmai.MoodNotesApp.repository.MoodNotesEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MoodNotesEntryService {

    @Autowired
    private MoodNotesEntryRepository moodNotesEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(MoodNotesEntry moodNotesEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            moodNotesEntry.setDate(LocalDateTime.now());
            MoodNotesEntry saved = moodNotesEntryRepository.save(moodNotesEntry);
            user.getMoodNotesEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error saving entry: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }

    public void saveEntry(MoodNotesEntry moodNotesEntry) {
        moodNotesEntryRepository.save(moodNotesEntry);
    }

    public List<MoodNotesEntry> getAll() {
        return moodNotesEntryRepository.findAll();
    }

    public Optional<MoodNotesEntry> findById(ObjectId id) {
        return moodNotesEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getMoodNotesEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                moodNotesEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("Error deleting entry: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }
}