package models;

public class Job {
    private final String descriptionPath;

    public Job(String descriptionPath) {
        this.descriptionPath = descriptionPath;
    }

    public String getPath() {
        return descriptionPath;
    }
}
