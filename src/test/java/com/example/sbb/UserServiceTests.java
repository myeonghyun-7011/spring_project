package com.example.sbb;

import com.example.sbb.answer.AnswerRepository;
import com.example.sbb.question.QuestionRepository;
import com.example.sbb.user.UserRepository;
import com.example.sbb.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {
  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;


  @BeforeEach
  void beforeEach() {
    clearData();
    createSampleData();
  }

  public static void createSampleData(UserService userService) {
    userService.create("admin", "admin@test.com", "1234");
    userService.create("user1", "user1@test.com", "1234");
  }

  private void createSampleData() {
    createSampleData(userService);
  }

  // 생성 유저 -> 질문 -> 답변 , 지우는것은 반대로 해줘야함.
  public static void clearData(UserRepository userRepository, AnswerRepository answerRepository,
                               QuestionRepository questionRepository) {
    // 답변먼저 삭제
    answerRepository.deleteAll();
    answerRepository.truncateTable();
    // 질문 삭제
    questionRepository.deleteAll();
    questionRepository.truncateTable();
    // user 삭제
    userRepository.deleteAll();
    userRepository.truncateTable(); // id = 1번으로
  }

  private void clearData() {
    clearData(userRepository, answerRepository, questionRepository);
  }

  @Test
  @DisplayName("회원가입이 가능하다.")
  public void t1() {
    userService.create("user2", "user2@email.com", "1234");
  }

}