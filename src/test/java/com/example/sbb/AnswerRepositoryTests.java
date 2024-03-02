package com.example.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;

  @BeforeEach
    // 프로그램 실행할때 딱 한번 실행
  void beforeEach() {
    clearData();
    createSampleData();
  }

  private void clearData() {
    QuestionRepositoryTests.clearData(questionRepository);

    answerRepository.deleteAll();
    answerRepository.truncateTable(); // 중복방지
  }

  private void createSampleData() {
    // 관련 답변이 하나도 없는 상태에서 쿼리 발생
    QuestionRepositoryTests.createSampleData(questionRepository);

    Question q = questionRepository.findById(1).get();

    Answer a1 = new Answer();
    a1.setContent("sbb는 질문답변 게시판입니다.");
    a1.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
    a1.setCreateDate(LocalDateTime.now());
    answerRepository.save(a1);

    q.getAnswerList().add(a1);

    Answer a2 = new Answer();
    a2.setContent("sbb에서는 주로 Springboot를 다룹니다.");
    a2.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
    a2.setCreateDate(LocalDateTime.now());
    answerRepository.save(a2);

    q.getAnswerList().add(a2);

    questionRepository.save(q);
  }

  @Test
  void 저장() {
    Question q = questionRepository.findById(2).get();
    Answer a = new Answer();
    a.setContent("네 자동으로 생성됩니다.");
    a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
    a.setCreateDate(LocalDateTime.now());
    answerRepository.save(a);
  }

  @Test
  void 조회() {
    Answer a = answerRepository.findById(1).get();
    assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판입니다.");
  }

  @Test
  void 관련된_qeustion_조회() {
    Answer a = answerRepository.findById(1).get(); // 1번 답변을가져옴
    Question q = a.getQuestion(); // 1번 답변에 대한 질문을 가져옴.

    assertThat(q.getId()).isEqualTo(1); // 1번이 맞냐
  }

  @Test
  @Transactional // 메서드가 실행되면 BeforEach로 묶임.
  @Rollback(false) // DB연결 유지
  void question으로부터_관련되_질문들_조회() {
    // SELECT * FROM question WHERE id = 1;
    Question q = questionRepository.findById(1).get(); // 1번 질문 가져옴.
    // DB 연결이 끊김

    // SELECT * FROM answer WHERE question_id = 1;
    List<Answer> answerList = q.getAnswerList();
    // question으로부터 답변이 여러개 이므로 List형태로 구현.

    assertThat(answerList.size()).isEqualTo(2); // 답변이 2개 같은지 확인
    assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판입니다."); // 0번째 질문 답변 같은지 확인
  }

}

