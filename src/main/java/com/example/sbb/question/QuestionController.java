package com.example.sbb.question;

import com.example.sbb.answer.AnswerForm;
import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

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
  private final UserService userService;

  @GetMapping("/list")
  // 이 자리에 @ResponseBody가 없으면 resource/template/question_list.html 파일을 뷰로 삼는다.
  public String list(HttpSession session, Model model, @RequestParam(defaultValue="0") int page) {

    Object o = session.getAttribute("SPRING_SECURITY_CONTEXT");
    System.out.println(o);

      Page<Question> paging = questionService.getList(page);


    // 미리 실행된 question_list.html에서
    // questionList 라는 이름으로 questionList 변수를 사용할 수 있다.
    model.addAttribute("paging", paging);
    return "question_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model, @PathVariable int id, AnswerForm answerForm) {
    Question question = questionService.getQuestion(id);

    model.addAttribute("question", question);

    return "question_detail";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
    Question question = this.questionService.getQuestion(id);

    if(!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    questionForm.setSubject(question.getSubject());
    questionForm.setContent(question.getContent());
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Integer id) {
    if (bindingResult.hasErrors()) {
      return "question_form";
    }

    Question question = this.questionService.getQuestion(id);

    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
    return String.format("redirect:/question/detail/%s", id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
    Question question = this.questionService.getQuestion(id);

    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }

    questionService.delete(question);
    return "redirect:/question/list";
  }


  // is + Authenticated 인증되어야만 들어가지게끔.
  // 로그인이 안되어 있을시 로그인창으로 들어가지고
  // 로그인이 되는 순간 클릭햇던 질문 리스트 폼으로 자동 생성.
  // 로그인만되면 아래꺼 실행가능
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/create") // form을 보여주기 위한 메서드
  public String questionCreate(QuestionForm questionForm) {
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  // 저장하고 list로 다시 돌아가기 위한 메서드
  // question_form.html 에서 등록되 내용이 저장됨.
  // @Valid => 그 룰에 맞게 작성한 내용을 체크해서 questionForm 에 넣어줌, 성공, 실패는 bindingResult에서 관리.
  // 유효성 검사를 함.
  public String questionCreate(Principal principal, Model model, @Valid QuestionForm questionForm, BindingResult bindingResult) {

    // 에러있는지 확인 절차.
    if (bindingResult.hasErrors()) {
      return "question_form";
    }

    SiteUser siteUser = userService.getUser(principal.getName());

    questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

    return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String questionVote(Principal principal, @PathVariable("id") Integer id) {
    Question question = questionService.getQuestion(id);
    SiteUser siteUser = userService.getUser(principal.getName());

    questionService.vote(question, siteUser);

    return "redirect:/question/detail/%d".formatted(id);
  }
}