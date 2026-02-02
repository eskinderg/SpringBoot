package com.project.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "event_tbl")
public class Event {

    @Id
    @Column(name = "event_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID event_id;
    @Column(name = "user_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID user_id;
    @Column(name = "title")
    private String title;
    @Column(name = "complete")
    private boolean complete;

    public UUID getEvent_id() {
       return this.event_id;
    }

    public void setNote_id(UUID event_id) {
       this.event_id = event_id;
    }

    public UUID getUser_id() {
        return this.user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean getComplete() {
        return this.complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
