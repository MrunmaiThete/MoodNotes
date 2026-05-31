package com.mrunmai.MoodNotesApp.repository;

import com.mrunmai.MoodNotesApp.entity.MoodNotesEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MoodNotesEntryRepository extends MongoRepository<MoodNotesEntry, ObjectId> {

}
