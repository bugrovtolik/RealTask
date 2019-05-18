package com.abugrov.realtask.service;

import com.abugrov.realtask.model.Category;
import com.abugrov.realtask.model.Task;
import com.abugrov.realtask.model.User;
import com.abugrov.realtask.repos.CategoryRepo;
import com.abugrov.realtask.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final CategoryRepo categoryRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, CategoryRepo categoryRepo) {
        this.taskRepo = taskRepo;
        this.categoryRepo = categoryRepo;
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

    public Map<Category, List<Category>> getCategories() {
        Map<Category, List<Category>> categories = new LinkedHashMap<>();

        List<Category> parents = categoryRepo.findAllByParentIsNull();

        for (Category parent : parents) {
            categories.put(parent, categoryRepo.findAllByParent(parent));
        }

        return categories;
    }

    public void deleteTask(Task task) {
        taskRepo.delete(task);
    }

    public void updateTask(Task oldTask, Task newTask) {
        oldTask.setTitle(newTask.getTitle());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setSecret(newTask.getSecret());
        oldTask.setLat(newTask.getLat());
        oldTask.setLng(newTask.getLng());
        oldTask.setExecFrom(newTask.getExecFromFormatted());
        oldTask.setExecTo(newTask.getExecToFormatted());
        oldTask.setPrice(newTask.getPrice());
        oldTask.setCashless(newTask.isCashless());
        oldTask.setCategory(newTask.getCategory());

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
