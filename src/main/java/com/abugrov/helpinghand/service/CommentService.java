package com.abugrov.helpinghand.service;

import com.abugrov.helpinghand.domain.Comment;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void saveComment(Comment comment) {
        commentRepo.save(comment);
    }

    public List<Comment> findAll() {
        return commentRepo.findAll();
    }

    public void deleteByTask(Task task) {
        commentRepo.deleteByTask(task);
    }
}
