package com.abugrov.helpinghand.service;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
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

    private void checkExpired() {
        taskRepo.checkExpired();
    }

    public Page<Task> findAllActive(Pageable pageable) {
        checkExpired();
        return taskRepo.findByActive(true, pageable);
    }

    public Page<Task> findAllNotActive(Pageable pageable) {
        checkExpired();
        return taskRepo.findByActive(false, pageable);
    }

    public Page<Task> findByTitleActive(String filter, Pageable pageable) {
        checkExpired();
        return taskRepo.findByTitleAndActive(filter, true, pageable);
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

    public void deactivate(Task task) {
        task.setActive(false);
        taskRepo.save(task);
    }

    public Page<Task> findByAuthor(User author, Pageable pageable) {
        checkExpired();
        return taskRepo.findByAuthor(author, pageable);
    }
}
