package com.example.sbb.answer;

import ch.qos.logback.core.model.Model;
import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/answer") // <- 중복 제거
@RequiredArgsConstructor //생성자 주입
@Controller
public class AnswerController {

  private final QuestionService questionService;
  private final AnswerService answerService;


  @PostMapping("/create/{id}")
  public String createAnswer(Model model, @PathVariable("id") Integer id, String content) {
    Question question = questionService.getQuestion(id);

    // TODO: 답변을 저장한다.
    // 답변 등록 시작
    answerService.create(question, content);
    // 답변 등록 끝
    return "redirect:/question/detail/%d".formatted(id);
  }
}