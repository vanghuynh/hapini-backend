package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}
