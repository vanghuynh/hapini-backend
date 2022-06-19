package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.Comment;
import com.hapinistay.backend.model.House;

public interface CommentService {
	
	Comment findById(Integer id);

	void saveComment(Comment comment);

	void updateComment(Comment comment);

	void deleteCommentById(Integer id);

	void deleteAllComments();

	List<Comment> findAllComments();
	
	House addCommentToRoom(Comment comment, Long roomId);
}