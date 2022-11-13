package com.sagag.eshop.backoffice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

  @RequestMapping(path = {
      "/home/**",
      "/login/**",
      "/affiliates/**",
      "/customers/**",
      "/users/**",
      "/sales/**",
      "/message/**",
      "/branches/**",
      "/opening-day/**",
      "/administration/**"
      })
  public String index() {
    return "forward:/";
  }

}
