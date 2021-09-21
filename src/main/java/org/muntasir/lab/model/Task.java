package org.muntasir.lab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Objects;

public class Task {

    private long id;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date targetDate;

    private boolean completed;

    public Task() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (completed != task.completed) return false;
        if (!Objects.equals(description, task.description)) return false;
        return Objects.equals(targetDate, task.targetDate);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (targetDate != null ? targetDate.hashCode() : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }

    public Task(String description, Date targetDate) {
        this.description = description;
        this.targetDate = targetDate;
    }

    public Task(long id, String description, Date targetDate) {
        this.id = id;
        this.description = description;
        this.targetDate = targetDate;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public Date getTargetDate() {
        return targetDate;
    }

    @JsonProperty
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    @JsonProperty
    public boolean isCompleted() {
        return completed;
    }

    @JsonProperty
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
