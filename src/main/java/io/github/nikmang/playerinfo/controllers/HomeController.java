package io.github.nikmang.playerinfo.controllers;

import org.springframework.web.bind.annotation.*;


@CrossOrigin(
        allowCredentials = "true",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
public class HomeController {
    @RequestMapping
    public String welcome() {
        return "This is the API backend. API calls only.";
    }
}
