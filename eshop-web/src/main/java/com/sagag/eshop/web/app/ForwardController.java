package com.sagag.eshop.web.app;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForwardController {

  @GetMapping(path = {
      "/results/**",
      "/home/**",
      "/dvse/**",
      "/login/**",
      "/tyres/**",
      "/bulbs/**",
      "/oil/**",
      "/batteries/**",
      "/vin/**",
      "/offers/**",
      "/freetextsearch/**",
      "/admin/**",
      "/shopping-basket/**",
      "/shoppingorder/**",
      "/checkout/**",
      "/myaccount/**",
      "/forgotpassword/**",
      "/pdf/**",
      "/orderconfirm/**",
      "/search/**",
      "/return/**",
      "/article-list/**",
      "/order-dashboard/**",
      "/thule/add-buyers-guide",
      "/vehicle/**",
      "/moto/**",
      "/article/**",
      "/vehicle-filtering/**",
      "/settings/**",
      "/unicat/**",
      "/saginfo/**",
      "/404/**",
      "/wholesaler/**",
	    "/wsp/**",
      "/advance-vehicle-search/**"})
  public String index() {
    return "forward:/";
  }

  @PostMapping(value = "/thule/add-buyers-guide", consumes = MediaType.ALL_VALUE)
  public String handleThuleResponse(HttpServletRequest request, HttpServletResponse response,
    @RequestParam MultiValueMap<String, String> data) {
    data.toSingleValueMap().entrySet().stream()
      .map(entry -> new Cookie(entry.getKey(), StringUtils.replace(entry.getValue(), ",", "|")))
      .forEach(response::addCookie);
    response.addCookie(new Cookie("fromThule", Boolean.TRUE.toString()));
    return "redirect:/thule/add-buyers-guide";
  }
}
