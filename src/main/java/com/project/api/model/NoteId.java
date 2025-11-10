package com.project.api.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class NoteId implements Serializable {
    private UUID note_id;
    private UUID user_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteId notePK)) return false;
        return note_id == notePK.note_id && Objects.equals(user_id, notePK.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(note_id, user_id);
    }

}
