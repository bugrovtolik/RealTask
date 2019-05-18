package com.abugrov.realtask.service;

import com.abugrov.realtask.model.Comment;
import com.abugrov.realtask.model.User;
import com.abugrov.realtask.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    Comment findByAuthorAndReceiver(User author, User receiver) {
        return commentRepo.findByAuthorAndReceiver(author, receiver);
    }

    List<Comment> findByReceiver(User receiver) {
        return commentRepo.findByReceiver(receiver);
    }

    void saveComment(Comment comment) {
        commentRepo.save(comment);
    }

    void deleteComment(Comment comment) {
        commentRepo.delete(comment);
    }
}
