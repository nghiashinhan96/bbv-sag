package com.sagag.eshop.web.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
      "/offers/**",
      "/freetextsearch/**",
      "/admin/**",
      "/shoppingbasket/**",
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
	  "/cars/**",
	  "/catelogues/**",
	  "/administration/**",
	  "/shopping-basket/**",
	  "/catelogues/**",
	  "/unauthorized/**",
	  "/price-offers/**",
    "/showpage/**",
    "/404/**",
    "/advance-vehicle-search/**"})
  public String index() {
    return "forward:/";
  }

}
