package com.project.api.core.utils;

import com.project.api.auth.CurrentAuthContext;
import com.project.api.model.Note;

public class NoteFactory {
    public static Note create(Note input) {
        input.setUser_id(CurrentAuthContext.getUserId());
        input.setOwner(CurrentAuthContext.getName());
        return input;
    }
}
