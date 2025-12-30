package com.project.api.controller;

import com.project.api.model.Preference;
import com.project.api.model.User;
import com.project.api.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preference")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @GetMapping()
    @PreAuthorize("hasRole('Read')")
    public ResponseEntity<List<Map<String, Object>>> get() {
        return new ResponseEntity<>(preferenceService.getUserPreference(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Read')")
    @PutMapping()
    public ResponseEntity<List<Map<String, Object>>> put(@RequestBody List<Preference> preferences) {
        return new ResponseEntity<>(preferenceService.upsert(preferences), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Read')")
    @PutMapping("/update")
    public ResponseEntity<List<Map<String, Object>>> update(@RequestBody List<User> users) {
        return new ResponseEntity<>(preferenceService.upsertUserInfo(users), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Read')")
    @GetMapping("/userinfo")
    public ResponseEntity<List<Map<String, Object>>> userInfo() {
        return new ResponseEntity<>(preferenceService.getUserInfo(), HttpStatus.OK);
    }
}
