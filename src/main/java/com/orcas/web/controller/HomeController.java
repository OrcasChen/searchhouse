package com.orcas.web.controller;

import com.orcas.base.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xcw on 2018/1/21.
 */
@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("name","陈柔妍");
        return "index";
    }

    @GetMapping("/404")
    public String notFoundPage() {
        return "404";
    }

    @GetMapping("/403")
    public String accessError() {
        return "403";
    }

    @GetMapping("/500")
    public String internalError() {
        return "500";
    }

    @GetMapping("/logout/page")
    public String logoutPage() {
        return "logout";
    }

}
