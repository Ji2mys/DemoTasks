package com.example.demo.services;

import com.example.demo.entities.Task;
import com.example.demo.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return this.repository.findAll();
    }

    public Task append(Task newTask) {
        return this.repository.save(newTask);
    }

    public Optional<Task> findById(Long id) {
        return this.repository.findById(id);
    }

    public Task save(Task task) {
        return repository.save(task);
    }
}
