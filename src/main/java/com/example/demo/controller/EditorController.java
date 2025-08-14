package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/editor")
@PreAuthorize("hasRole('EDITOR')")
public class EditorController {

    private final ArticleRepository articleRepo;

    public EditorController(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }

    @GetMapping("/dashboard")
    public String editorDashboard(Model model) {
        List<Article> articles = articleRepo.findAll();
        model.addAttribute("articles", articles);
        return "editorDashboard"; // This must exist in templates folder!
    }

    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable Long id, Model model) {
        Article article = articleRepo.findById(id).orElse(null);
        if (article != null) {
            model.addAttribute("article", article);
            return "editorEditArticle";
        } else {
            return "redirect:/editor/dashboard";
        }
    }

    @PostMapping("/update")
    public String updateArticle(@ModelAttribute Article updatedArticle) {
        Article article = articleRepo.findById(updatedArticle.getId()).orElse(null);
        if (article != null) {
            article.setTitle(updatedArticle.getTitle());
            article.setContent(updatedArticle.getContent());
            articleRepo.save(article);
        }
        return "redirect:/editor/dashboard";
    }
    
 // SEARCH for Editor
    @GetMapping("/search")
    public String searchEditor(
            @RequestParam(value = "q", required = false) String q,
            Model model) {

        List<Article> articles = articleRepo.findAll();

        if (q != null && !q.isBlank()) {
            String searchLower = q.toLowerCase();
            articles = articles.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(searchLower) ||
                                 (a.getAuthor() != null && a.getAuthor().getUsername().toLowerCase().contains(searchLower)))
                    .toList();
        }

        model.addAttribute("articles", articles);
        model.addAttribute("q", q != null ? q : "");
        return "editorDashboard";
    }
}
