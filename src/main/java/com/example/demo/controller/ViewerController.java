package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/viewer")
@PreAuthorize("hasRole('VIEWER')")
public class ViewerController {

    private final ArticleRepository articleRepo;

    public ViewerController(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }

    @GetMapping("/dashboard")
    public String viewArticles(Model model) {
        List<Article> approvedArticles = articleRepo.findByApproved(true);
        model.addAttribute("articles", approvedArticles);
        return "viewerDashboard";
    }
 // SEARCH for Viewer
    @GetMapping("/search")
    public String searchViewer(
            @RequestParam(value = "q", required = false) String q,
            Model model) {

        List<Article> approvedArticles = articleRepo.findByApproved(true);

        if (q != null && !q.isBlank()) {
            String searchLower = q.toLowerCase();
            approvedArticles = approvedArticles.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(searchLower) ||
                                 (a.getAuthor() != null && a.getAuthor().getUsername().toLowerCase().contains(searchLower)))
                    .toList();
        }

        model.addAttribute("articles", approvedArticles);
        model.addAttribute("q", q != null ? q : "");
        return "viewerDashboard";
    }
}
