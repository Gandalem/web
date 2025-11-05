package com.bookmarket.web.service;

import com.bookmarket.web.entity.Post;
import com.bookmarket.web.entity.User;
import com.bookmarket.web.repository.PostRepository;
import com.bookmarket.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public Long savePost(String title, String content, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        postRepository.save(post);
        return post.getId();
    }

    // 게시글 수정
    public Long updatePost(Long postId, String title, String content, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
        return post.getId();
    }

    // 게시글 삭제
    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    // 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    // ID로 게시글 조회
    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
    }
}
