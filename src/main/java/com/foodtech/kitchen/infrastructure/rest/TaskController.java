package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.exceptions.AccessDeniedException;
import com.foodtech.kitchen.application.ports.in.CompleteTaskPreparationPort;
import com.foodtech.kitchen.application.ports.in.GetTasksByStationPort;
import com.foodtech.kitchen.application.ports.in.StartTaskPreparationPort;
import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;
import com.foodtech.kitchen.domain.model.UserRole;
import com.foodtech.kitchen.domain.services.RoleStationMapper;
import com.foodtech.kitchen.infrastructure.rest.dto.TaskResponse;
import com.foodtech.kitchen.infrastructure.rest.mapper.TaskMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final GetTasksByStationPort getTasksByStationPort;
    private final StartTaskPreparationPort startTaskPreparationPort;
    private final CompleteTaskPreparationPort completeTaskPreparationPort;

    public TaskController(GetTasksByStationPort getTasksByStationPort,
                          StartTaskPreparationPort startTaskPreparationPort,
                          CompleteTaskPreparationPort completeTaskPreparationPort) {
        this.getTasksByStationPort = getTasksByStationPort;
        this.startTaskPreparationPort = startTaskPreparationPort;
        this.completeTaskPreparationPort = completeTaskPreparationPort;
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<TaskResponse>> getTasksByStation(
            @PathVariable Station station,
            @RequestParam(required = false) TaskStatus status) {
        UserRole callerRole = extractCallerRole();
        if (callerRole != null && !RoleStationMapper.stationsFor(callerRole).contains(station)) {
            throw new AccessDeniedException(
                "Role " + callerRole + " cannot access tasks at station " + station
            );
        }
        List<Task> tasks = getTasksByStationPort.execute(Set.of(station), status);
        return ResponseEntity.ok(TaskMapper.toResponseList(tasks));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<TaskResponse> startTaskPreparation(@PathVariable Long id) {
        Task task = startTaskPreparationPort.execute(id, null);
        return ResponseEntity.ok(TaskMapper.toResponse(task));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTaskPreparation(@PathVariable Long id) {
        UserRole callerRole = extractCallerRole();
        Task task = completeTaskPreparationPort.execute(id, callerRole);
        return ResponseEntity.ok(TaskMapper.toResponse(task));
    }

    private UserRole extractCallerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> a.startsWith("ROLE_"))
            .findFirst()
            .map(a -> {
                try {
                    return UserRole.valueOf(a.substring(5));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            })
            .orElse(null);
    }
}
