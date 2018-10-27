package com.abugrov.helpinghand.service;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    public List<Task> findByTitle(String filter) {
        return taskRepo.findByTitle(filter);
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
