package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ArticleRepository articleRepo;
    private final UserRepository userRepo;

    public AdminController(ArticleRepository articleRepo, UserRepository userRepo) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Article> articles = articleRepo.findAll();
        model.addAttribute("articles", articles);
        return "adminDashboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("article", new Article());
        return "adminAddArticle";
    }

    @PostMapping("/add")
    public String addArticle(@ModelAttribute Article article, Principal principal) {
        article.setAuthor(userRepo.findByUsername(principal.getName()).orElse(null));
        article.setApproved(false);
        articleRepo.save(article);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        articleRepo.findById(id).ifPresent(article -> model.addAttribute("article", article));
        return "adminEditArticle";
    }

    @PostMapping("/update")
    public String updateArticle(@ModelAttribute Article updatedArticle) {
        articleRepo.findById(updatedArticle.getId()).ifPresent(existing -> {
            existing.setTitle(updatedArticle.getTitle());
            existing.setContent(updatedArticle.getContent());
            existing.setApproved(updatedArticle.isApproved());
            articleRepo.save(existing);
        });
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleRepo.deleteById(id);
        return "redirect:/admin/dashboard";
        
        
        
    }
    // SEARCH for Admin
    @GetMapping("/search")
    public String searchAdmin(
            @RequestParam(value = "q", required = false) String q,
            Model model) {

        List<Article> list = (q != null && !q.isBlank())
                ? articleRepo.findByTitleContainingIgnoreCaseOrAuthor_UsernameContainingIgnoreCase(q, q)
                : articleRepo.findAll();

        model.addAttribute("articles", list);
        model.addAttribute("q", q != null ? q : "");
        return "adminDashboard";
    }
}
