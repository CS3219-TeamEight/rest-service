package com.rankr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document public class Job {
    /* Privacy settings for each job */
    public static final int VISIBILITY_PUBLIC = 0;
    public static final int VISIBILITY_PRIVATE = 1;

    @Id private String id;
    private String description;
    private String descriptionPath;
    private String title;
    @JsonIgnore private String password;
    @JsonIgnore private String salt;
    private int visibility;

    @PersistenceConstructor
    public Job(String description, String descriptionPath, String title, String password,
        String salt, int visibility) {
        this.description = description;
        this.descriptionPath = descriptionPath;
        this.title = title;
        this.password = password;
        this.salt = salt;
        assert (visibility == VISIBILITY_PRIVATE) || (visibility == VISIBILITY_PUBLIC);
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionPath() {
        return descriptionPath;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getSalt() {
        return salt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
