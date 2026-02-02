
//package com.example.demo.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.example.demo.controller.repo.UserRepository;
//import com.example.demo.entity.User;
//
//@Controller  // ✅ Changed from @RestController to @Controller
//public class ControllerClass {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/")
//    public String root() {
//        return "index";  // ✅ Return a template name, not raw HTML
//    }
//
//    @GetMapping("/home")
//    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        model.addAttribute("name", principal.getAttribute("name"));
//        model.addAttribute("login", principal.getAttribute("login"));
//        model.addAttribute("avatar_url", principal.getAttribute("avatar_url"));
//        return "home";
//    }
//
//    @GetMapping("/users")
//    @ResponseBody  // ✅ Only for this API endpoint
//    public List<User> listAllUsers() {
//        return userRepository.findAll();
//    }
//}