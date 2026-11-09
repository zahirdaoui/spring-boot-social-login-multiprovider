package com.OAuth2.OAuth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "login";
    }

   @GetMapping("/home")
   public String home(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
       if (oauth2User != null) {

           String name = oauth2User.getAttribute("name");
           if (name == null) {
               name = oauth2User.getAttribute("login");
           }
           String email = oauth2User.getAttribute("email");

           model.addAttribute("name", name);
           model.addAttribute("email", email);
       }
       return "home";
   }
}