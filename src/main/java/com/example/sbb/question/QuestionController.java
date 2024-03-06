package com.example.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/question") // <- 중복 제거
@RequiredArgsConstructor //생성자 주입
@Controller
// 컨트롤러는 Repository가 있는지 몰라야한다.
// 서비스는 브라우저라는 것이 이 세상에 존재하는지 몰라야한다.
// 세션은 어디서 다뤄야 할까요?
// 컨트롤러
// 서비스
// 리포지터리는 서비스가 이 세상에 있는지 몰라야한다.
// 서비스는 컨트롤러를 몰라야한다.
// DB는 리포지터리를 몰라야한다.
// SPRING DATA JPA는 MySqL을 몰라야한다.
// SPRING DATA JPA(리포지터리) -> JPA -> 하이버네이트 -> JDBC -> MYSQL driver -> MYSQL

public class QuestionController {

  // @Autowired 필드 주입
  // @Autowired를 대신에 @RequiredArgsConstructor, final 사용하면 동일함.
  // final이 붙는것들은 생성자 주입
  private final QuestionService questionService;

  @GetMapping("/list")
  // 이 자리에 @ResponseBody가 없으면 resource/template/question_list.html 파일을 뷰로 삼는다.
  public String list(Model model) {
    List<Question> questionList = questionService.getList();

    // 미리 실행된 question_list.html에서
    // questionList 라는 이름으로 questionList 변수를 사용할 수 있다.
    model.addAttribute("questionList", questionList);
    return "question_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model, @PathVariable int id) {
    Question question = questionService.getQuestion(id);

    model.addAttribute("question", question);

    return "question_detail";
  }

  @GetMapping("/create") // form을 보여주기 위한 메서드
  public String questionCreate() {
    return "question_form";
  }

  @PostMapping("/create") // 저장하고 list로 다시 돌아가기 위한 메서드
  public String questionCreate( String subject,  String content) { // question_form.html 에서 등록되 내용이 저장됨.

    questionService.create(subject,content); // 제목과 내용을 QuestionRepository에저장

    return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
  }
}