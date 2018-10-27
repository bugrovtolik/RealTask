package com.abugrov.helpinghand.repos;

import com.abugrov.helpinghand.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByTitle(String title);
}
