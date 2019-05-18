package com.abugrov.realtask.repos;

import com.abugrov.realtask.model.Comment;
import com.abugrov.realtask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    Comment findByAuthorAndReceiver(User author, User receiver);

    List<Comment> findByReceiver(User receiver);
}
