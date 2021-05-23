package com.instazoo.app.service;

import com.instazoo.app.dto.CommentDTO;
import com.instazoo.app.entity.Comment;
import com.instazoo.app.entity.Post;
import com.instazoo.app.entity.Users;
import com.instazoo.app.exception.PostNotFoundException;
import com.instazoo.app.repository.CommentRepository;
import com.instazoo.app.repository.PostRepository;
import com.instazoo.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        Users users = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + users.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(users.getId());
        comment.setUsername(users.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<Comment> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }


    private Users getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUsersByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
