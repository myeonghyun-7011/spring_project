package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
  public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
    Question question = questionService.getQuestion(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("question", question);
      return "question_detail";
    }


    // TODO: 답변을 저장한다.
    // 답변 등록 시작
    answerService.create(question, answerForm.getContent());
    // 답변 등록 끝
    return "redirect:/question/detail/%d".formatted(id);
  }
}