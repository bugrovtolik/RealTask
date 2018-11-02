package com.abugrov.helpinghand.repos;

import com.abugrov.helpinghand.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
    @Override
    Page<Task> findAll(Pageable pageable);

    Page<Task> findByTitle(String title, Pageable pageable);
}
