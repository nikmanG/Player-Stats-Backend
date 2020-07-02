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
    /**
     * Retrieves the homepage for the backend.
     * There is no proper page for this.
     *
     * @return A string to tell you that there is no backend landing page
     */
    @RequestMapping(value = {"", "/"})
    public String welcome() {
        return "This is the API backend. API calls only.";
    }
}
