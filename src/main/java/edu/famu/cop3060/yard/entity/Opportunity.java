package edu.famu.cop3060.yard.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opportunity")
public class Opportunity {

    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 32)
    private String type;

    @Column(nullable = false, length = 255)
    private String sponsor;

    @Column(nullable = false, length = 32)
    private String deadline;

    @Column(nullable = false, length = 500)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "opportunity_tag", joinColumns = @JoinColumn(name = "opportunity_id"))
    @Column(name = "tag_value", length = 64)
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false, length = 2048)
    private String url;

    protected Opportunity() {
    }

    public Opportunity(String id, String title, String type, String sponsor, String deadline,
                       String description, List<String> tags, String url) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.sponsor = sponsor;
        this.deadline = deadline;
        this.description = description;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
