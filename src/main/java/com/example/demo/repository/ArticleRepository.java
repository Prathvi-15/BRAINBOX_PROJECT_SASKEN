package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthor(User author);
    List<Article> findByAuthorAndApproved(User author, boolean approved);
    List<Article> findByApproved(boolean approved);
    
    // ✅ This line fixes the ReviewerController error
    List<Article> findByApprovedFalse();
    List<Article> findByTitleContainingIgnoreCaseOrAuthor_UsernameContainingIgnoreCase(String title, String username);
   // List<Article> findByTitleContainingIgnoreCaseOrAuthorUsernameContainingIgnoreCase(String title, String username);
    List<Article> findByAuthorUsernameContainingIgnoreCaseOrTitleContainingIgnoreCase(String author, String title);
}
