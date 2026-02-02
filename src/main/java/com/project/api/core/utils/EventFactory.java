package com.project.api.core.utils;

import com.project.api.auth.CurrentAuthContext;
import com.project.api.model.Event;

public class EventFactory {
    public static Event create(Event input) {
        input.setUser_id(CurrentAuthContext.getUserId());
//        input.setOwner(CurrentAuthContext.getName());
        return input;
    }
}
