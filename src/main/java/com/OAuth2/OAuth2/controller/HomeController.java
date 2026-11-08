package com.OAuth2.OAuth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "login";
    }

   /* @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return "مستخدم غير مسجل!";
        }

        // استخراج بيانات المستخدم من الـ OAuth2User
        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        String picture = oauth2User.getAttribute("picture");


        return "html";
    }*/
   @GetMapping("/home")
   public String home(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
       if (oauth2User != null) {
           // استخراج البيانات من كائن OAuth2User لعرضها في الصفحة
           String name = oauth2User.getAttribute("name");
           if (name == null) {
               name = oauth2User.getAttribute("login"); // في حال كان غيت هب ولا يوجد اسم صريح
           }
           String email = oauth2User.getAttribute("email");

           // إضافة البيانات إلى النموذج (Model) لتمريرها لواجهة الـ HTML
           model.addAttribute("name", name);
           model.addAttribute("email", email);
       }
       return "home";
   }
}