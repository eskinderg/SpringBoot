package com.project.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "preference_tbl")
public class Preference {

    @Id
    @Column(name = "pref_id")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private long id;


    @Column(name = "user_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID user_id;

    @Column(name = "dark_mode")
    private boolean dark_mode;

    public void setId(long id) {
        this.id = id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public void setDark_mode(boolean dark_mode) {
        this.dark_mode = dark_mode;
    }

    public long getId() {
        return id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public boolean isDark_mode() {
        return dark_mode;
    }
}
