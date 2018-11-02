package com.abugrov.helpinghand.service;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public void saveTask(Task task) {
        taskRepo.save(task);
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskRepo.findAll(pageable);
    }

    public Page<Task> findByTitle(String filter, Pageable pageable) {
        return taskRepo.findByTitle(filter, pageable);
    }

    public void deleteTask(Task task) { taskRepo.delete(task); }

    public void updateTask(Task oldTask, Task newTask) {
        oldTask.setTitle(newTask.getTitle());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setSecret(newTask.getSecret());
        oldTask.setLat(newTask.getLat());
        oldTask.setLng(newTask.getLng());
        oldTask.setExecFrom(newTask.getExecFromFormatted());
        oldTask.setExecTo(newTask.getExecToFormatted());
        oldTask.setPrice(newTask.getPrice());

        taskRepo.save(oldTask);
    }
}
