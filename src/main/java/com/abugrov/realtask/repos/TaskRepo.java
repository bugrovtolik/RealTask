package com.abugrov.realtask.repos;

import com.abugrov.realtask.domain.Task;
import com.abugrov.realtask.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepo extends JpaRepository<Task, Long> {
    @Override
    Page<Task> findAll(Pageable pageable);

    Page<Task> findByTitleAndActive(String title, boolean b, Pageable pageable);

    Page<Task> findByActive(boolean b, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Task set active = false where current_timestamp >= exec_to")
    void checkExpired();

    Page<Task> findByAuthor(User author, Pageable pageable);
}
