package com.mrunmai.MoodNotesApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.mrunmai.MoodNotesApp.entity.MoodNotesEntry;
import com.mrunmai.MoodNotesApp.entity.User;
import com.mrunmai.MoodNotesApp.service.MoodNotesEntryService;
import com.mrunmai.MoodNotesApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/moodnotes")
@Tag(name = "MoodNotes APIs")
@Slf4j
public class MoodNotesEntryController {

    @Autowired
    private MoodNotesEntryService moodNotesEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all entries of a user")
    public ResponseEntity<?> getAllMoodNotesEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<MoodNotesEntry> all = user.getMoodNotesEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(summary = "Create a new MoodNotes entry")
    public ResponseEntity<MoodNotesEntry> createEntry(@RequestBody MoodNotesEntry myEntry) {
        try {
            log.info("Creating entry for user, title: {}", myEntry.getTitle());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Authenticated user: {}", userName);
            moodNotesEntryService.saveEntry(myEntry, userName);
            log.info("Entry created successfully for user: {}", userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating entry: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    @Operation(summary = "Get a MoodNotes entry by ID")
    public ResponseEntity<?> getMoodNoteEntryById(@PathVariable String myId) {
        ObjectId objectId = new ObjectId(myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<MoodNotesEntry> collect = user.getMoodNotesEntries().stream()
                .filter(x -> x.getId().equals(objectId))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<MoodNotesEntry> moodNotesEntry = moodNotesEntryService.findById(objectId);
            if (moodNotesEntry.isPresent()) {
                return new ResponseEntity<>(moodNotesEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    @Operation(summary = "Delete a MoodNotes entry by ID")
    public ResponseEntity<?> deleteMoodNoteEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = moodNotesEntryService.deleteById(myId, username);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    @Operation(summary = "Update a MoodNotes entry by ID")
    public ResponseEntity<?> updateMoodNoteById(@PathVariable ObjectId myId,
                                                @RequestBody MoodNotesEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<MoodNotesEntry> collect = user.getMoodNotesEntries().stream()
                .filter(x -> x.getId().equals(myId))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<MoodNotesEntry> moodNotesEntry = moodNotesEntryService.findById(myId);
            if (moodNotesEntry.isPresent()) {
                MoodNotesEntry old = moodNotesEntry.get();
                old.setTitle(newEntry.getTitle() != null &&
                        !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null &&
                        !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                moodNotesEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}