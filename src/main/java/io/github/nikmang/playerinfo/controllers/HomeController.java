package io.github.nikmang.playerinfo.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @RequestMapping
    public String welcome() {
        return "This is the API backend. API calls only.";
    }
}
