package com.project.api.controller;

import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.model.Event;
import com.project.api.model.Note;
import com.project.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping()
    public List<Map<String, Object>>  get() {
        return eventService.getEvents();
    }

    @PostMapping()
    public List<Map<String, Object>> post(@RequestBody Event event) {
        return eventService.save(event);
    }

    @PutMapping()
    public List<Map<String, Object>> put(@RequestBody Event event) {
        return eventService.update(event);
    }

    @PutMapping("/toggle")
    public List<Map<String, Object>> toggle(@RequestBody Event event) {
        event.setComplete(!event.getComplete());
        event.setUser_id(CurrentAuthContext.getUserId());
        return eventService.update(event);
    }

    @PutMapping("/upsert")
    public ResponseEntity<List<Map<String, Object>>> Update(@RequestBody List<Event> events) {
            return new ResponseEntity<>(eventService.upsert(events), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Event delete(@PathVariable UUID id) {
        return eventService.delete(id);
    }

    @DeleteMapping()
    public List<Event> delete(@RequestBody List<Event> events) {
        return events;
    }

}
