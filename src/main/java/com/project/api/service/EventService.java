package com.project.api.service;

import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.services.StoredProcedureService;
import com.project.api.core.utils.EventFactory;
import com.project.api.core.utils.JsonHelper;
import com.project.api.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private StoredProcedureService spService;

    public List<Map<String, Object>> save(Event event) {
        return this.upsert(List.of(event));
    }

    @Transactional
    public List<Map<String, Object>> getEvents() {
        Map<String, Object> params = Map.of("p_user_id", CurrentAuthContext.getUserId().toString());
        return spService.callProcedureForList("getUserEvents", params);
    }

    public List<Map<String, Object>> upsert(List<Event> events) {
        String eventsJson = JsonHelper.convertToJson(events.stream().map(EventFactory::create).toList());
        Map<String, Object> params = Map.of(
                "p_user_id", CurrentAuthContext.getUserId().toString(),
                "events_json", eventsJson);
        return spService.callProcedureForList("event_bulk_upsert", params);
    }

    public List<Map<String, Object>> update(Event event) {
        return this.upsert(List.of(event));
    }

    public Event delete(UUID id) {
        return new Event();
    }
}
