package org.pizzeria.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    
     @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalida la sessione
        request.getSession().invalidate();

        // Cancella eventuali cookie di autenticazione
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // Reindirizza alla login page
        return "redirect:/login?logout";
    }
}
