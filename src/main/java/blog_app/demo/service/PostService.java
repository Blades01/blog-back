package blog_app.demo.service;

import blog_app.demo.dto.PostRequest;
import blog_app.demo.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Post createPost(PostRequest postRequest);

    Post getPostById(Long id);

    List<Post> getAllPosts();

    Page<Post> getAllPosts(Pageable pageable);

    Post updatePost(Long id, PostRequest postRequest);

    void deletePost(Long id);

    List<Post> searchPosts(String keyword);

    Page<Post> searchPosts(String keyword, Pageable pageable);

    Page<Post> getPostsByCategory(Long categoryId, Pageable pageable);
}
