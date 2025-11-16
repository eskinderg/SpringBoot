package com.project.api.model;

import com.project.api.core.DatabaseGeneratedValue;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "note_tbl")
@IdClass(NoteId.class)
public class Note {

    @Id
    @Column(name = "note_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID note_id;

    @Id
    @Column(name = "user_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID user_id;

    @Column(name = "header")
    private String header;

    @Column(name = "colour")
    private String colour;
    @Column(name = "selection")
    private String selection;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "favorite")
    private boolean favorite;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "spell_check")
    private boolean spell_check;

    @DatabaseGeneratedValue
    @Column(name = "pin_order", nullable = false)
    private Timestamp pin_order;

    @CreationTimestamp
    @Column(name = "date_created", insertable = false, nullable = false)
    private Timestamp date_created = new Timestamp(new Date().getTime());

    @DatabaseGeneratedValue
    @Column(name = "date_modified", nullable = false)
    private Timestamp date_modified = new Timestamp(new Date().getTime());

    @DatabaseGeneratedValue
    @Column(name = "date_archived", nullable = false)
    private Timestamp date_archived = new Timestamp(new Date().getTime());

    @DatabaseGeneratedValue
    @Column(name = "date_deleted", nullable = false)
    private Timestamp date_deleted = new Timestamp(new Date().getTime());

    @DatabaseGeneratedValue
    @Column(name = "last_modified_date", nullable = false)
    private Timestamp last_modified_date = new Timestamp(new Date().getTime());

    @Column(name = "owner")
    private String owner;

    @Column(name = "text", length = 16777215, columnDefinition = "mediumtext")
    private String text;

    public UUID getNote_id() {
        return note_id;
    }

    public void setNote_id(UUID note_id) {
        this.note_id = note_id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSpell_check() {
        return spell_check;
    }

    public void setSpell_check(boolean spell_check) {
        this.spell_check = spell_check;
    }

    public Timestamp getPin_order() {
        return pin_order;
    }

    public void setPin_order(Timestamp pin_order) {
        this.pin_order = pin_order;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public Timestamp getDate_deleted() {
        return date_deleted;
    }

    public void setDate_deleted(Timestamp date_deleted) {
        this.date_deleted = date_deleted;
    }

    public Timestamp getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(Timestamp last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public Timestamp getDate_archived() {
        return date_archived;
    }

    public void setDate_archived(Timestamp date_archived) {
        this.date_archived = date_archived;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
