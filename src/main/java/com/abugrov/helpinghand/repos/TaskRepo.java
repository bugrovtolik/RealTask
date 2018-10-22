package com.abugrov.helpinghand.repos;

import com.abugrov.helpinghand.domain.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepo extends CrudRepository<Task, Long> {

    List<Task> findByTitle(String title);
}
