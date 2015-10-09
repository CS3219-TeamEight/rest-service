package entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document public class Resume {
    @Id private String id;
    private String resumePath;
    private double score;

    public Resume(String resumePath, double score) {
        this.resumePath = resumePath;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getResumePath() {
        return resumePath;
    }

    public double getScore() {
        return score;
    }
}
