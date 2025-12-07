package com.project.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_info")
public class User {

    @Id
    @Column(name = "user_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID user_id;

    @Column(name = "owner")
    private String owner;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, nullable = false)
    private Timestamp createdAt = new Timestamp(new Date().getTime());

    public UUID getUser_id() {
        return this.user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
