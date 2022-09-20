package com.example.demo.controllers;

import com.example.demo.entities.Task;
import com.example.demo.entities.User;
import com.example.demo.services.TaskService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("TasksController")
public class TaskController {
    @Autowired
    private final TaskService taskService;
    @Autowired
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return this.taskService.findAll();
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> appendTask(@RequestBody Map<Object, Object> data) {
        Task task = new Task();
        Optional<User> taskUser = userService.findById(Long.valueOf((Integer) data.get("user")));
        task.setTitle((String) data.get("title"));
        task.setDone((Boolean) data.get("done"));
        task.setDueDate(LocalDate.parse((CharSequence) data.get("dueDate")));
        try {
            task.setUser(taskUser.get());
            return new ResponseEntity<>(this.taskService.append(task), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Task.class, (String) key);
                field.setAccessible(true);
                if (key == "user") {
                    Optional<User> taskUser = userService.findById(Long.valueOf((Integer) fields.get("user")));
                    ReflectionUtils.setField(field, task.get(), taskUser.get());
                } else {
                    ReflectionUtils.setField(field, task.get(), value);
                }
            });
            Task updatedTask = taskService.save(task.get());
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
