package com.instazoo.app.repository;

import com.instazoo.app.entity.Post;
import com.instazoo.app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUsersOrderByCreatedDateDesc(Users users);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUsers(Long id, Users users);
}
