package blog_app.demo.service;

import blog_app.demo.dto.PostRequest;
import blog_app.demo.model.Category;
import blog_app.demo.model.Post;
import blog_app.demo.model.Tag;
import blog_app.demo.repository.CategoryRepository;
import blog_app.demo.repository.PostRepository;
import blog_app.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Post createPost(PostRequest postRequest) {
        Post post = mapPostRequestToPost(postRequest);
        return postRepository.save(post);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post updatePost(Long id, PostRequest postRequest) {
        Post existingPost = getPostById(id);

        existingPost.setTitle(postRequest.getTitle());
        existingPost.setContent(postRequest.getContent());
        existingPost.setImageUrl(postRequest.getImageUrl());

        if (postRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + postRequest.getCategoryId()));
            existingPost.setCategory(category);
        } else {
            existingPost.setCategory(null);
        }

        Set<Tag> tags = resolveTagsFromNames(postRequest.getTags());
        existingPost.setTags(tags);

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public Page<Post> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable);
    }

    private Post mapPostRequestToPost(PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());

        if (postRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + postRequest.getCategoryId()));
            post.setCategory(category);
        }

        Set<Tag> tags = resolveTagsFromNames(postRequest.getTags());
        post.setTags(tags);

        return post;
    }

    private Set<Tag> resolveTagsFromNames(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        return tagNames.stream()
                .map(this::getOrCreateTagByName)
                .collect(Collectors.toSet());
    }

    private Tag getOrCreateTagByName(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    return tagRepository.save(newTag);
                });
    }
}
