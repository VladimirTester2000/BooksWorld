package ru.lesson.springBootProject.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.lesson.springBootProject.exceptions.BookServiceException;
import ru.lesson.springBootProject.models.Comment;

import java.util.Optional;

public interface CommentService {
    Page findAllByUser(Long userId, Pageable pageable);

    Page findAll(Pageable pageable);

    Comment save(Comment comment) throws BookServiceException;

    Optional<? extends Comment> findByIdAndInstanceofWithOwner(Comment oldComment);
}
