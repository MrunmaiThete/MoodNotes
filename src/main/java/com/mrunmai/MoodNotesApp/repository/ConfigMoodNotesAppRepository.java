package com.mrunmai.MoodNotesApp.repository;

import com.mrunmai.MoodNotesApp.entity.ConfigMoodNotesAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigMoodNotesAppRepository extends MongoRepository<ConfigMoodNotesAppEntity, ObjectId> {

}
