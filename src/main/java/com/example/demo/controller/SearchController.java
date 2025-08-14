package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ArticleRepository articleRepo;

    public SearchController(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "q", required = false) String query,
            Model model) {

        if (query == null || query.isBlank()) {
            model.addAttribute("articles", List.of());
            model.addAttribute("q", "");
        } else {
            List<Article> results =
                    articleRepo.findByAuthorUsernameContainingIgnoreCaseOrTitleContainingIgnoreCase(query, query);
            model.addAttribute("articles", results);
            model.addAttribute("q", query);
        }
        return "searchResults";
    }
}