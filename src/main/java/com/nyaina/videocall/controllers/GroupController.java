package com.nyaina.videocall.controllers;

import com.nyaina.videocall.models.Group;
import com.nyaina.videocall.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<?> save (@RequestBody Group group, UriComponentsBuilder builder) {
        try{
            var savedGroup = groupService.save(group);
            URI location = builder
                    .path("/group/{id}")
                    .buildAndExpand(savedGroup.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedGroup);
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
