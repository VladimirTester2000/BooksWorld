package ru.lesson.springBootProject.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lesson.springBootProject.security.details.UserDetailsImpl;

import java.util.Map;

@Controller
public class GreetingController {
    @GetMapping("/")
    public String root(Map<String, Object> map,
                       @RequestParam(value = "message",required = false)String message,
                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        map.put("message",message);
        return "main";
    }
}
