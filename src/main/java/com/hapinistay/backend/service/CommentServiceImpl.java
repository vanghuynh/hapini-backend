package com.hapinistay.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Comment;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.repositories.CommentRepository;



@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private HouseService roomService;

	@Override
	public Comment findById(Integer id) {
		return commentRepository.findOne(id.longValue());
	}

	@Override
	public void saveComment(Comment comment) {
		commentRepository.save(comment);
		
	}

	@Override
	public void updateComment(Comment comment) {

		this.commentRepository.save(comment);
	}

	@Override
	public void deleteCommentById(Integer id) {
		this.commentRepository.delete(id.longValue());
		
	}

	@Override
	public void deleteAllComments() {
		this.commentRepository.deleteAll();
		
	}

	@Override
	public List<Comment> findAllComments() {
		return this.commentRepository.findAll();
	}

	@Override
	public House addCommentToRoom(Comment comment, Long roomId) {
		House room = this.roomService.findById(roomId, "vi");
		if(room != null) {
			comment.setRoom(room);
			this.commentRepository.save(comment);
			return room;
		}
		return null;
	}

}
