package com.mrunmai.MoodNotesApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_moodnotes_app")
@Data
@NoArgsConstructor
public class ConfigMoodNotesAppEntity {

    private String key;
    private String value;

}
