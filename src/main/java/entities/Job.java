package entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document public class Job {
    /* Privacy settings for each job */
    public static final int VISIBILITY_PUBLIC = 0;
    public static final int VISIBILITY_PRIVATE = 1;

    @Id private String id;
    private String description;
    private String descriptionPath;
    private String title;
    private String auth;
    private String password;
    private int visibility;
    private List<Resume> resumes;

    @PersistenceConstructor
    public Job(String description, String descriptionPath, String title, String auth,
        String password, int visibility, List<Resume> resumes) {
        this.description = description;
        this.descriptionPath = descriptionPath;
        this.title = title;
        this.auth = auth;
        this.password = password;
        this.visibility = visibility;
        this.resumes = resumes;
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

    public String getAuth() {
        return auth;
    }

    public String getPassword() {
        return password;
    }

    public int getVisibility() {
        return visibility;
    }

    public List<Resume> getResumes() {
        return resumes;
    }
}
