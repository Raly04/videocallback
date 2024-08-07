package com.nyaina.videocall.services;

import com.nyaina.videocall.models.Group;
import com.nyaina.videocall.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public Group save(Group group) {
        return groupRepository.save(group);
    }
}
