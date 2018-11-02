package com.abugrov.helpinghand.repos;

import com.abugrov.helpinghand.domain.Comment;
import com.abugrov.helpinghand.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByTask(Task task);

    void deleteByTask(Task task);
}
