package com.mrunmai.MoodNotesApp.entity;

import lombok.*;
import com.mrunmai.MoodNotesApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "moodnotes_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodNotesEntry {

    @Id
    private ObjectId id;

    private String title;

    private String content;

    private LocalDateTime date;

    private Sentiment sentiment;
}