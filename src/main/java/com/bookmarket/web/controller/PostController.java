package com.bookmarket.web.controller;

import com.bookmarket.web.entity.Post;
import com.bookmarket.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;

    // 게시글 목록
    @GetMapping("/list")
    public String postList(Model model) {
        List<Post> posts = postService.findAllPosts();
        model.addAttribute("posts", posts);
        return "board/list";
    }

    // 게시글 상세
    @GetMapping("/view/{id}")
    public String postView(@PathVariable("id") Long id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "board/view";
    }

    // 게시글 작성 폼
    @GetMapping("/form")
    public String postForm(Model model) {
        model.addAttribute("post", new Post());
        return "board/form";
    }

    // 게시글 수정 폼
    @GetMapping("/form/{id}")
    public String postEditForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Post post = postService.findPostById(id);
        if (!post.getAuthor().getUsername().equals(authentication.getName())) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }
        model.addAttribute("post", post);
        return "board/form";
    }

    // 게시글 등록 및 수정 처리
    @PostMapping("/form")
    public String savePost(@ModelAttribute Post post, Authentication authentication) {
        String username = authentication.getName();
        if (post.getId() == null) { // 신규 등록
            postService.savePost(post.getTitle(), post.getContent(), username);
        } else { // 수정
            postService.updatePost(post.getId(), post.getTitle(), post.getContent(), username);
        }
        return "redirect:/board/list";
    }

    // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.deletePost(id, username);
        return "redirect:/board/list";
    }
}
