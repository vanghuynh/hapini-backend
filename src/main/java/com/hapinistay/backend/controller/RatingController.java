package com.hapinistay.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hapinistay.backend.model.Comment;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.service.CommentService;
import com.hapinistay.backend.util.CustomErrorType;

@RestController
@RequestMapping("/rating")
public class RatingController {

	public static final Logger logger = LoggerFactory.getLogger(RatingController.class);

	@Autowired
	CommentService commentService; //Service which will do all data retrieval/manipulation work
	

	// -------------------Retrieve All customers---------------------------------------------

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Comment>> listAllCustomers() {
		List<Comment> states = commentService.findAllComments();
		if (states.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Comment>>(states, HttpStatus.OK);
	}
	
	
	// -------------------Retrieve Single state------------------------------------------

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getComment(@PathVariable("id") Integer id) {
		logger.info("Fetching Comment with id {}", id);
		Comment state = commentService.findById(id);
		if (state == null) {
			logger.error("Comment with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Comment>(state, HttpStatus.OK);
	}

	// -------------------Create a state-------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> createComment(@RequestBody Comment comment, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Comment : {}", comment);

		commentService.saveComment(comment);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/state/{id}").buildAndExpand(comment.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
		
		// -------------------Create a room rating-------------------------------------------
		@RequestMapping(value = "/room/{roomId}", method = RequestMethod.POST)
		@Transactional
		public ResponseEntity<?> createRoomComment(@PathVariable("roomId") Long roomId, @RequestBody Comment comment) {
			logger.info("Creating Comment : {}", comment);

			if (roomId == null) {
				logger.error("Comment with room id {} not found.", roomId);
				return new ResponseEntity(new CustomErrorType("Room with id " + roomId 
						+ " not found"), HttpStatus.NOT_FOUND);
			}
			House room = this.commentService.addCommentToRoom(comment, roomId);
			System.out.println(room);

			return new ResponseEntity<String>("Added comments to room " + roomId, HttpStatus.CREATED);
		}
}