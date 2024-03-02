package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/home")
  // 이 자리에 @ResponseBody가 없으면 resource/template/question_list.html 파일을 뷰로 삼는다.
  public String list() {
    return "home_main";
  }
}