package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.model.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/contributor")
@PreAuthorize("hasRole('CONTRIBUTOR')")
public class ContributorController {

    private final ArticleRepository articleRepo;
    private final UserRepository userRepo;

    public ContributorController(ArticleRepository articleRepo, UserRepository userRepo) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/dashboard")
    public String contributorDashboard(Model model, Principal principal) {
        User contributor = userRepo.findByUsername(principal.getName()).orElse(null);

        List<Article> drafts = articleRepo.findByAuthorAndApproved(contributor, false);
        List<Article> published = articleRepo.findByApproved(true);

        model.addAttribute("drafts", drafts);
        model.addAttribute("published", published);
        model.addAttribute("username", contributor.getUsername());

        return "contributorDashboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("article", new Article());
        return "contributorAddArticle";
    }

    @PostMapping("/add")
    public String addArticle(@ModelAttribute Article article, Principal principal) {
        User contributor = userRepo.findByUsername(principal.getName()).orElse(null);
        article.setAuthor(contributor);
        article.setApproved(false);
        articleRepo.save(article);
        return "redirect:/contributor/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        articleRepo.findById(id).ifPresent(article -> model.addAttribute("article", article));
        return "contributorEditArticle";
    }

    @PostMapping("/update")
    public String updateArticle(@ModelAttribute Article updatedArticle, Principal principal) {
        User contributor = userRepo.findByUsername(principal.getName()).orElse(null);

        articleRepo.findById(updatedArticle.getId()).ifPresent(existing -> {
            if (existing.getAuthor().getId().equals(contributor.getId())) {
                existing.setTitle(updatedArticle.getTitle());
                existing.setContent(updatedArticle.getContent());
                articleRepo.save(existing);
            }
        });
        return "redirect:/contributor/dashboard";
    }
    
    // SEARCH for Contributor
    @GetMapping("/search")
    public String searchContributor(
            @RequestParam(value = "q", required = false) String q,
            Model model,
            Principal principal) {

        User contributor = userRepo.findByUsername(principal.getName()).orElseThrow();
        List<Article> drafts = articleRepo.findByAuthorAndApproved(contributor, false);
        List<Article> published = articleRepo.findByAuthorAndApproved(contributor, true);

        if (q != null && !q.isBlank()) {
            String searchLower = q.toLowerCase();
            drafts = drafts.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(searchLower))
                    .toList();
            published = published.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(searchLower))
                    .toList();
        }

        model.addAttribute("drafts", drafts);
        model.addAttribute("published", published);
        model.addAttribute("q", q != null ? q : "");
        return "contributorDashboard";
    }
}
